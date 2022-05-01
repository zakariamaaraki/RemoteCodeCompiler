[![Build and Test](https://github.com/zakariamaaraki/RemoteCodeCompiler/actions/workflows/build.yaml/badge.svg)](https://github.com/zakariamaaraki/RemoteCodeCompiler/actions/workflows/build.yaml) [![Docker](https://badgen.net/badge/icon/docker?icon=docker&label)](https://https://docker.com/) [![Test Coverage](https://github.com/zakariamaaraki/RemoteCodeCompiler/blob/master/.github/badges/jacoco.svg)](https://github.com/zakariamaaraki/RemoteCodeCompiler/actions/workflows/build.yaml)
[![CodeQL](https://github.com/zakariamaaraki/RemoteCodeCompiler/actions/workflows/codeAnalysis.yaml/badge.svg)](https://github.com/zakariamaaraki/RemoteCodeCompiler/actions/workflows/codeAnalysis.yaml)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

# Remote Code Compiler

An online code compiler for Java, C, C++ and Python for competitive programming and coding interviews.
This service execute your code remotely using docker containers to separate the different environements of executions.

Supports **Rest Calls (Long Polling and [Push Notification](https://en.wikipedia.org/wiki/Push_technology))**, **Apache Kafka** and **Rabbit MQ Messages**.

### Example of an input

```json
{
    "input": "9",
    "expectedOutput": "0 1 2 3 4 5 6 7 8 9",
    "sourceCode": "<YOUR_SOURCE_CODE>",
    "language": "JAVA",
    "timeLimit": 15,
    "memoryLimit": 500
}
```

### Example of an ouput

The compiler cleans your output, so having extra spaces or line breaks does not affect the status of the response.

```json
{
  "result": {
    "statusResponse": "Accepted",
    "statusCode": 100,
    "output": "0 1 2 3 4 5 6 7 8 9",
    "expectedOutput": "0 1 2 3 4 5 6 7 8 9",
    "executionDuration": 2111
  },
  "dateTime": "2022-01-28T23:32:02.843465"
}
```

## Prerequisites

To run this project you need a docker engine running on your machine.

## Getting Started

Build docker image by typing the following command :

```shell
docker image build . -t compiler
```

Run the container by typing the following command

```shell
sudo docker container run -p 8080:8082 -v /var/run/docker.sock:/var/run/docker.sock -e DELETE_DOCKER_IMAGE=true -e EXECUTION_MEMORY_MAX=10000 -e EXECUTION_MEMORY_MIN=0 -e EXECUTION_TIME_MAX=15 -e EXECUTION_TIME_MIN=0 -e MAX_REQUESTS=1000 -t compiler
```
* The value of the env variable **DELETE_DOCKER_IMAGE** is by default set to true, and that means that each docker image is deleted after the execution of the container. 
* The value of the env variable **EXECUTION_MEMORY_MAX** is by default set to 10 000 MB, and represents the maximum value of memory limit that we can pass in the request. **EXECUTION_MEMORY_MIN** is by default set to 0.
* The value of the env variable **EXECUTION_TIME_MAX** is by default set to 15 sec, and represents the maximum value of time limit that we can pass in the request. **EXECUTION_TIME_MIN** is by default set to 0.  
* **MAX_REQUESTS** represents the number of requests that can be executed in parallel. When this value is reached all incoming requests will be throttled and the user will get 429 HTTP status code (there will be a retry in queue mode).

### Push Notifications

To enable push notifications you must set this env variable to true : **ENABLE_PUSH_NOTIFICATION**

For long running executions, you may want to get the response later and to avoid http timeouts, you can use push notifications,
to do so you must pass two header values (**url** where you want to get the response and **preferPush=prefer-push**)

### Local Run (for dev environment only)
Look at the documentation in local folder, a docker-compose is provided.

### On K8s
You can use the provided helm chart to deploy the project on k8s

```shell
helm install compiler ./k8s/helm_charts/compiler_chart
```

Note if you are running k8s using Minikube :
* you can reuse the Docker daemon from Minikube by running this command : **eval $(minikube docker-env)**.
* set image pull policy to **Never** in the values.yaml file.


## How It Works

The compiler creates a container for each execution to separate the execution environments.

![Architecture](images/compiler.png?raw=true "Compiler")

There are 5 endpoints. The call is done through POST requests to the following urls :

- localhost:8080/compiler/**java**
- localhost:8080/compiler/**c**
- localhost:8080/compiler/**cpp**
- localhost:8080/compiler/**python**
- localhost:8080/compiler/**json**

For the documentation visit the swagger page at the following url : http://localhost:8080/swagger-ui.html

![Compilers endpoints](images/swagger.png?raw=true "Swagger")

### Verdicts

#### Accepted
```json
{
  "result": {
    "statusResponse": "Wrong Answer",
    "statusCode": 200,
    "output": "YES",
    "expectedOutput": "YES",
    "executionDuration": 73
  },
  "dateTime": "2022-01-28T23:32:02.843465"
}
```

#### Wrong Answer

```json
{
  "result": {
    "statusResponse": "Wrong Answer",
    "statusCode": 200,
    "output": "YES",
    "expectedOutput": "NO",
    "executionDuration": 116
  },
  "dateTime": "2022-01-28T23:32:02.843465"
}
```

#### Compilation Error

```json
{
  "result": {
    "statusResponse": "Compilation Error",
    "statusCode": 300,
    "output": "",
    "expectedOutput": "0 1 2",
    "executionDuration": 0
  },
  "dateTime": "2022-01-28T23:32:02.843465"
}
```

#### Time Limit Exceeded
```json
{
  "result": {
    "statusResponse": "Time Limit Exceeded",
    "statusCode": 500,
    "output": "",
    "expectedOutput": "YES",
    "executionDuration": 15001
  },
  "dateTime": "2022-01-28T23:32:02.843465"
}
```

#### Runtime Error
```json
{
  "result": {
    "statusResponse": "Runtime Error",
    "statusCode": 600,
    "output": "",
    "expectedOutput": "YES",
    "executionDuration": 0
  },
  "dateTime": "2022-01-28T23:32:02.843465"
}
```

#### Out Of Memory
```json
{
  "result": {
    "statusResponse": "Out Of Memory",
    "statusCode": 400,
    "output": "",
    "expectedOutput": "YES",
    "executionDuration": 0
  },
  "dateTime": "2022-01-28T23:32:02.843465"
}
```

### Visualize Docker images and containers infos
It is also possible to visualize information about the images and docker containers that are currently running using these endpoints

![Docker info](images/swagger-docker-infos.png?raw=true "Docker info Swagger")

## Kafka Mode

![remote code compiler kafka mode](images/kafka-streams.png?raw=true "Compiler Kafka Mode")

You can use the compiler with an event driven architecture.
To enable kafka mode you must pass to the container the following env variables :
* **ENABLE_KAFKA_MODE** : True or False
* **KAFKA_INPUT_TOPIC** : Input topic, json request
* **KAFKA_OUTPUT_TOPIC** : Output topic, json response
* **KAFKA_CONSUMER_GROUP_ID** : Consumer group
* **KAFKA_HOSTS** : List of brokers
* **CLUSTER_API_KEY** : API key
* **CLUSTER_API_SECRET** : API Secret
* **KAFKA_THROTTLING_DURATION** : Throttling duration, by default set to 10000ms (when number of docker containers running reach MAX_REQUESTS, this value is used to do not lose the request and retry after this duration)

For best performance it's better to use 4 partitions for input topic

```shell
sudo docker container run -p 8080:8082 -v /var/run/docker.sock:/var/run/docker.sock -e DELETE_DOCKER_IMAGE=true -e EXECUTION_MEMORY_MAX=10000 -e EXECUTION_MEMORY_MIN=0 -e EXECUTION_TIME_MAX=15 -e EXECUTION_TIME_MIN=0 -e ENABLE_KAFKA_MODE=true -e KAFKA_INPUT_TOPIC=topic.input -e KAFKA_OUTPUT_TOPIC=topic.output -e KAFKA_CONSUMER_GROUP_ID=compilerId -e KAFKA_HOSTS=ip_broker1,ip_broker2,ip_broker3 -e API_KEY=YOUR_API_KEY -e API_SECRET=YOUR_API_SECRET -t compiler
```

## Rabbit MQ Mode

![remote code compiler rabbitMq mode](images/rabbitMq.png?raw=true "Compiler rabbitMq Mode")

To enable Rabbit MQ mode you must pass to the container the following env variables :
* **ENABLE_RABBITMQ_MODE** : True or False
* **RABBIT_QUEUE_INPUT** : Input queue, json request
* **RABBIT_QUEUE_OUTPUT** : Output queue, json response
* **RABBIT_USERNAME** : Rabbit MQ username  
* **RABBIT_PASSWORD** : Rabbit MQ password
* **RABBIT_HOSTS** : List of brokers
* **RABBIT_THROTTLING_DURATION** : Throttling duration, by default set to 10000ms (when number of docker containers running reach MAX_REQUESTS, this value is used to do not lose the request and retry after this duration)

```shell
sudo docker container run -p 8080:8082 -v /var/run/docker.sock:/var/run/docker.sock -e DELETE_DOCKER_IMAGE=true -e EXECUTION_MEMORY_MAX=10000 -e EXECUTION_MEMORY_MIN=0 -e EXECUTION_TIME_MAX=15 -e EXECUTION_TIME_MIN=0 -e ENABLE_RABBITMQ_MODE=true -e RABBIT_QUEUE_INPUT=queue.input -e RABBIT_QUEUE_OUTPUT=queue.output -e RABBIT_USERNAME=guest -e RABBIT_PASSWORD=guest -e RABBIT_HOSTS=ip_broker1,ip_broker2,ip_broker3 -t compiler
```

### Metrics
Check out exposed prometheus metrics using the following url : http://localhost:8080/actuator/prometheus

![Parallel executions](images/parallel-executions-metrics.png?raw=true "Parallel Executions Metrics")

![Throttling counter](images/throttling-counter-metrics.png?raw=true "Throttling Counter Metrics")

## Author

- **Zakaria Maaraki** - _Initial work_ - [zakariamaaraki](https://github.com/zakariamaaraki)
