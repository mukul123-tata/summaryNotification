#!/bin/sh

cd /home/pamapp-servicenow-dev/Project/SummaryNotification/summaryNotification/target/

export http_proxy=http://10.133.12.181:80

export https_proxy=https://10.133.12.181:80

nohup java -Djava.net.useSystemProxies=true  -jar  /home/pamapp-servicenow-dev/Project/SummaryNotification/summaryNotification/target/summaryNotifications-0.0.1-SNAPSHOT.jar &