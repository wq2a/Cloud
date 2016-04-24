all:	server

server:	server/*.java
	javac server/*.java -d .
	java -cp .:lib/mysql-connector-java.jar cloud.server.Server 9900

serverClean:	server/*.java
	javac server/*.java -d .
	java -cp .:lib/mysql-connector-java.jar cloud.server.Server 9900 1

# java cloud.client.Client
client:	client/*.java
	javac client/*.java -d .
	sudo cp -r resources cloud/client/
	jar cvfe client.jar cloud.client.Client cloud/client

clean:
	sudo rm -rf cloud

.PHONY: all server client clean
