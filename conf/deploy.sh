#! /bin/sh

if [ ! -d "/home/ocr/ocr-web" ]; then
  echo "web dir not exist"
  mkdir /home/ocr/ocr-web
fi

if [ ! -f "dist.zip" ]; then
  echo "部署文件不存在"
  exit 0
fi

echo "start deploy"

echo "clear dir"
rm -rf /home/ocr/ocr-web/*

echo "move package"
mv dist.zip /home/ocr/ocr-web/dist.zip

cd /home/ocr/ocr-web/

echo "unzip"
unzip -q dist.zip
rm dist.zip

cd /home/ocr
chown -R ocr:ocr /home/ocr/ocr-web
echo "finish"
