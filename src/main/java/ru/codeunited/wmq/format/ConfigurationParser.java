package ru.codeunited.wmq.format;

import ru.codeunited.wmq.messaging.pcf.MQXFOperation;

import java.io.InputStream;
import java.util.Map;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 22.05.15.
 */
public interface ConfigurationParser<T> {

    class Configuration {

        Map<MQXFOperation, OperationConfig> operationMap;

        public OperationConfig getOperationConfig(MQXFOperation operation) {
            return operationMap.get(operation);
        }

    }

    class OperationConfig {

        private final MQXFOperation operation;

        public OperationConfig(MQXFOperation operation) {
            this.operation = operation;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            OperationConfig operation1 = (OperationConfig) o;

            return operation == operation1.operation;

        }

        @Override
        public int hashCode() {
            return operation.hashCode();
        }
    }

    void load(String str);

    void load(InputStream stream);

    T getRoot();
}
