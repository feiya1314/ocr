#！ /bin/sh

# 脚本真实全路径
CUR_PATH=$(cd "$(dirname "$0")";pwd)

#echo "CUR_PATH:""$CUR_PATH"

ROOT_PATH=$(dirname "$CUR_PATH")
#echo "rootPath:""$ROOT_PATH"

LOG_PATH=$ROOT_PATH/logs
#echo "LOG_PATH:""$LOG_PATH"

source "$CUR_PATH"/java_opts.sh
#echo "JAVA_OPTS:""$JAVA_OPTS"

if [ ! -d "$LOG_PATH" ]; then
  mkdir $LOG_PATH
fi

echo "CUR_PATH:""$CUR_PATH" > $LOG_PATH/nohup.log
echo "rootPath:""$ROOT_PATH" >> $LOG_PATH/nohup.log
echo "LOG_PATH:""$LOG_PATH" >> $LOG_PATH/nohup.log
echo "JAVA_OPTS:""$JAVA_OPTS" >> $LOG_PATH/nohup.log

cd $ROOT_PATH
nohup java -jar $JAVA_OPTS $ROOT_PATH/ocr-main-service-1.0-SNAPSHOT.jar >> $LOG_PATH/nohup.log 2>&1 &



