#！ /bin/sh

CUR_PATH=$(cd "$(dirname "$0")";pwd)

echo "CUR_PATH:""$CUR_PATH"

ROOT_PATH=$(dirname "$CUR_PATH")
echo "rootPath:""$ROOT_PATH"

LOG_PATH=$ROOT_PATH/logs
echo "LOG_PATH:""$LOG_PATH"

source "$CUR_PATH"/java_opts.sh
echo "JAVA_OPTS:""$JAVA_OPTS"

pid=$(ps -ef | grep java | grep "open-ai-chat-service" | awk '{print $2}')

if [ "$pid" == "" ]; then
  echo "pid not exist"
  exit 0
fi

echo "pid : $pid"
kill $pid
if [ $? -ne 0 ]; then
  echo "force kill "
  kill -9 $pid
fi



