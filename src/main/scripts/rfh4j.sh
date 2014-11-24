#!/bin/sh
unset CLASSPATH
WMQ_LIB=/opt/mqm/java/lib
CLASSPATH=./rfh4j-1.2-SNAPSHOT.jar:${WMQ_LIB}/com.ibm.mq.commonservices.jar:${WMQ_LIB}/com.ibm.mq.headers.jar:${WMQ_LIB}/com.ibm.mq.jar:${WMQ_LIB}/com.ibm.mq.jmqi.jar:${WMQ_LIB}/connector.jar
#echo $CLASSPATH
#echo $WMQ_LIB
java -classpath ${CLASSPATH} ru.codeunited.wmq.RFH4J "$@"