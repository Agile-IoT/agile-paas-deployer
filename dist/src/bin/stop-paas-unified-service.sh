DIR=$(cd "$(dirname "$0")" && pwd)/..

cd $DIR
PID_FILE=run/paas-unified.pid
kill $(<$PID_FILE)
rm "$PID_FILE"
