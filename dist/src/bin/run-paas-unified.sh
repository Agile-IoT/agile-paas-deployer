#!/usr/bin/env bash
# Usage: $0
#
# Run start-paas-unified-service.sh for daemonized service
#
#
DIR=$(cd "$(dirname "$0")/.." && pwd)
cd "$DIR"
. etc/env.sh
exec java -jar bin/unified-paas-service.jar server etc/config.yml
