#！ /bin/sh
echo "rootPath:""$ROOT_PATH"
echo "CUR_PATH:""$CUR_PATH"
echo "LOG_PATH:""$LOG_PATH"

JAVA_OPTS=""
JAVA_OPTS="$JAVA_OPTS -Xmx256m"
# gc
JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC -XX:+PrintReferenceGC -XX:+PrintGCApplicationStoppedTime"
JAVA_OPTS="$JAVA_OPTS -Xloggc:$LOG_PATH/gc-%t.log -XX:+UseGCLogFileRotation  -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=10M"

#JAVA_OPTS="$JAVA_OPTS -Xmx3550m -Xms3550m -Xmn2g -Xss128k"
export JAVA_OPTS
