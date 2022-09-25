#! /bin/sh


cd /home/ocr/ocr-service

nohup python Bootstrap.py >> /home/ocr/ocr-service/logs/service.log 2>&1 &


cd /home/ocr/ocr-portal-service/ocr-main-service/bin

sleep 30
sh start.sh



