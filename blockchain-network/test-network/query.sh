#!/bin/bash

. scripts/utils.sh
. scripts/setEnv.sh

setGlobals $1
shift

# TODO: Get arguments and make them comma-separated string

set -x
fcn_call='{"Args":["'$1'"]}'
peer chaincode query -C mychannel -n basic -c ${fcn_call}
res=$?
{ set +x; } 2>/dev/null
