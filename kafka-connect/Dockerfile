FROM ubuntu:trusty

RUN apt-get update
RUN apt-get install wget software-properties-common python-software-properties -y
RUN add-apt-repository "deb http://packages.confluent.io/deb/2.0 stable main"
RUN apt-get update
RUN apt-get install confluent-platform-2.11.7 -y --force-yes
RUN echo "name=oracle-jdbc-connector\
	connector.class=io.confluent.connect.jdbc.JdbcSourceConnector\
	tasks.max=1
	connection.url=jdbc:datadirect:oracle://<;server>:<port>;User=<user>;Password=<password>;Database=<dbname>\
	mode=timestamp+incrementing\
	incrementing.column.name=<id>\
	timestamp.column.name=<modifiedtimestamp>\
	topic.prefix=jdbc_\
	table.whitelist=actor" > /etc/oracle.properties

RUN echo "name=mongodb-jdbc-sink-connector
	connector.class=io.confluent.connect.jdbc.JdbcSinkConnector
	tasks.max=1
	topics=
	connection.url=jdbc:datadirect:mongodb://mongo;Database=cache
	auto.create=true
