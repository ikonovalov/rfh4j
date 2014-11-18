rfh4j
=====
Try to make something like RFHUtil with Java and Blackjack.

__Examples:__

* ./rfh4j.sh --config default.properties --dstq Q.APPLICATION_OUT -p ~/myfile.xml | awk '{print $4}'
* ./rfh4j.sh -Q DEFQM -c JVM.DEF.SVRCONN --dstq Q.APPLICATION_OUT -t someText
* ./rfh4j.sh --config default.properties --dstq Q1 -s < ~/developer/projects/MQCluster/src/spring.xml
<pre><code>
usage: rfh4j [-c <channel>] [--config <config_file>] [--dstq <queue>] [-H <hostname or IP>] [-h] [-P <port>] [-p <file> | -s | -t <text>] [-Q <qmanager>]  [--srcq <queue>]  [-u <user>] [-w <milliseconds>]
Option description
     -c,--channel <channel>         WMQ SVRCON channel name
        --config <config_file>      Configuration file for WMQ connection (use it like c,H,P,Q,u)
        --dstq <queue>              Destination queue
     -H,--host <hostname or IP>     WMQ QM host name or ip address (localhost is default).
     -h,--help                      Help information
     -P,--port <port>               WMQ QM listener port (1414 is default).
     -p,--payload <file>            File to send.
     -Q,--qmanager <qmanager>       WMQ queue manager name
     -s,--stream                    Stream for message (std in/out).
        --srcq <queue>              Source queue
     -t,--text <text>               Text for message.
     -u,--user <user>               WMQ QM user.
     -w,--wait <milliseconds>       Wait specified amount of time.
Usage examples:
1) Send text message to queue (host, port, channel are default)
rfh4j.sh -Q DEFQM --dstq RFH.QTEST.QGENERAL1 -t hello!
</code></pre>