DIR=$(cd "$(dirname "$0")" && pwd)/..

cd $DIR
PID_FILE=run/paas-unified.pid
if [ -f $PID_FILE ]; then
    echo "Instance running with pid" $(<$PID_FILE)
    exit 1
fi
nohup bin/run-paas-unified.sh &> logs/paas-unified.out &
echo $! > "$PID_FILE"
