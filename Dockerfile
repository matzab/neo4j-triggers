FROM neo4j:5.10.0-community-bullseye
ENV NEO4J_AUTH=neo4j/my_password
ENV NEO4J_server_https_advertised__address="localhost:7473"
ENV NEO4J_server_http_advertised__address="localhost:7474"
ENV NEO4J_server_bolt_advertised__address="localhost:7687"
COPY target/neo4j-triggers-1.0.0.jar $NEO4J_HOME/plugins/

