#！ /bin/sh
echo "rootPath:""$ROOT_PATH"
echo "CUR_PATH:""$CUR_PATH"
echo "LOG_PATH:""$LOG_PATH"

JAVA_OPTS=""
JAVA_OPTS="$JAVA_OPTS -Xmx256m"
# 系统可用内存百分比配置堆，堆内存一般占用系统内存的少一半，其他的给堆外内存使用，比如 GC，元空间，Tracing 等等，
# 同时设置 MinHeapFreeRatio 为最小值，MaxHeapFreeRatio 为最大值是为了避免 GC 的时候动态伸缩堆大小
# JAVA_OPTS="$JAVA_OPTS -XX:MaxRAMPercentage=45 -XX:InitialRAMPercentage=45 -XX:MinHeapFreeRatio=0 -XX:MaxHeapFreeRatio=100"
# gc
# JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCDetails -XX:+PrintReferenceGC -XX:+PrintGCApplicationStoppedTime"
# JAVA_OPTS="$JAVA_OPTS -Xloggc:$LOG_PATH/gc-%t.log -XX:+UseGCLogFileRotation  -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=10M"

JAVA_OPTS="$JAVA_OPTS -Xlog:gc*:file=$LOG_PATH/gc-%t.log:time,uptime:filecount=10,filesize=10M"

#JAVA_OPTS="$JAVA_OPTS -Xmx3550m -Xms3550m -Xmn2g -Xss128k"
export JAVA_OPTS
