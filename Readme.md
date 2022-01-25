# Attestation Engine

## Development
How to set up locally for development:

### Initial Set Up
* The following keys should be generated
  * `DATASOURCE_URL` - url to running postgres(default: `jdbc:postgresql://localhost:5432/oracle`)
  * `DATABASE_USERNAME` & DATABASE_PASSWORD - credentials to access running postgres
  * `XMPP_DOMAIN` - xmpp domain
  * `XMPP_HOST` - host with running xmpp server(for example `prosody`)
  * `XMPP_PASSWORD` - credentials for attestation engine to login to xmpp server (account public address will be used as username) 
  * `XMPP_ADMIN_USERNAME` & `XMPP_ADMIN_PASSWORD` - xmpp admin credentials (admin should be able to create users)
  * `ACCOUNT_PRIVATE_KEY` - wallet private key
  * `WEB3_HOST` - link to blockchain net API
  * `CASHIER_CONTRACT_ADDRESS` & `TRUST_TOKEN_ADDRESS` & `ERC20_ADDRESS` - addresses of deployed contracts
* Database can be quickly run locally with the following command (`docker-compose.yml`)
  ```shell
  docker-compose up -d
  ```
  That will also run docker for attestation provider example
* XMPP deployment it complex step, documentation about `prosody` deployment can be found here:
  https://prosody.im/
  * Admin credentials should be created there
* ETH account should be generated according to the net attestation engine is going to use
  * RSK testnet will be used here as example: `https://public-node.testnet.rsk.co`
  * Metamask (https://metamask.io/) can help to create account, account private key can be extracted from it
  * https://faucet.rsk.co/ should be used to top up the created account
  * Inside deployed prosody the following account should be created `${ACCOUNT_ADDRESS}@${XMPP_DOMAIN}`

### Building docker image
* Tools
  * Web3J generator is required for building the app 
  * Libsodium is required for running the app
  * Web3J generator and libsodium can be installed with the following comands (Ubuntu) (Dockerfile.builder)
  ```shell
    apt-get install -y libsodium-dev
    curl -L get.web3j.io | sh && . ~/.web3j/source.sh
  ```
* Running docker engine is required to build a jar (`.gitlab.ci.yml`):
```shell
./gradlew clean generateWeb3J build
```
* Building docker image:
```shell
docker build .
```
* Resulting image can be used to run attestation engine

