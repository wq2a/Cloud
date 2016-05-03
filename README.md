# Cloud Prototype
A prototype of the cloud environment.
## Getting Started
### Prerequisities
Before run the server you need to run mysql first, but no need to create the database.
### Protocol (HTTP like protocol)
In this project, we will generate HTTP like protocol.
####Request
Several types of requests will be considered in this project, for example, GET, POST, PUT and DELETE. The GET method is used to get a file or get the name list of a directory. The POST method is used to check authentication or close the connect. The PUT method is used to upload a file. The DELETE method is used to delete a file on cloud. Meanwhile, user cloud define any type of property in the request.
#### Response
The Status code is the same as HTTP protocol, we will use 200, 401, 404 and so on.
### Salt (cryptography)
In cryptography, a salt which is a secure random string that is used as an additional input to a one-way function that "hashes" a password. The main reason using salt is to defend against dictionary attacks versus a list of password hashes and against pre-computed rainbow table attacks. In this project, we use this argorithm to implement the user authentication. 
In the client side, we use the username as a salt combined with password and using SHA_256 to hash this combined string and get string H1. Then, pass both username and H1 to the server side. Next, in the server side, we will look for the received username in database and find another salt which used in server side and the H2 which was computed when user registered this account. At the end, we will hash h1 with the new salt and get string H2_, and the user will be validated if H2_ is the same as H2.

![GitHub Logo](/images/login.png)
Format: ![Alt Text](url)


## Built With
* Makefile
```
make serverClean - will create the required database and tables every time and start the server.
make server - will create the required database and tables at first time and start the server.
make client - start the client
make clean - remove all class files
```
