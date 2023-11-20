#!/bin/bash

. scripts/utils.sh
. scripts/setEnv.sh

# setGlobals $1
shift

set -x
fcn_name=$1
shift
args=($@)
args=$(printf '%s ' "${args[@]}")
args=`echo "$args"`
../application-gateway-go/assetTransfer ${fcn_name} ${args}
{ set +x; } 2>/dev/null
