#!/usr/bin/env bash
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

# Usage: $0
#
# Run start-paas-unified-service.sh for daemonized service
#
#
DIR=$(cd "$(dirname "$0")/.." && pwd)
cd "$DIR"
. etc/env.sh
exec java -jar bin/unified-paas-service.jar server etc/config.yml
