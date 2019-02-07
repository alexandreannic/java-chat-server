# java-chat-server

Simple multithreaded java server done during my 1st year of Master.

### Usage 

#### Start the server

Go to the source directory

```
cd java-chat-server
```

Compile

```
javac -classpath src/ src/Main.java
``` 

Run giving as argument a port to listen (eg. 12345) and a max client capacity (at least 2, chatting with yourself doesn't make sens) 

```
java -classpath src/ Main <port> <capacity>
```

#### Client using `netcat`

Start as much client that you want. Beyond capacity, others client will be on the waiting list.
```
netcat localhost <port>
``` 
