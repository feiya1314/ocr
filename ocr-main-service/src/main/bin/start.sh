#！ /bin/sh

# 脚本真实全路径
CUR_PATH=$(cd "$(dirname "$0")";pwd)

echo "CUR_PATH:""$CUR_PATH"

ROOT_PATH=$(dirname "$CUR_PATH")
echo "rootPath:""$ROOT_PATH"

LOG_PATH=$ROOT_PATH/logs
echo "LOG_PATH:""$LOG_PATH"

source "$CUR_PATH"/java_opts.sh
echo "JAVA_OPTS:""$JAVA_OPTS"

cd $ROOT_PATH
nohup java -jar "$JAVA_OPTS" ocr-main-service-1.0-SNAPSHOT.jar &



