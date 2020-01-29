#!/usr/bin/env bash

# 쉬고 있는 profile 찾기
function find_idle_profile() {
    # 현재 nginx가 바라보고 있는 스프링 부트가 정상적으로 수행 중인지 확인
    # 응답값은 HttpStatus로 수신 (정상 200, 에러는 400~503 이므로 400 이상은 real2를 현재 profile로 사용)
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)

    if [ ${RESPONSE_CODE} -ge 400 ]
    # IDLE_PROFILE은 nginx와 연결되지 않은 profile
    # 스프링 부트를 이 profile로 연결하기 위해 반환
    then
      CURRENT_PROFILE=real2
    else
      CURRENT_PROFILE=$(curl -s http://localhost/profile)
    fi

    if [ ${CURRENT_PROFILE} == real1 ]
    then
      IDLE_PROFILE=real2
    else
      IDLE_PROFILE=real1
    fi

    # bash는 스크립트 값을 반환하는 기능이 없음
    # 따라서 echo로 값을 출력후 클라이언트에서 출력 값을 잡아 ($(find_idle_profile)) 사용, 중간에 echo를 사용하면 안됨
    echo "${IDLE_PROFILE}"
}

# 쉬고 있는 profile의 port 찾기
function find_idle_port() {
    IDLE_PROFILE=$(find_idle_profile)

    if [ ${IDLE_PROFILE} == real1 ]
    then
      echo "8081"
    else
      echo "8082"
    fi
}
