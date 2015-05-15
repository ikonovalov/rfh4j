package ru.codeunited.wmq.messaging;

import com.ibm.mq.headers.MQRFH2;
import ru.codeunited.wmq.messaging.pcf.MQHeaderException;

import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 16.04.15.
 */
public final class HeaderUtilService {

    private HeaderUtilService() {

    }

    /**
     * Get field from RFH2 header in a specified folder.
     * Note: it replace strange behavior while element doesn't exists in a folder.
     * @param mqrfh2
     * @param folder
     * @param fieldName
     * @return
     */
    public static String getStringFromRHF2Folder(MQRFH2 mqrfh2, String folder, String fieldName) {
        try {
            return mqrfh2.getStringFieldValue(folder, fieldName);
        } catch(NoSuchElementException e) {
            throw new MQHeaderException(String.format("Element [%s] not found in folder [%s]", fieldName, folder));
        } catch (IOException e) {
            throw new MQHeaderException(e);
        }
    }

    /**
     * Same as <code>getStringFromRHF2Folder</code>, but if something goes wrong it returns empty string.
     * @param mqrfh2
     * @param folder
     * @param fieldName
     * @return
     */
    public static String getStringFromRHF2FolderSafe(MQRFH2 mqrfh2, String folder, String fieldName) {
        try {
            return mqrfh2.getStringFieldValue(folder, fieldName);
        } catch(Exception e) {
            return "";
        }
    }
}
