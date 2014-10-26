#!/bin/sh
unset CLASSPATH
WMQ_LIB=/opt/mqm/java/lib
CLASSPATH=./rfh4j-1.0-SNAPSHOT.jar:${WMQ_LIB}/com.ibm.mq.commonservices.jar:${WMQ_LIB}/com.ibm.mq.headers.jar:${WMQ_LIB}/com.ibm.mq.jar:${WMQ_LIB}/com.ibm.mq.jmqi.jar:${WMQ_LIB}/connector.jar
#echo $CLASSPATH
#echo $WMQ_LIB
java -classpath ${CLASSPATH} ru.codeunited.wmq.RFH4J "$@"

# THIS IS WORKS
#java -classpath /opt/mqm/java/lib/com.ibm.mq.commonservices.jar:/opt/mqm/java/lib/com.ibm.mq.headers.jar:/opt/mqm/java/lib/com.ibm.mq.jar:/opt/mqm/java/lib/com.ibm.mq.jmqi.jar:/opt/mqm/java/lib/connector.jar:./rfh4j-1.0-SNAPSHOT.jar ru.codeunited.wmq.RFH4J