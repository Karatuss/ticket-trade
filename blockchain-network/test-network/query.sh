#!/bin/bash

. scripts/utils.sh
. scripts/setEnv.sh

if [ $1 -ne 1 ] && [ $1 -ne 2 ]; then
  errorln "The first arg should be 1(Seller;Manager) or 2(Buyer;Member)!" 
  exit 1
fi

setGlobals $1
shift

set -x
fcn_call=($@)
fcn_call=$(printf '"%s",' "${fcn_call[@]}")
fcn_call="{\"Args\":[`echo "${fcn_call%,}"`]}"
peer chaincode query -C mychannel -n basic -c ${fcn_call} >&log.txt
res=$?
{ set +x; } 2>/dev/null
cat log.txt
