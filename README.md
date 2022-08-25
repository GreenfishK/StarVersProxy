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
TODO: Add a configuration file for the database server address. Currently the host and port are hardcoded in Proxy.java.


# 3. For testing and experimenting
## Start the GraphDB database with docker-compose
Use following command to run your GraphDB inside a container. This eases manual experimenting and testing as you do not need to manually start a GraphDB instance first.
Run `docker-compose up database`
