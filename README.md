rfh4j
=====
Try to make something like RFHUtil with Java and Blackjack.

__Examples:__

* ./rfh4j.sh --config default.properties --dstq Q.APPLICATION_OUT -p ~/myfile.xml | awk '{print $4}'
* ./rfh4j.sh -Q DEFQM -c JVM.DEF.SVRCONN --dstq Q.APPLICATION_OUT -t someText

<pre><code>
usage: rfh4j [-c <channel>] [--config <config_file>] [--dstq <queue>] [-H <hostname or IP>] -h | -Q <qmanager> [-P <port>] [-p <file> | -t <text>]   [-u <user>]
Option description
     -c,--channel <channel>         WMQ SVRCON channel name
        --config <config_file>      Configuration file for WMQ connection (use it like c,H,P,Q,u)
        --dstq <queue>              Destination queue
     -H,--host <hostname or IP>     WMQ QM host name or ip address. localhost is default
     -h,--help                      Help information
     -P,--host <port>               WMQ QM listener port. 1414 is default.
     -p,--payload <file>            file to send
     -Q,--qmanager <qmanager>       WMQ queue manager name
     -t,--text <text>               text for message
     -u,--user <user>               WMQ QM user.
Usage examples:
1) Send text message to queue (host, port, channel are default)
rfh4j.sh -Q DEFQM --dstq Q1 -t hello!
</code></pre>