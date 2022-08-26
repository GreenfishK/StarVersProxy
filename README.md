# 1. How to use the proxy
Follow these steps to use the proxy with its default configurations. To see how it can be reconfigured to target a specific server address, see [Configurations](#Configurations).

## Download the standalone version and license
1. Go to the GraphDB [website](https://graphdb.ontotext.com/) and download GraphDB free. You need to register first and then you will get a mail from Ontotext with the download options. 
2. Download "GraphDB as a standalone distributive" and put it into the directory src/test/resources/graphdb-docker-master/free-edition . 
3. Put the acquired license into the directory src/test/resources/graphdb-docker-master/free-edition (TODO: document how to acquire the license)

## Start the Proxy server with docker-compose
Run `docker-compose up proxy`
By default, the proxy will run on on port 7480 and localhost. It will connect to localhost:7200, which is the default GraphDB server. This is currently hardcoded in the main method of the Proxy.java file.

## Execute SPARQL queries
TODO: Describe the possible ways how SPARQL queries can be sent to the proxy
### Using a graphical query interface

### Using an API

# 2. Configurations
## Configure server address
The server address and port can be configered in the src/main/resources/config.properties file. 


# 3. For testing and experimenting
This keeps the infrastructure that builds docker images for [GraphDB](http://graphdb.ontotext.com/)

Check [Docker Hub Images](https://hub.docker.com/r/ontotext/graphdb/) for information on how to use the images.

Note that to use GraphDB EE or SE docker images, you must get a license from us first.

Currently there are no public images for GraphDB Free and you will have to build those if needed from the zip distribution that you get after registering on our website.

## Preload preconfigured GraphDB repository

Run the following service do preload the testTimestamping GraphDB repository.
```
docker-compose up preload
```

By default it will:

* Create and override the repository testTimestamping as defined in the `graphdb-repo-config.ttl` file.
* Upload a test ntriple file from the `preload/import` subfolder.

> See the [GraphDB preload documentation](http://graphdb.ontotext.com/documentation/free/loading-data-using-preload.html) for more details.

When running the preload docker-compose various parameters can be provided in the `preload/.env` file:

```bash
GRAPHDB_VERSION=9.10.3
GRAPHDB_HEAP_SIZE=2g
GRAPHDB_HOME=../graphdb-data
REPOSITORY_CONFIG_FILE=./graphdb-repo-config.ttl
```

Build and run:

```bash
docker-compose build
docker-compose up -d preload
```

> GraphDB data will go to `/data/graphdb`

Go back to the root of the git repository to start GraphDB:

```bash
cd ..
```

## Start GraphDB inside a container
Use following command to run your GraphDB inside a container. This eases manual experimenting and testing as you do not need to manually start a GraphDB instance first. 
Run `docker-compose up database`
You can run this service without preloading the repository if you need to experiment with an empty repository.

## Run everything

To preload the GraphDB repository, run GraphDB in the backround and start the proxy server run the following command:

```bash
docker-compose up -d
```

> It will use the repo created by the preload in `graphdb-data/`

> Feel free to add a `.env` file similar to the preload repository to define variables.


## Original docu
https://github.com/Ontotext-AD/graphdb-docker
