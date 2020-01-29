# stop.sh가 속해 있는 경로 탐색, profile.sh 경로를 찾기 위해 사용됨
ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)

# 자바의 import 구문과 유사, 이로 인해 profile.sh의 함수들을 사용할 수 있게 됨
source ${ABSDIR}/profile.sh

IDLE_PORT=$(find_idle_port)

echo "> Check Running Process PID in $IDLE_PORT"
IDLE_PID=$(lsof -ti tcp:${IDLE_PORT})

if [ -z ${IDLE_PID} ]
then
  echo "> It will not be terminated to isn't running application"
else
  echo "> kill -15 $IDLE_PID"
  kill -15 ${IDLE_PID}
  sleep 5
fi
