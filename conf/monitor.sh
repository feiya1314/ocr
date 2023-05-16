#! /bin/sh

#crontab -e 每5分钟 */5 * * * *
# * * * * * /home/ocr/monitor.sh >> /home/ocr/logs/cron.log 2>&1
cd /home/ocr

if [ ! -d "logs" ]; then
  echo "home logs dir not exist"
  mkdir logs
fi

#nowTime=`date`
#echo "$nowTime crontab start" >> ~/logs/cron.log

pyOcrStartPath="/home/ocr/ocr_python_start.sh"
portalStartPath="/home/ocr/ocr-portal-service/ocr-main-service/bin/start.sh"

pyOcr=$(ps -ef | grep python | grep "Bootstrap.py")

nowTime=`date`
if [ "$pyOcr" == "" ]; then
  echo "$nowTime --- python ocr service not running,restart"
  echo "$nowTime --- python ocr service not running,restart" >> ~/logs/monitor.log
  echo "resatrt python ocr service" >> ~/logs/monitor.log

  sh $pyOcrStartPath
fi



portal=$(ps -ef | grep java | grep "ocr-main-service" | awk '{print $2}')

nowTime=`date`
if [ "$portal" == "" ]; then
  echo "$nowTime --- portal service not running,restart"
  echo "$nowTime --- portal service not running,restart" >> ~/logs/monitor.log
  echo "resatrt portal" >> ~/logs/monitor.log
  sleep 10
  sh $portalStartPath
fi