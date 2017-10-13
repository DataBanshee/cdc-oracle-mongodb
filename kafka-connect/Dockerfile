FROM ubuntu:trusty

RUN apt-get update
RUN apt-get install wget software-properties-common python-software-properties -y
RUN add-apt-repository "deb http://packages.confluent.io/deb/2.0 stable main"
RUN apt-get update
RUN apt-get install confluent-platform-2.11.7 openjdk-7-jre -y --force-yes
RUN echo "name=oracle-jdbc-connector \
	connector.class=io.confluent.connect.jdbc.JdbcSourceConnector \
	tasks.max=1 \
	connection.url=jdbc:datadirect:oracle://oracle:1521;User=system;Password=oracle;Database=xe \
	mode=timestamp+incrementing \
        key.converter=io.confluent.connect.avro.AvroConverter \
        key.converter.schema.registry.url=http://kafka:8081 \
	incrementing.column.name=<id> \
	timestamp.column.name=<modifiedtimestamp> \
	topic.prefix=jdbc_\
	table.whitelist=actor" > /etc/oracle-jdbc-connector.properties

RUN echo "name=mongodb-jdbc-sink-connector \
	connector.class=io.confluent.connect.jdbc.JdbcSinkConnector \
        key.converter=io.confluent.connect.avro.AvroConverter \
        key.converter.schema.registry.url=http://localhost:8081 \
	tasks.max=1 \
	topics= \
	connection.url=jdbc:datadirect:mongodb://mongo;Database=cache \
	auto.create=true" > /etc/mongodb-sink-jdbc-connector.properties

CMD bash
