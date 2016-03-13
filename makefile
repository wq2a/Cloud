all:	server

server:	server/*.java
	javac server/*.java -d .
	java -cp .:lib/mysql-connector-java.jar cloud.server.Server 9900

client:	client/*.java
	javac client/*.java -d .
	java cloud.client.Client

clean:
	rm -rf cloud

.PHONY: all server client clean