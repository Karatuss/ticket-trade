#!/bin/bash

. scripts/utils.sh

infoln "------------------------------------------------------------------------------------------------------------------"
infoln "| This script needs to excute before making a communication between Blockchain network and Backend.              |"
# infoln "|  |"
infoln "------------------------------------------------------------------------------------------------------------------"

warnln "0. Trim out some directories, files, or something for prepare the initial network environment."
# warnln "${PWD}"
# docker rm -f peer0seller_chaincode_ccaas peer0buyer_chaincode_ccaas 2> /dev/null
# TODO: Control $? of rm command below
rm -rf ${PWD}/../blockchain-network/application-go/wallet     # Prevent from the error, "io.grpc.StatusRuntimeException: UNKNOWN: ..."
rm -rf ${PWD}/../blockchain-network/application-gateway-go/result.txt


warnln "0.5. Build binary."
# cd ${PWD}/../blockchain-network/application-gateway-go
# go build assetTransfer.go
# cd -


warnln "1. Make sure the blockchain network being down."
. network.sh down


warnln "2. Let the network up again with creating a channel applying CA."
. network.sh up createChannel


warnln "3. Deploy the chaincode with Docker."
. network.sh deployCC -c mychannel -ccn basic -ccp ../chaincode-go/ -ccl go      # No init function version
# . network.sh deployCC -c mychannel -ccn basic -ccp ../chaincode-go/ -ccl go -cci InitLedger


warnln "4. Check invoke and query work normally."
. invoke.sh 1 InitLedger
. query.sh 1 GetAllAssets
# ./invoke.sh 1 InitLedger && ./query.sh 1 GetAllAssets
