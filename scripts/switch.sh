#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

function switch_proxy() {
    IDLE_PORT=$(find_idle_port)

    echo "> Port to swtich : $IDLE_PORT"
    echo "> Switch Port"
    # 하나의 문장, 파이프 라인으로 넘겨주기 위해 echo 사용
    # nginx가 변경할 프록시 주소 생성
    # 해당 echo 출력은 ' (single quotation)을 사용할것
    # sudo tee 명령은 앞에서 넘겨준 문장을 service-url.inc에 덮어씀
    # 아래 echo 명령어 앞에 > 를 붙여 nginx 에러 발생, 그로 인해 서버가 안됨
    echo '> Port : $IDLE_PORT'
    echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc

    echo "> Reload nginx"
    # nginx restart는 끊김 현상이 있지만 reload는 끊김 없이 다시 불러옴
    # 하지만 중요한 설정은 반영되지 않기 때문에 restart를 사용해야 함
    # 지금 경우는 외부 설정 파일인 service-url을 리로딩 하는 것이기에 reload로 가능
    sudo service nginx reload
}
