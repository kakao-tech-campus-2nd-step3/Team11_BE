#!/bin/bash

# JAR 파일 경로
JAR_PATH="/home/ubuntu/my-spring-app/libs/boomerang-0.0.1-SNAPSHOT.jar"

# 애플리케이션 종료 (기존 프로세스 종료)
pkill -f $JAR_PATH

# 애플리케이션 실행
nohup java -jar $JAR_PATH > /home/ubuntu/my-spring-app/log.txt 2>&1 &
