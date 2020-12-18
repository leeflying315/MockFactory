#!/bin/bash
#cd ..
base_dir=$(cd `dirname $0`;cd ..; pwd)
files=$(ls $base_dir)
for filename in $files; do
  if [ ${filename##*.} == jar ]; then
    JAR=${filename}
  fi
done
sh ./bin/stop.sh

if [ ! -d "${base_dir}/logs" ]; then
  mkdir ${base_dir}/logs
fi

JAVA_OPTS="-Xms1G -Xmx1G -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -Dlogging.config=${base_dir}/conf/log4j2.xml -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.Log4j2LogDelegateFactory"
GC_OPTS="-Xloggc:${base_dir}/logs/gc.log -XX:+PrintGCTimeStamps -XX:+PrintGCDetails"
LOG_OPTS="-Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.Log4j2LogDelegateFactory"
#调试日志模式
#nohup java ${JAVA_OPTS} ${GC_OPTS}  ${LOG_OPTS} -Dbase.dir=${base_dir} -jar ${JAR} >>start.log 2>&1 &
nohup java ${JAVA_OPTS} ${GC_OPTS} ${LOG_OPTS}  -Dbase.dir=${base_dir} -jar ${JAR} >>/dev/null 2>&1 &

pid=$(ps -ef | grep ${JAR} | grep -v grep | awk '{print $2}')
if [ "$pid" ]; then
  echo ${JAR} is running pid=$pid start success
else
  echo ${JAR} start failed
  exit
fi
