#!/bin/bash

REPOSITORY=/home/ec2-user/app/step2
PROJECT_NAME=springboot-aws-starter

echo "> Copy Build file"

cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> Check Processing PID NOW"

# 현재 실행중인 PID 찾기
CURRENT_PID=$(pgrep -fl $PROJECT_NAME | grep jar | awk '{print $1}')

echo " Current Processing PID : $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
    echo "> It will not be terminated to isn't running application"
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "> Deploy New application"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name : $JAR_NAME"

echo "> Add execution permission on $JAR_NAME"

# Jar 파일 실행권한 부여
sudo chmod +x $JAR_NAME

echo "> Run $JAR_NAME"

# $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
# nohup 실행시 CodeDeploy는 무한 대기, 이 이슈를 위해 nohup.out 파일을 표준 입출력으로 별도 사용
# 이렇게 하지 않을 경우 nohup.out 파일이 생성되지 않고, CodeDeploy 로그에 표준 입출력 됨
# nohup이 끝나기 전까지 CodeDeploy도 끝나지 않으니, 꼭 이렇게 해야함
nohup java -jar \
    -Dspring.config.location=classpath:/application.yaml,classpath:/application-real.yaml,/home/ec2-user/app/application-oauth.yaml,/home/ec2-user/app/application-real-db.yaml \
    -Dspring.profiles.active=real \
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
