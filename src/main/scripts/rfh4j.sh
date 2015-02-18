#!/bin/sh
unset CLASSPATH
unset WMQ_LIB
unset PLATFORM_LIB

WMQ_LIB=/opt/mqm/java/lib
PLATFORM_LIB=/opt/mqm/java/lib64 # for x84_64
CLASSPATH=./rfh4j-1.2-SNAPSHOT.jar:${WMQ_LIB}/com.ibm.mq.commonservices.jar:${WMQ_LIB}/com.ibm.mq.headers.jar:${WMQ_LIB}/com.ibm.mq.jar:${WMQ_LIB}/com.ibm.mq.jmqi.jar:${WMQ_LIB}/connector.jar

java -Djava.library.path=${PLATFORM_LIB} -classpath ${CLASSPATH} ru.codeunited.wmq.RFH4J "$@"