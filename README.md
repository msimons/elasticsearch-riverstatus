Feeder Status plugin for Elasticsearch
================================
Provides insight about the feeders around your Elasticsearch cluster.

### Features###
* Overview of feeders

### Todo###
* Animated status indication per river

### Supported feeder implementation ###

- Elasticsearch JDBC [https://github.com/jprante/elasticsearch-jdbc](https://github.com/jprante/elasticsearch-jdbc)


![Alt text](src/site/overview.png "Screenshot Overview")

Development
-----------

### Run/debug ###
```shell
mvn install exec:java
```
This will start Elasticsearch on [http://localhost:9200](http://localhost:9200). You can access the plugin by going to [http://localhost:9200/_plugin/feederstatus](http://localhost:9200/_plugin/feederstatus).

### Build release (ZIP) ###

To build a ZIP file that can be installed to Elasticearch:

```shell
mvn package
```

The ZIP file can be found in `<project home>/target/releases`

Installation
------------

*This plugin is currently in development, no official releases have been published yet. If you would like to install the plugin manually, you can do so by getting the build release package (see Build release) and run the following command from within your Elasticsearch folder:*

```shell
./bin/plugin -u file:///path/to/plugin.zip -i elasticsearch-feederstatus
```

or use the latest snapshot build from GitHub (change URL to specific build version):

```shell
./bin/plugin -u <URL> -i elasticsearch-feederstatus
````
For example:
```shell
./bin/plugin -u https://github.com/msimons/elasticsearch-riverstatus/releases/download/1.0-SNAPSHOT_09_06_2015/elasticsearch-riverstatus-1.0-SNAPSHOT.zip -i elasticsearch-feederstatus
```



