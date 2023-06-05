#! /bin/sh

###################### 说明 ########################
# 将打包好的前端文件，文件名dist.zip，放到/ home/ocr目录
# 执行该脚本
###################################################
mkdir /home/ocr/nginx
if [ ! -d "/home/ocr/ai-chat" ]; then
  echo "web dir not exist"
  mkdir /home/ocr/ai-chat
fi

if [ ! -d "/home/ocr/ai-chat/chat-web" ]; then
  echo "web dir not exist"
  mkdir /home/ocr/ai-chat/chat-web
fi

if [ ! -f "dist.zip" ]; then
  echo "部署文件不存在"
  exit 0
fi

if [ -d "/home/ocr/ocr-web-bak" ]; then
  echo "web bak dir exist"
  rm -rf /home/ocr/ocr-web-bak
fi

echo "start deploy"

echo "clear dir"
mv /home/ocr/ai-chat/chat-web /home/ocr/ai-chat/chat-web-bak

mkdir /home/ocr/ai-chat/chat-web

echo "move package"
mv dist.zip /home/ocr/ocr-web/dist.zip

cd /home/ocr/ocr-web/

echo "unzip"
unzip -q dist.zip
rm dist.zip

cd /home/ocr
chown -R ocr:ocr /home/ocr/ocr-web

mkdir /home/ocr/nginx
chown -R ocr:ocr /home/ocr/nginx

echo "reboot nginx"
systemctl stop nginx
systemctl start nginx

echo "finish"
