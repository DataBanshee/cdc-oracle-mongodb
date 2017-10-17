FROM gayakwad/confluent-platform

#ADD drivers/PROGRESS_DATADIRECT_JDBC_MONGODB_ALL.jar /
#ADD drivers/PROGRESS_DATADIRECT_JDBC_ORACLE_ALL.jar /

# RUN java -jar /PROGRESS_DATADIRECT_JDBC_MONGODB_ALL.jar -D-i=silent
# RUN java -jar /PROGRESS_DATADIRECT_JDBC_ORACLE_ALL.jar


ADD drivers/ojdbc7.jar /confluent-3.0.0/share/java/kafka-connect-jdbc/

ADD etc/worker.properties /etc
ADD etc/oracle-jdbc-connector.properties /etc
ADD etc/mongodb-jdbc-sink-connector.properties /etc

CMD bash
