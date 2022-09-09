#! /bin/sh


cd /home/ocr/ocr-service

nohup python Bootstrap.py &


cd /home/ocr/ocr-portal-service/ocr-main-service/bin

sleep 30
sh start.sh



