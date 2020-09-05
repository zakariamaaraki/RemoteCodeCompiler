# RemoteCodeCompiler

An online code compiler for Java, C, C++ and Python for competitive programming and coding interviews.
This service execute your code remotely using docker containers to separate the different environements of executions.  

## Prerequisites

To run this project you need a docker engine running on your machine.


## Getting Started

Build docker image by typing the following command :

```
docker image build . -t compilertest  
```

Run the container by typing the following command 

```
docker container run -p 8080:8082 -v /var/run/docker.sock:/var/run/docker.sock -t compilertest
```

Now your server is listening on the port 8080

## Author

- **Zakaria Maaraki** - _Initial work_ - [BrolyCode](https://github.com/BrolyCode)
