#
#  Copyright (c) 2018 Atos, and others
#  All rights reserved. This program and the accompanying materials
#  are made available under the terms of the Eclipse Public License 2.0
#  which accompanies this distribution, and is available at
#  https://www.eclipse.org/legal/epl-2.0/
#
#  Contributors:
#  Atos - initial implementation
#

DIR=$(cd "$(dirname "$0")" && pwd)/..

cd $DIR
PID_FILE=run/paas-unified.pid
if [ -f $PID_FILE ]; then
    echo "Instance running with pid" $(<$PID_FILE)
    exit 1
fi
nohup bin/run-paas-unified.sh &> logs/paas-unified.out &
echo $! > "$PID_FILE"
