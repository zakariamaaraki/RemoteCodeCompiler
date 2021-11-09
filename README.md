[![Build and Test](https://github.com/zakariamaaraki/RemoteCodeCompiler/actions/workflows/build.yaml/badge.svg)](https://github.com/zakariamaaraki/RemoteCodeCompiler/actions/workflows/build.yaml) [![Docker](https://badgen.net/badge/icon/docker?icon=docker&label)](https://https://docker.com/) [![MIT license](https://img.shields.io/badge/License-MIT-blue.svg)](https://lbesson.mit-license.org/) [![Test Coverage](https://github.com/zakariamaaraki/RemoteCodeCompiler/blob/master/.github/badges/jacoco.svg)](https://github.com/zakariamaaraki/RemoteCodeCompiler/actions/workflows/build.yaml)

# Remote Code Compiler

An online code compiler for Java, C, C++ and Python for competitive programming and coding interviews.
This service execute your code remotely using docker containers to separate the different environements of executions.

## Prerequisites

To run this project you need a docker engine running on your machine.

## Getting Started

Build docker image by typing the following command :

```shell
docker image build . -t compiler
```

Run the container by typing the following command

```shell
docker container run -p 8080:8082 -v /var/run/docker.sock:/var/run/docker.sock -t compiler
```

Now your server is listening on the port 8080.

### Portainer
It might be a good idea if you run a **Portainer** instance and mount it to the same volume, in order to have a total view of created images and running containers.

```shell
docker container run -p 9000:9000 -v /var/run/docker.sock:/var/run/docker.sock portainer/portainer
```

### On K8s
You can use the provided helm chart to deploy the project on k8s

```shell
helm install compiler ./k8s/helm_charts/compiler_chart
```

```shell
kubectl get all  // display all resources
```

Note if you are running k8s using Minikube :
* you can reuse the Docker daemon from Minikube by running this command : **eval $(minikube docker-env)**.
* set image pull policy to **Never** in the values.yml file.


## How It Works

![Architecture](images/compiler.png?raw=true "Compiler")

There is four endpoints, one for Java, one for C, one for C ++ and another for Python. The call is done through POST requests to the following urls :

- localhost:8080/compiler/**java**
- localhost:8080/compiler/**c**
- localhost:8080/compiler/**cpp**
- localhost:8080/compiler/**python**

For the documentation visit the swagger page at the following url : http://localhost:8080/swagger-ui.html

![Compilers endpoints](images/swagger.png?raw=true "Swagger")

### Visualize Docker images and containers info
It is also possible to visualize information about the images and docker containers that are currently running using these endpoints 

![Docker info](images/swagger-docker-info.png?raw=true "Docker info Swagger")

#### Example of an Output

##### Docker Containers 

![Docker info response](images/docker-info-response.png?raw=true "Docker info Swagger")

##### Docker Images 

![Docker images info](images/docker-images-info.png?raw=true "Docker images info Swagger")

### How the docker image is generated

We generate an entrypoint.sh file depending on the information given by the user (time limit, memory limit, programming language, and also the inputs).

![Alt text](images/image_generation.png?raw=true "Docker image Generation")



### Metrics
Check out exposed prometheus metrics using the following url : http://localhost:8080/actuator/prometheus

## Author

- **Zakaria Maaraki** - _Initial work_ - [zakariamaaraki](https://github.com/zakariamaaraki)
