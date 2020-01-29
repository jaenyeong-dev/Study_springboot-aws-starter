#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh # 아래 IDLE_PROFILE=$(find_idle_profile)

REPOSITORY=/home/ec2-user/app/step3

PROJECT_NAME=springboot-aws-starter

echo "> Copy Build file"
echo "> cp $REPOSITORY/zip/*.jar $REPOSITORY/"

cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> Deploy New application"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name : $JAR_NAME"

echo "> Add execution permission on $JAR_NAME"

# Jar 파일 실행권한 부여
sudo chmod +x $JAR_NAME

echo "> Run $JAR_NAME"

IDLE_PROFILE=$(find_idle_profile)

echo "> Running $JAR_NAME as profile=$IDLE_PROFILE"
nohup java -jar \
    -Dspring.config.location=classpath:/application.yaml,classpath:/application-$IDLE_PROFILE.yaml,/home/ec2-user/app/application-oauth.yaml,/home/ec2-user/app/application-real-db.yaml \
    -Dspring.profiles.active=$IDLE_PROFILE \
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &

# 기본적으로 step2의 deploy.sh과 유사
# 차이점은 IDLE_PROFILE을 통해 설정 파일을 가져오고, active profile을 지정함

