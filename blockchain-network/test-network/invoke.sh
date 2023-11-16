#!/bin/bash

. scripts/utils.sh
. scripts/setEnv.sh

setGlobals $1

# TODO: Get arguments and make them comma-separated string

set -x
fcn_call='{"function":"'$2'","Args":[]}'
infoln "invoke fcn call:${fcn_call}"
peer chaincode invoke -o localhost:7050 --ordererTLSHostnameOverride orderer.example.com --tls --cafile "${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem" -C mychannel -n basic --peerAddresses localhost:7051 --tlsRootCertFiles "${PWD}/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt" --peerAddresses localhost:9051 --tlsRootCertFiles "${PWD}/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt" -c ${fcn_call} >&log.txt
res=$?
{ set +x; } 2>/dev/null
cat log.txt
