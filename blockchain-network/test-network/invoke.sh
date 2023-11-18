#!/bin/bash

. scripts/utils.sh
. scripts/setEnv.sh

if [ $1 != 1 ] && [ $1 != 2 ]
then
  errorln "The first arg should be 1(Seller;Manager) or 2(Buyer;Member)!" 
  exit 1
fi

setGlobals $1

set -x
fcn_name=$2
shift 2
args=($@)
args=$(printf '"%s",' "${args[@]}")
args="`echo "${args%,}"`"
fcn_call='{"function":"'$fcn_name'","Args":['$args']}'
infoln "invoke fcn call:${fcn_call}"
peer chaincode invoke -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile "${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem" -C mychannel -n basic --peerAddresses localhost:7051 --tlsRootCertFiles "${PWD}/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt" --peerAddresses localhost:9051 --tlsRootCertFiles "${PWD}/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt" -c ${fcn_call} >&log.txt
res=$?
{ set +x; } 2>/dev/null
cat log.txt
