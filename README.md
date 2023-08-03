## Neo4j Triggers Requirements

-
    1) Deployed in docker
-
    2) Developed as plugin (java)
-
    3) Provide logging

# Proof-of-concept

### 1. Docker set up

- Download Neo4j docker image version:`neo4j:5.10.0-community-bullseye -> latest`.
  - ***Before running the container, directories directories must be created for 
  persistent data***
  - > mkdir path/to/file 
- run the container
> docker run --name <container-name> \
> --restart always \
> --publish=7474:7474\
> --publish=7687:7687 \
> --env NEO4J_AUTH=neo4j/my_password \
> --volume=/path/to/data:/data \
> --volume=/path/to/logs:/logs neo4j:latest

- Create volumes to persist data between container restarts:
  - enable http and bolt communication
  - set up password for neo4j user
  - mount logging directory in `/logs`
  - mount database to `/data/database` in the host machine
- Setting up Neo4j Logging:
  1) Modify default logging configuration provided by Neo4j Image in `conf/user-logs.xml` file, adding custom Appender and
    Logger for the  OR
  2)  Create config volume and copy provided config files when running the container

```xml
...
<RollingRandomAccessFile name="Neo4jDBChanges" fileName="${config:server.directories.logs}/changes.log"
                         filePattern="$${config:server.directories.logs}/neo4j.log.%02i">
    <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss.SSSZ}{GMT+0} %-5p %m%n"/>
    <Policies>
        <SizeBasedTriggeringPolicy size="20 MB"/>
    </Policies>
    <DefaultRolloverStrategy fileIndex="min" max="7"/>
</RollingRandomAccessFile>
        ...
<Logger name="<package.name>" level="INFO" additivity="false">
<AppenderRef ref="Neo4jDBChanges"/>
</Logger>
        ...
```

-  compile and build plugin's `.jar ` file, copy the file to `$NEO4J_HOME/plugins/` directory

> docker cp <path/to/file.jar> <container-name>:<path/to/neo4j/plugins/folder/
- restart docker image, and plugin should be integrated in the database
> docker restart <image_name>
### 2. Java Plugin

- Triggers implemented as  `TransactionEventListener<>` with implementation of `beforeCommit`
  `afterCommit` and `afterRollback` functions.
- Plugin is registered to Neo4j database on start and unregistered on shutdown







