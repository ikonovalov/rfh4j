package ru.codeunited.wmq.format;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.ibm.mq.headers.MQHeader;
import com.ibm.mq.headers.MQRFH2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import ru.codeunited.wmq.messaging.HeaderUtilService;
import ru.codeunited.wmq.messaging.MessageTools;
import ru.codeunited.wmq.messaging.pcf.*;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.ibm.mq.constants.MQConstants.*;


/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 02.02.15.
 */
@Singleton
public class MQFMTAdminActivityTraceFormatterDepFin extends MQActivityTraceFormatter<String> {

    private static final int BUFFER_2Kb = 2048;

    private static final int MAX_BODY_LENGTH = 1024 * 1024; // 1Mb

    private static final int BODY_SLOT = 1;

    private volatile Optional<String> passedOptionsStr = Optional.absent();

    private volatile Optional<List<Pair<String, String>>> passedOptionsList = Optional.absent();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    MQFMTAdminActivityTraceFormatterDepFin() {
        super();
    }

    private final static ActivityRecordFilter OPERATION_FILTER = new ActivityRecordXFOperationFilter(
            MQXFOperation.MQXF_GET,
            MQXFOperation.MQXF_PUT,
            MQXFOperation.MQXF_PUT1
    );

    @Inject
    public void setPassedOptions(@PassedFormatterOptions @Nullable String options) {
        if (options == null) {
            return;
        }
        try {
            if (lock.writeLock().tryLock(1, TimeUnit.SECONDS)) {
                List<Pair<String, String>> listOfPairs = new ArrayList<>();
                String parseStr = options.replace("[", "").replace("]", "").trim();
                Iterable<String> pairsAsString = Splitter.on(';').trimResults().split(parseStr);
                for (String pairStr : pairsAsString) {
                    Iterator<String> splittedPairIterator = Splitter.on('.').split(pairStr).iterator();
                    listOfPairs.add(new ImmutablePair<>(splittedPairIterator.next(), splittedPairIterator.next()));
                }
                passedOptionsList = Optional.of(Collections.unmodifiableList(listOfPairs));
                passedOptionsStr = Optional.of(options);
            } else {
                throw new TimeoutException();
            }
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private Optional<List<Pair<String, String>>> getPassedOptions() {
        Optional<List<Pair<String, String>>> list = Optional.absent();
        try {
            if (lock.readLock().tryLock(1, TimeUnit.SECONDS)) {
                list = passedOptionsList;
            } else {
                throw new TimeoutException();
            }
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        } finally {
            lock.readLock().unlock();
        }
        return list;
    }


    @Override
    public String format(final ActivityTraceCommand activityCommand) {
        try {
            if (lock.readLock().tryLock(1, TimeUnit.SECONDS)) {

                final StringBuffer buffer = new StringBuffer(BUFFER_2Kb);

                boolean allowOutput = false;

                List<ActivityTraceRecord> records = activityCommand.getRecords();

                for (ActivityTraceRecord record : records) {
                    if (!OPERATION_FILTER.allowed(record)) // skip failed
                        continue;
                    if (record.getOperation().anyOf(MQXFOperation.MQXF_GET, MQXFOperation.MQXF_PUT)) {
                        MQXFMessageMoveRecord moveRecord = (MQXFMessageMoveRecord) record;
                        buffer.append(moveRecord.getPutDateTimeISO()).append(';');

                        boolean xmitExchange = moveRecord.isTransmissionMessage();

                        buffer.append( /* append message id */
                                xmitExchange ?
                                        moveRecord.getXMITMessageId() :
                                        moveRecord.getMessageId()
                        ).append(';');

                        buffer.append( /* append queue name */
                                xmitExchange ?
                                        String.format("%s;%s;",
                                                extractQueueName(moveRecord),
                                                moveRecord.getXMITRemoteQueueName()
                                        ) :
                                        String.format("%s;;",
                                                extractQueueName(moveRecord)
                                        )
                        );

                        buffer.append(moveRecord.getOperation().name()).append(';'); /* append operation name */
                        buffer.append(moveRecord.getCompCode()).append(';'); /* append operation status: failed or not */

                        buffer.append( /* append queuemanager name */
                                xmitExchange ?
                                        String.format("%s;%s;",
                                                extractQManagerName(moveRecord),
                                                moveRecord.getXMITRemoteQueueMananger()
                                        ) :
                                        String.format("%s;;",
                                                extractQManagerName(moveRecord)
                                        )
                        );

                        buffer.append(moveRecord.getMessageLength()).append(';');
                        buffer.append(activityCommand.getApplicationName()).append(';');
                        buffer.append(activityCommand.getApplicationType()).append(';');
                        buffer.append(activityCommand.getUserId()).append(";");

                        // ================================================================================
                        // print captured data (it has four slots: headerdata xDynamic, bodydata x1 (last))
                        final TraceData traceData = moveRecord.getData();
                        final String format = moveRecord.getFormat();

                        final Optional<List<Pair<String, String>>> passedList = getPassedOptions();
                        final String[] capturedOutBlock = passedList.isPresent() ? new String[passedList.get().size() + BODY_SLOT] : new String[BODY_SLOT];
                        Arrays.fill(capturedOutBlock, "");

                        final Optional<List<MQHeader>> listOfHeadersOpt = traceData.getHeaders();
                        final Optional<Object> bodyOpt = traceData.getBody();

                        switch (format) {
                            case MQFMT_RF_HEADER_2:
                                if (listOfHeadersOpt.isPresent()) {
                                    List<MQHeader> headers = listOfHeadersOpt.get();
                                    MQRFH2 mqrfh2 = (MQRFH2) headers.get(0);
                                    if (passedList.isPresent()) {
                                        moveRFH2toCaptureBlock(capturedOutBlock, mqrfh2);
                                    }

                                    if (bodyOpt.isPresent()) {
                                        if (MQFMT_STRING.equals(mqrfh2.getFormat())) {
                                            moveBodyToCaptureBlock(capturedOutBlock, (String) bodyOpt.get());
                                        } else {
                                            moveBodyToCaptureBlock(capturedOutBlock, (byte[]) bodyOpt.get());
                                        }
                                    }
                                }
                                break;
                            case MQFMT_NONE:
                            case MQFMT_ADMIN:
                                if (bodyOpt.isPresent()) {
                                    moveBodyToCaptureBlock(capturedOutBlock, (byte[]) bodyOpt.get());
                                }
                                break;
                            case MQFMT_STRING:
                                if (bodyOpt.isPresent()) {
                                    moveBodyToCaptureBlock(capturedOutBlock, (String) bodyOpt.get());
                                }
                                break;
                            default:
                        }
                        buffer.append(Joiner.on(";").join(capturedOutBlock));
                        // end of printing captured data

                        buffer.append('\n');
                    }

                    allowOutput = true;
                }

                if (!allowOutput) { // drop buffer in it contains nothing interesting.
                    buffer.setLength(0);
                }
                return buffer.toString().trim();
            } else {
                throw new TimeoutException();
            }
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        } finally {
            lock.readLock().unlock();
        }
    }

    private void moveBodyToCaptureBlock(String[] capturedOutBlock, String stringBody) {
        capturedOutBlock[bodyBlockPosition(capturedOutBlock)] = trimBodyToMaxSize(stringBody);
    }

    private void moveBodyToCaptureBlock(String[] capturedOutBlock, byte[] binaryBody) {
        moveBodyToCaptureBlock(capturedOutBlock, MessageTools.bytesToHex(binaryBody));
    }

    private int bodyBlockPosition(String[] capturedOutBlock) {
        return capturedOutBlock.length - BODY_SLOT;
    }

    public void moveRFH2toCaptureBlock(String[] capturedOutBlock, MQRFH2 mqrfh2) {
        int index = 0;
        for (Pair<String, String> folderAndField : passedOptionsList.get()) {
            capturedOutBlock[index++] = HeaderUtilService.getStringFromRHF2FolderSafe(mqrfh2, folderAndField.getLeft(), folderAndField.getRight());
        }
    }

    private static String trimBodyToMaxSize(String realBody) {
        if (realBody.length() > MAX_BODY_LENGTH) {
            realBody = realBody.substring(0, MAX_BODY_LENGTH) + "...";
        }
        realBody = realBody.replace("\r\n", " ").replace("\n", " ");
        return realBody;
    }

    protected static String extractQueueName(MQXFMessageMoveRecord moveRecord) {
        return coalesce(moveRecord.getObjectName(), moveRecord.getResolvedQueueName(), moveRecord.getResolvedLocalQueueName());
    }

    protected static String extractQManagerName(MQXFMessageMoveRecord moveRecord) {
        return coalesce(moveRecord.getObjectQueueManagerName(), moveRecord.getResolvedQueueManagerName(), moveRecord.getResolvedLocalQueueManagerName());
    }

    protected static String coalesce(String... choice) {
        if (choice != null)
            for (String c : choice) {
                if (StringUtils.isNotBlank(c)) {
                    return c;
                }
            }
        return "";
    }


}
