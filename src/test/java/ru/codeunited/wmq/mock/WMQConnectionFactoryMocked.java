package ru.codeunited.wmq.mock;


import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import ru.codeunited.wmq.messaging.*;

import java.io.IOException;
import java.util.Properties;

import static org.mockito.Mockito.*;
/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 26.10.14.
 */
@SuppressWarnings("UnusedParameters")
public class WMQConnectionFactoryMocked implements WMQConnectionFactory {

    public MQQueueManager connectQueueManager(String queueManagerName, Properties properties) {
        MQQueueManager manager = mock(MQQueueManager.class);

        // return valid mqm name
        try {
            when(manager.getName()).thenReturn(queueManagerName + "-mocked");
        } catch (MQException e) {
            throw new RuntimeException(e);
        }

        // first - for check "is connected", second - for "is disconnected"
        when(manager.isConnected()).thenReturn(true, false);

        try {
            when(manager.getDescription()).thenReturn("Mocked manager");
        } catch (MQException e) {
            e.printStackTrace();
        }

        return manager;
    }

    @Override
    public MQLink connectQueueManager(final ConnectionOptions connectionOptions) throws MQException {
        return new MQLink() {
            @Override
            public ConnectionOptions getOptions() {
                ConnectionOptions options = mock(ConnectionOptions.class);
                when(options.getQueueManagerName()).thenReturn("DEFQM");
                return options;
            }

            @Override
            public QueueManager getManager() {
                return new QueueManager() {

                    MQQueueManager manager;

                    {
                        manager = connectQueueManager(
                                connectionOptions.getQueueManagerName(),
                                connectionOptions.getOptions()
                        );
                    }

                    @Override
                    public MQQueueManager get() {

                        return manager;
                    }

                    @Override
                    public boolean isConnected() {
                        return manager.isConnected();
                    }

                    @Override
                    public String getDLQName() {
                        return null;
                    }

                    @Override
                    public String getDescription() {
                        return null;
                    }

                    @Override
                    public String getIdentefier() {
                        return null;
                    }

                    @Override
                    public String getName() {
                        return null;
                    }

                    @Override
                    public QueueManagerAttributes getAttributes() {
                        return null;
                    }

                    @Override
                    public void close() throws IOException {
                        try {
                            manager.disconnect();
                        } catch (MQException e) {
                            throw new IOException(e);
                        }
                    }
                };
            }

            @Override
            public boolean isConnected() {
                return false;
            }

            @Override
            public void close() throws IOException {

            }
        };
    }
}
