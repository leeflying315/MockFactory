#!/bin/bash
base_dir=$(cd $(dirname $0);cd ..; pwd)
files=$(ls $base_dir)
for filename in $files; do
  if [ ${filename##*.} == jar ]; then
    JAR=${filename}
  fi
done

pid=`ps -ef | grep ${JAR} | grep -v grep |awk '{print $2}'`
if [ "$pid" ]; then
    echo  ${JAR}  is  running pid=$pid start to kill
    kill -9 $pid
   else
         echo ${JAR} not started, no need to kill
   exit
fi
pid=`ps -ef | grep ${JAR} | grep -v grep |awk '{print $2}'`
if [ "$pid" ]; then
    echo kill failed, please check
   else
     echo kill successed
fi
