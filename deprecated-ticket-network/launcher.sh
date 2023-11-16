#!/bin/bash

. scripts/utils.sh

infoln "0. Trim out some directories, files, or something for prepare the initial network environment."
# warnln "${PWD}"
# TODO: Control $? of rm command above
rm -rf ${PWD}/../Gateway/wallet     # Prevent from the error, "io.grpc.StatusRuntimeException: UNKNOWN: ..."
docker rm -f peer0seller_chaincode_ccaas peer0buyer_chaincode_ccaas 2> /dev/null


infoln "1. Make sure the blockchain network being down."
. start.sh down


infoln "2. Let the network up again with creating a channel applying CA."
. start.sh up createChannel -ca


infoln "3. Deploy the chaincode with Docker."
# . start.sh deployCCAAS -ccn chaincode -ccp ../chaincode
# . start.sh deployCC -ccn chaincode -ccp ../chaincode -ccl java
. start.sh deployCC -c ticket-channel -ccn chaincode -ccp ../chaincode-go/ -ccl go -cci InitLedger
