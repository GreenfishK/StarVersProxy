# How to use the proxy
## Download the standalone version and license
1. Go to the GraphDB [website](https://graphdb.ontotext.com/) and download GraphDB free. You need to register first and then you will get a mail from Ontotext with the download options. \
2. Download "GraphDB as a standalone distributive" and put it into the directory src/test/resources/graphdb-docker-master/free-edition . \
3. Put the acquired license into the directory src/test/resources/graphdb-docker-master/free-edition (TODO: document how to acquire the license)

## Start the GraphDB database with docker-compose
Run `docker-compose up database`

## Start the Proxy server with docker-compose
Run `docker-compose up proxy`

## Build?
`mvn clean compile assembly:single` ?
