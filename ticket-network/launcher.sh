#!/bin/bash

. scripts/utils.sh

infoln "1. Make sure the blockchain network being down."
. start.sh down

infoln "2. Let the network up again with creating a channel applying CA."
. start.sh up createChannel -ca

infoln "3. Deploy the chaincode with Docker."
. start.sh deployCCAAS -ccn chaincode -ccp ../chaincode
