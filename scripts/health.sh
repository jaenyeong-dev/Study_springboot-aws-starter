#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh
source ${ABSDIR}/switch.sh

IDLE_PORT=$(find_idle_port)

echo "> Health Check Start!"
echo "> IDLE_PORT: $IDLE_PORT"
echo "> curl -s http://localhost:$IDLE_PORT/profile "
sleep 10

for RETRY_COUNT in {1..10}
do
  RESPONSE=$(curl -s http://localhost:${IDLE_PORT}/profile)
  UP_COUNT=$(echo ${RESPONSE} | grep 'real' | wc -l)

  if [ ${UP_COUNT} -ge 1 ]
  then
    # $up_count >= 1 ('real' 문자열이 있는지 검증)
    echo "> Success Health Check"
    switch_proxy
    break
  else
    echo "> Health check response is unknown or not running"
    echo "> Health check : $RESPONSE"
  fi

  if [ ${RETRY_COUNT} -eq 10 ]
  then
    echo "> Fail Health Check"
    echo "> Terminate deploy without not connecting to nginx"
    exit 1
  fi

  echo "> Health Check Connection fail. Retry.."
  sleep 10

done

# nginx와 연결되지 않은 포트로 스프링 부트가 잘 수행되었는지 확인
# 이상이 없는 경우 nginx 프록시 설정을 변경(switch_proxy)
# nginx 프록시 설정 변경은 switch.sh에서 실행
