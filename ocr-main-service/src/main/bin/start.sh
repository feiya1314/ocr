#！ /bin/sh

CUR_PATH=$(pwd)
echo "CUR_PATH:""$CUR_PATH"

ROOT_PATH=$(dirname "$CUR_PATH")
echo "rootPath:""$ROOT_PATH"

LOG_PATH=$ROOT_PATH/logs
echo "LOG_PATH:""$LOG_PATH"

source ./java_opts.sh
echo "JAVA_OPTS:""$JAVA_OPTS"

nohup java -jar "$CUR_PATH"/ocr-main-service-1.0-SNAPSHOT.jar &



