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

## How It Works

![Alt text](./compiler.png?raw=true "Compiler")

you have four controllers one for Java, one for C, one for C ++ and another for Python. The call to these controllers is done through POST requests to the following urls :

- localhost:8080/compiler/**java**
- localhost:8080/compiler/**c**
- localhost:8080/compiler/**cpp**
- localhost:8080/compiler/**python**

Here is an example of the request body :
![Alt text](./requestBody.png?raw=true "Request Body")

- **output** : the expected output.
- **sourceCode** : your source code in java, c, c++ or python.
- **timeLimit** : the time limit in seconds that your code must not exceed during its execution.
- **memoryLimit** : the memory limit in Mb that your code must not exceed during its execution.
- **inputFile** : inputs written in separate lines.

## Author

- **Zakaria Maaraki** - _Initial work_ - [BrolyCode](https://github.com/BrolyCode)
