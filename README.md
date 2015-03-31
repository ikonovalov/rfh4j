rfh4j
=====
Try to make something like RFHUtil with Java and Blackjack.

__Examples:__

* Connection configuration:
    * Use default.properties: `./rfh4j.sh` (nothing else, it used by default)
    * Specify non default config: `./rfh4j.sh --config myconn.properties`
    * Use CLI: `./rfh4j.sh --hostname=mqhome.mydomain.org --port=1414 -Q DEFQM --userID=mqgod --password=123 -c JVM.DEF.SVRCONN`
    * Combination: `./rfh4j.sh --config myconn.properties --hostname=mqhome.mydomain.org -c JVM.DEF.SVRCONN`
    * Connect in binding mode: `--transport=binding`
    * Connect in client mode (default): `--transport=client`
     
* Get messages
    * Get(--srcq) and print to console(--stream) body to console `--srcq RFH.QTEST.QGENERAL1 --stream` 
    * Get, print with wait (--wait milsec) `--srcq RFH.QTEST.QGENERAL1 --stream --wait 5000`
    * Get, wait and save to file(--payload) (name is msgID) `--srcq RFH.QTEST.QGENERAL1 --wait 5000 --payload /tmp/`
    
* Put message
    * Put (--dstq) text (--text) message: `--dstq Q.APPLICATION_OUT --text someText`
    * Put file (-p, --payload) as payload: `--dstq Q.APPLICATION_OUT -p ~/myfile.xml`
    * Put message as redirected stream(-s, --stream) `--config default.properties --dstq Q.APPLICATION_OUT -s < ~/developer/projects/MQCluster/src/spring.xml`
    * Put file as payload 100 times (--times): `--dstq Q.APPLICATION_OUT -p ~/myfile.xml --times 100`
    
* Activity log (MQ online monitoring)
    * Listen with redirect stream: `--srcq SYSTEM.ADMIN.TRACE.ACTIVITY.QUEUE --stream --limit -1 > /tmp/online.log`
    * Get 50 records with spec. formatter: `--srcq SYSTEM.ADMIN.TRACE.ACTIVITY.QUEUE --stream --limit 50 --formatter=ru.codeunited.wmq.format.MQFTMAdminCommonFormatter`

* Inquiry
    * Get queues status without filter (--lslq)`./rfh4j.sh --lslq`
    * Get queues status with filter (--lslq *wildcard*)`./rfh4j.sh --lslq TESTQ.*`




<pre><code>
usage: rfh4j [--all] [-c <channel>] [--config <config_file>] [--dstq <queue>] [--formatter <arg>] [-h] [-H <hostname or IP>] [--handler <handler.class> | -p <file> | -s | -t <text>] [--limit <arg>] [--lslq <pattern>] [-P <port>]  [--password <user password>] [-Q <queue manager>]  [--srcq <queue>]
       [--times <arg>] [--transport <binding|client>] [-u <user id>] [-v <arg>] [-w <milliseconds>]
Option description
        --all                            Applicable to GET command
     -c,--channel <channel>              WMQ SVRCON channel name
        --config <config_file>           Configuration file for WMQ connection (use it like c,H,P,Q,u)
        --dstq <queue>                   Destination queue
        --formatter <arg>                Special formatter class name
     -h,--help                           Help information
     -H,--hostname <hostname or IP>      WMQ QM host name or ip address (localhost is default).
        --handler <handler.class>
        --limit <arg>                    Limit GET command. This is maximum messages or use negative value for infinity mode
        --lslq <pattern>                 List localqueues with filter. Default value is * (means all).
     -P,--port <port>                    WMQ QM listener port (1414 is default).
     -p,--payload <file>                 File to send.
        --password <user password>       WMQ QM user password.
     -Q,--qmanager <queue manager>       WMQ queue manager name
     -s,--stream                         Stream for message (std in/out).
        --srcq <queue>                   Source queue
     -t,--text <text>                    Text for message.
        --times <arg>                    Repeat count. Supported for MQPUT now.
        --transport <binding|client>     WMQ transport type
     -u,--userID <user id>               WMQ QM user id.
     -v,--verbose <arg>                  Print additional output
     -w,--wait <milliseconds>            Wait specified amount of time.
Usage examples:
1) Send text message to a queue (host, port, channel are default)
rfh4j.sh --dstq RFH.QTEST.QGENERAL1 -t hello!
2) Get message from a queue and print to console
rfh4j.sh --srcq RFH.QTEST.QGENERAL1 --stream
3) Get message from a queue with timeout and put to file with default name
rfh4j.sh --srcq RFH.QTEST.QGENERAL1 --wait 5000 --payload /tmp/
4) List all local queues
rfh4j.sh  --lslq
5) List all local queues with filter
rfh4j.sh --lslq MYQ*
</code></pre>