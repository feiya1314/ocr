#! /bin/sh

###################### 说明 ########################
# 将打包好的前端文件，文件名dist.zip，放到/ home/ocr目录
# 执行该脚本
###################################################
ENV=$1
CHAT_HOME=/home/ocr/ai-chat
CHAT_WEB_HOME=/home/ocr/ai-chat/chat-web
CHAT_WEB_BAK=/home/ocr/ai-chat/chat-web-bak
if [ X$ENV = 'Xtest' ]; then
  CHAT_HOME=/home/ocr/ai-chat-test
  CHAT_WEB_HOME=/home/ocr/ai-chat-test/chat-web
  CHAT_WEB_BAK=/home/ocr/ai-chat-test/chat-web-bak
fi

echo CHAT_HOME $CHAT_HOME
echo CHAT_WEB_HOME $CHAT_WEB_HOME
echo CHAT_WEB_BAK $CHAT_WEB_BAK

if [ ! -d "$CHAT_WEB_HOME" ]; then
  echo "web dir not exist"
  mkdir $CHAT_WEB_HOME
fi

cd /home/ocr
if [ ! -f "dist.zip" ]; then
  echo "部署文件不存在"
  exit 0
fi

if [ -d "$CHAT_WEB_BAK" ]; then
  echo "web bak dir exist"
  rm -rf $CHAT_WEB_BAK
fi

echo "start deploy"

echo "clear dir"
mv $CHAT_WEB_HOME $CHAT_WEB_BAK

mkdir $CHAT_WEB_HOME

echo "move package"
mv dist.zip $CHAT_WEB_HOME/dist.zip

cd $CHAT_WEB_HOME

echo "unzip"
unzip -q dist.zip
rm dist.zip

cd /home/ocr
chown -R ocr:ocr $CHAT_WEB_HOME

echo "reboot nginx"
/usr/local/nginx/sbin/nginx -s reload

echo "finish"
