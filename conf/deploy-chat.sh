#! /bin/sh

###################### 说明 ########################
# 将打包好的后端服务，放到/ home/ocr目录
# 执行该脚本
###################################################
ENV=$1
deploy_file='easy-chat-service-bin.tar.gz'
chat_dir=/home/ocr/ai-chat
service_dir=/home/ocr/ai-chat/easy-chat-service
service_bak_dir=/home/ocr/ai-chat/easy-chat-service-bak

if [ X$ENV = 'Xtest' ]; then
  chat_dir=/home/ocr/ai-chat-test
  service_dir=/home/ocr/ai-chat-test/easy-chat-service
  service_bak_dir=/home/ocr/ai-chat-test/easy-chat-service-bak
fi

echo chat_dir $chat_dir
echo service_dir $service_dir
echo service_bak_dir $service_bak_dir

if [ ! -d "$chat_dir" ]; then
  echo "web dir not exist"
  mkdir $chat_dir
fi

if [ ! -f "$deploy_file" ]; then
  echo "部署文件不存在"
  exit 0
fi

if [ -d "$service_bak_dir" ]; then
  echo "web bak dir exist"
  rm -rf $service_bak_dir
fi

echo "start deploy"

echo "bak dir"
sh $service_dir/bin/stop.sh
mv $service_dir $service_bak_dir

echo "move package"
mv $deploy_file $chat_dir

cd $chat_dir
tar -zxvf $deploy_file

sh $service_dir/bin/start.sh
rm $deploy_file
