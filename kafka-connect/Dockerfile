FROM ubuntu:trusty

RUN apt-get update
RUN apt-get install wget software-properties-common python-software-properties -y
RUN add-apt-repository "deb http://packages.confluent.io/deb/2.0 stable main"
RUN apt-get update
RUN apt-get install confluent-platform-2.11.7 openjdk-7-jre -y --force-yes

# ADD drivers/PROGRESS_DATADIRECT_JDBC_MONGODB_ALL.jar /
# ADD drivers/PROGRESS_DATADIRECT_JDBC_ORACLE_ALL.jar /

# RUN java -jar /PROGRESS_DATADIRECT_JDBC_MONGODB_ALL.jar -D-i=silent
# RUN java -jar /PROGRESS_DATADIRECT_JDBC_ORACLE_ALL.jar

ADD etc/worker.properties /etc
ADD etc/oracle-jdbc-connector.properties /etc
ADD etc/mongodb-jdbc-sink-connector.properties /etc

CMD bash
