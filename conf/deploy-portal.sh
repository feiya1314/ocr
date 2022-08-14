#! /bin/sh

###################### 说明 ########################
# 将打包好的前端文件，文件名ocr-main-service*.tar.gz，放到/ home/ocr目录
# 执行该脚本
###################################################

if [ ! -d "/home/ocr/ocr-portal-service" ]; then
  echo "ocr-portal-service dir not exist"
  mkdir /home/ocr/ocr-portal-service
fi

deploy_file=$(ls ocr-main-service*.tar.gz)
if [ "$deploy_file" == "" ]; then
  echo "部署文件不存在"
  exit 1
fi

echo "start deploy portal service"
echo "deploy_file:$deploy_file"
echo "clear dir"

rm -rf /home/ocr/ocr-portal-service/*

echo "move package"
mv $deploy_file /home/ocr/ocr-portal-service/$deploy_file

cd /home/ocr/ocr-portal-service/

echo "unpack"
tar -xzvf $deploy_file
rm $deploy_file

chown -R ocr:ocr /home/ocr/ocr-portal-service/
chmod -R +x /home/ocr/ocr-portal-service/ocr-main-service

# 以ocr用户停止
su - ocr -c "sh /home/ocr/ocr-portal-service/ocr-main-service/bin/stop.sh"

# 以ocr用户启动
su - ocr -c "sh /home/ocr/ocr-portal-service/ocr-main-service/bin/start.sh"

echo "finish"
