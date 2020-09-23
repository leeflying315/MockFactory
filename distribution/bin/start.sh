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

if [ -n "$1" ]
then
    profile=$1
else
    echo no profile input, use dev as default
    profile=dev
fi

JAVA_OPTS="-Xms4G -Xmx4G -XX:+UseG1GC -XX:MaxGCPauseMillis=200 "
GC_OPTS="-Xloggc:${base_dir}/log/gc.log -XX:+PrintGCTimeStamps -XX:+PrintGCDetails"

#调试日志模式
#nohup java ${JAVA_OPTS} ${GC_OPTS} -Dspring.profiles.active=$profile -Dlogging.config=${base_dir}/conf/logback-spring.xml -Dbase.dir=${base_dir} -jar ${JAR} >>start.log 2>&1 &
nohup java ${JAVA_OPTS} ${GC_OPTS} -Dspring.profiles.active=$profile -Dlogging.config=${base_dir}/conf/logback-spring.xml -Dbase.dir=${base_dir} -jar ${JAR} >>/dev/null 2>&1 &

pid=$(ps -ef | grep ${JAR} | grep -v grep | awk '{print $2}')
if [ $pid ]; then
  echo ${JAR} is running pid=$pid start success
else
  echo ${JAR} start failed
  exit
fi