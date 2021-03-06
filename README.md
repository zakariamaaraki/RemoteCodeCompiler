# Remote Code Compiler

An online code compiler for Java, C, C++ and Python for competitive programming and coding interviews.
This service execute your code remotely using docker containers to separate the different environements of executions.

## Prerequisites

To run this project you need a docker engine running on your machine.

## Getting Started

Build docker image by typing the following command :

```
docker image build . -t compiler
```

Run the container by typing the following command

```
docker container run -p 8080:8082 -v /var/run/docker.sock:/var/run/docker.sock -t compiler
```

Now your server is listening on the port 8080

## How It Works

![Alt text](./compiler.png?raw=true "Compiler")

There is four controllers one for Java, one for C, one for C ++ and another for Python. The call to these controllers is done through POST requests to the following urls :

- localhost:8080/compiler/**java**
- localhost:8080/compiler/**c**
- localhost:8080/compiler/**cpp**
- localhost:8080/compiler/**python**

For the documentation visit the swagger page at the following url : http://localhost:8080/swagger-ui.html

![Alt text](./swagger.png?raw=true "Swagger")

## Author

- **Zakaria Maaraki** - _Initial work_ - [zakariamaaraki](https://github.com/zakariamaaraki)
