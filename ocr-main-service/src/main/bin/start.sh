#！ /bin/sh

CUR_PATH=$(dirname "$0")
echo "CUR_PATH:""$CUR_PATH"

ROOT_PATH=$(dirname "$CUR_PATH")
echo "rootPath:""$ROOT_PATH"

LOG_PATH=$ROOT_PATH/logs
echo "LOG_PATH:""$LOG_PATH"

source "$CUR_PATH"/java_opts.sh
echo "JAVA_OPTS:""$JAVA_OPTS"

cd $ROOT_PATH
nohup java -jar ocr-main-service-1.0-SNAPSHOT.jar &



