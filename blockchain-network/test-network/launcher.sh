#!/bin/bash

. scripts/utils.sh

warnln "0. Trim out some directories, files, or something for prepare the initial network environment."
# warnln "${PWD}"
# TODO: Control $? of rm command below
rm -rf ${PWD}/../Gateway/wallet     # Prevent from the error, "io.grpc.StatusRuntimeException: UNKNOWN: ..."
docker rm -f peer0seller_chaincode_ccaas peer0buyer_chaincode_ccaas 2> /dev/null


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
