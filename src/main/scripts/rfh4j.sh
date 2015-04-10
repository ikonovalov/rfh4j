#!/bin/bash

unset CLASSPATH
unset WMQ_LIB
unset PLATFORM_LIB

APP_VERSION=1.5-SNAPSHOT
WMQ_LIB=/opt/mqm/java/lib
PLATFORM_LIB=/opt/mqm/java/lib64 # for x84_64
CLASSPATH=./rfh4j-${APP_VERSION}.jar:${WMQ_LIB}/com.ibm.mq.commonservices.jar:${WMQ_LIB}/com.ibm.mq.headers.jar:${WMQ_LIB}/com.ibm.mq.jar:${WMQ_LIB}/com.ibm.mq.jmqi.jar:${WMQ_LIB}/connector.jar
WHICH_JAVA=`which java`

if [ -z "$JAVA_HOME" ]; then
    JAVA="$WHICH_JAVA"

elif [ -x "$JAVA_HOME/jre/bin/java" ]; then
    JAVA="$JAVA_HOME/jre/bin/java"

elif [ -x "$JAVA_HOME/bin/java" ]; then
    JAVA="$JAVA_HOME/bin/java"

fi

${JAVA} -Djava.library.path=${PLATFORM_LIB} -classpath ${CLASSPATH} ru.codeunited.wmq.RFH4J "$@"