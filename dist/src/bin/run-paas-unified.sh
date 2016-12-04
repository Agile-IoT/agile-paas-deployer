#
# Usage: $0
#
# Run start-paas-unified-service.sh for daemonized service
#
#
DIR=$(cd "$(dirname "$0")/.." && pwd)
cd "$DIR"
. bin/env.sh
exec java -jar bin/unified-paas-service-0.0.1-SNAPSHOT.jar server etc/config.yml
