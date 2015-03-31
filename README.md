rfh4j
=====
Try to make something like RFHUtil with Java and Blackjack.

__Examples:__

* ./rfh4j.sh --config default.properties --dstq Q.APPLICATION_OUT -p ~/myfile.xml | awk '{print $4}'
* ./rfh4j.sh -Q DEFQM -c JVM.DEF.SVRCONN --dstq Q.APPLICATION_OUT -t someText
* ./rfh4j.sh --config default.properties --dstq Q1 -s < ~/developer/projects/MQCluster/src/spring.xml
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