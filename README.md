[![Build and Test](https://github.com/zakariamaaraki/RemoteCodeCompiler/actions/workflows/build-master.yaml/badge.svg)](https://github.com/zakariamaaraki/RemoteCodeCompiler/actions/workflows/build.yaml) [![Docker](https://badgen.net/badge/icon/docker?icon=docker&label)](https://https://docker.com/) [![Test Coverage](https://github.com/zakariamaaraki/RemoteCodeCompiler/blob/master/.github/badges/jacoco.svg)](https://github.com/zakariamaaraki/RemoteCodeCompiler/actions/workflows/build.yaml)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

# Remote Code Compiler

![UX](images/UX-RemoteCodeCompiler.png?raw=true "User Interface")

An online code compiler supporting 11 languages (**Java**, **Kotlin**, **C**, **C++**, **C#**, **Golang**, **Python**, **Scala**, **Ruby**, **Rust** and **Haskell**) for competitive programming and coding interviews.
This tool execute your code remotely using docker containers to separate environments of execution.

![Supported languages](images/supported-languages.png?raw=true "supported-languages logos")

Supports **Rest Calls (Long Polling and [Push Notification](https://en.wikipedia.org/wiki/Push_technology))**, **Apache Kafka** and **Rabbit MQ Messages**.

**Example of an input**

```json
{
    "testCases": {
      "test1": {
        "input": "<YOUR_INPUT>",
        "expectedOutput": "<YOUR_EXPECTED_OUTPUT>"
      },
      "test2": {
        "input": "<YOUR_INPUT>",
        "expectedOutput": "<YOUR_EXPECTED_OUTPUT>"
      },
      ...
    },
    "sourceCode": "<YOUR_SOURCE_CODE>",
    "language": "JAVA",
    "timeLimit": 15,  
    "memoryLimit": 500 
}
```

**Example of an ouput**

The compiler cleans up your output, which means having extra spaces or line breaks does not affect the status of the response.

```json
{
    "verdict": "Accepted",
    "statusCode": 100,
    "error": "",
    "testCasesResult": {
      "test1": {
        "verdict": "Accepted",
        "verdictStatusCode": 100,
        "output": "0 1 2 3 4 5 6 7 8 9",
        "error": "", 
        "expectedOutput": "0 1 2 3 4 5 6 7 8 9",
        "executionDuration": 175
      },
      "test2": {
        "verdict": "Accepted",
        "verdictStatusCode": 100,
        "output": "9 8 7 1",
        "error": "" ,
        "expectedOutput": "9 8 7 1",
        "executionDuration": 273
      },
      ...
    },
    "compilationDuration": 328,
    "averageExecutionDuration": 183,
    "timeLimit": 1500,
    "memoryLimit": 500,
    "language": "JAVA",
    "dateTime": "2022-01-28T23:32:02.843465"
}
```

## Prerequisites

To run this project you need a docker engine running on your machine.

## Getting Started

1- Build a docker image:

```shell
docker image build . -t compiler
```

2- Create a volume:

```shell
docker volume create compiler
```

3- build the necessary docker images used by the compiler

```shell
./environment/build.sh
```

4- Run the container:

```shell
docker container run -p 8080:8082 -v /var/run/docker.sock:/var/run/docker.sock -v compiler:/compiler -e DELETE_DOCKER_IMAGE=true -e EXECUTION_MEMORY_MAX=10000 -e EXECUTION_MEMORY_MIN=0 -e EXECUTION_TIME_MAX=15 -e EXECUTION_TIME_MIN=0 -e MAX_REQUESTS=1000 -e MAX_EXECUTION_CPUS=0.2 -e COMPILATION_CONTAINER_VOLUME=compiler -t compiler
```
* The value of the env variable **DELETE_DOCKER_IMAGE** is by default set to true, and that means that each docker image is deleted after the execution of the container. 
* The value of the env variable **EXECUTION_MEMORY_MAX** is by default set to 10 000 MB, and represents the maximum value of memory limit that we can pass in the request. **EXECUTION_MEMORY_MIN** is by default set to 0.
* The value of the env variable **EXECUTION_TIME_MAX** is by default set to 15 sec, and represents the maximum value of time limit that we can pass in the request. **EXECUTION_TIME_MIN** is by default set to 0.  
* **MAX_REQUESTS** represents the number of requests that can be executed in parallel. When this value is reached all incoming requests will be throttled, and the user will get 429 HTTP status code (there will be a retry in queue mode).
* **MAX_EXECUTION_CPUS** represents the maximum number of cpus to use for each execution (by default the maximum available cpus). If this value is set, then all requests will be throttled when the service reaches the maximum.
* **COMPILATION_CONTAINER_VOLUME** It should be the same as the volume created in step 2.
* **MAX_TEST_CASES** Maximum number of test cases a request should handle (by default it's set to 20)

### Push Notifications
You may want to get the response later and to avoid http timeouts, you can use push notifications,
to do so you should pass two header values (**url** where you want to get the response and set **preferPush** to prefer-push)

![push-notifications.png](images/webhooks.png)

To enable push notifications you should set the environment variable **ENABLE_PUSH_NOTIFICATION** to true

### Multipart request

You have also the possibility to use multipart requests, you typically can use these requests for file uploads and for transferring data of several types in a single request.
The only limitation with that, is that you can specify only one test case.

![multipart-request.png](images/multipart-request.png)

## Local Run (for dev environment only)
See the documentation in the [local](https://github.com/zakariamaaraki/RemoteCodeCompiler/tree/master/local) folder, a docker-compose is provided.

```shell
docker-compose up --build
```

## On K8s

<p align="center">
<img height="100px" width="100px" src="https://upload.wikimedia.org/wikipedia/commons/thumb/3/39/Kubernetes_logo_without_workmark.svg/1200px-Kubernetes_logo_without_workmark.svg.png" alt="k8s logo"/>
</p>

You can use the provided helm chart to deploy the project on k8s, see the documentation in the [k8s](https://github.com/zakariamaaraki/RemoteCodeCompiler/tree/master/k8s) folder.

```shell
helm install compiler ./k8s/compiler
```

### AKS Provisioning
We provide you with a script to provision an AKS cluster to ease your deployment experience. See the documentation in the [provisioning](https://github.com/zakariamaaraki/RemoteCodeCompiler/tree/master/provisioning/arm) folder.

## How It Works

When a request comes in, the compiler creates a container responsible of compiling the given sourcecode (this container shares the same volume with the main application). After a successful compilation, an execution container (with it's own execution environment and totally isolated from other containers) is created for each test case.

![Architecture](images/remote_code_compiler_architecture.png?raw=true "Compiler")

<p>
    In the execution step, each container is assigned a set number of CPUs (consistent across all containers,
with a recommended value of 0.1 CPUs per execution), as well as limits on memory and execution time. 
When the container hits either the memory threshold or the maximum time allowed, it is automatically terminated, 
and a user-facing error message is generated to explain the termination cause.
</p>

For the documentation visit the swagger page at the following url : http://<IP:PORT>/swagger-ui.html

![Compiler swagger doc](images/swagger.png?raw=true "compiler swagger doc")

### Verdicts

Here is a list of Verdicts that can be returned by the compiler:

:tada: **Accepted**
```json
{
    "verdict": "Accepted",
    "statusCode": 100,
    "error": "",
    "testCasesResult": {
      "test1": {
        "verdict": "Accepted",
        "verdictStatusCode": 100,
        "output": "0 1 2 3 4 5 6 7 8 9",
        "error": "", 
        "expectedOutput": "0 1 2 3 4 5 6 7 8 9",
        "executionDuration": 175
      },
      "test2": {
        "verdict": "Accepted",
        "verdictStatusCode": 100,
        "output": "9 8 7 1",
        "error": "" ,
        "expectedOutput": "9 8 7 1",
        "executionDuration": 273
      },
      ...
    },
    "compilationDuration": 328,
    "averageExecutionDuration": 183,
    "timeLimit": 1500,
    "memoryLimit": 500,
    "language": "JAVA",
    "dateTime": "2022-01-28T23:32:02.843465"
}
```

:x: **Wrong Answer**

```json
{
    "verdict": "Wrong Answer",
    "statusCode": 200,
    "error": "",
    "testCasesResult": {
      "test1": {
        "verdict": "Accepted",
        "verdictStatusCode": 100,
        "output": "0 1 2 3 4 5 6 7 8 9",
        "error": "", 
        "expectedOutput": "0 1 2 3 4 5 6 7 8 9",
        "executionDuration": 175
      },
      "test2": {
        "verdict": "Wrong Answer",
        "verdictStatusCode": 200,
        "output": "9 8 7 1",
        "error": "" ,
        "expectedOutput": "9 8 6 1",
        "executionDuration": 273
      }
    },
    "compilationDuration": 328,
    "averageExecutionDuration": 183,
    "timeLimit": 1500,
    "memoryLimit": 500,
    "language": "JAVA",
    "dateTime": "2022-01-28T23:32:02.843465"
}
```

:shit: **Compilation Error**

```json
{
  "verdict": "Compilation Error",
   "statusCode": 300,
   "error": "# command-line-arguments\n./main.go:5:10: undefined: i\n./main.go:6:21: undefined: i\n./main.go:7:9: undefined: i\n",
   "testCasesResult": {},
   "compilationDuration": 118,
   "averageExecutionDuration": 0,
   "timeLimit": 1500,
   "memoryLimit": 500,
   "language": "GO",
   "dateTime": "2022-01-28T23:32:02.843465"
}
```

:clock130: **Time Limit Exceeded** 
```json
{
    "verdict": "Time Limit Exceeded",
    "statusCode": 500,
    "error": "Execution exceeded 15sec",
    "testCasesResult": {
      "test1": {
        "verdict": "Accepted",
        "verdictStatusCode": 100,
        "output": "0 1 2 3 4 5 6 7 8 9",
        "error": "", 
        "expectedOutput": "0 1 2 3 4 5 6 7 8 9",
        "executionDuration": 175
      },
      "test2": {
        "verdict": "Time Limit Exceeded",
        "verdictStatusCode": 500,
        "output": "",
        "error": "Execution exceeded 15sec" ,
        "expectedOutput": "9 8 7 1",
        "executionDuration": 1501
      }
    },
    "compilationDuration": 328,
    "averageExecutionDuration": 838,
    "timeLimit": 1500,
    "memoryLimit": 500,
    "language": "JAVA",
    "dateTime": "2022-01-28T23:32:02.843465"
}
```

:boom: **Runtime Error** 
```json
{
    "verdict": "Runtime Error",
    "statusCode": 600,
    "error": "panic: runtime error: integer divide by zero\n\ngoroutine 1 [running]:\nmain.main()\n\t/app/main.go:11 +0x9b\n",
    "testCasesResult": {
      "test1": {
        "verdict": "Accepted",
        "verdictStatusCode": 100,
        "output": "0 1 2 3 4 5 6 7 8 9",
        "error": "", 
        "expectedOutput": "0 1 2 3 4 5 6 7 8 9",
        "executionDuration": 175
      },
      "test2": {
        "verdict": "Runtime Error",
        "verdictStatusCode": 600,
        "output": "",
        "error": "panic: runtime error: integer divide by zero\n\ngoroutine 1 [running]:\nmain.main()\n\t/app/main.go:11 +0x9b\n" ,
        "expectedOutput": "9 8 7 1",
        "executionDuration": 0
      }
    },
    "compilationDuration": 328,
    "averageExecutionDuration": 175,
    "timeLimit": 1500,
    "memoryLimit": 500,
    "language": "GO",
    "dateTime": "2022-01-28T23:32:02.843465"
}
```

:minidisc: **Out Of Memory** 
```json
{
    "verdict": "Out Of Memory",
    "statusCode": 400,
    "error": "fatal error: runtime: out of memory\n\nruntime stack:\nruntime.throw({0x497d72?, 0x17487800000?})\n\t/usr/local/go/src/runtime/panic.go:992 +0x71\nruntime.sysMap(0xc000400000, 0x7ffccb36b0d0?, 0x7ffccb36b13...",
    "testCasesResult": {
      "test1": {
        "verdict": "Accepted",
        "verdictStatusCode": 100,
        "output": "0 1 2 3 4 5 6 7 8 9",
        "error": "", 
        "expectedOutput": "0 1 2 3 4 5 6 7 8 9",
        "executionDuration": 175
      },
      "test2": {
        "verdict": "Out Of Memory",
        "verdictStatusCode": 400,
        "output": "",
        "error": "fatal error: runtime: out of memory\n\nruntime stack:\nruntime.throw({0x497d72?, 0x17487800000?})\n\t/usr/local/go/src/runtime/panic.go:992 +0x71\nruntime.sysMap(0xc000400000, 0x7ffccb36b0d0?, 0x7ffccb36b13..." ,
        "expectedOutput": "9 8 7 1",
        "executionDuration": 0
      }
    },
    "compilationDuration": 328,
    "averageExecutionDuration": 175,
    "timeLimit": 1500,
    "memoryLimit": 500,
    "language": "GO",
    "dateTime": "2022-01-28T23:32:02.843465"
}
```

### Visualize Docker images and containers infos
It is also possible to visualize information about the images and docker containers that are currently running using these endpoints

![Docker info](images/swagger-docker-infos.png?raw=true "Docker info Swagger")

## Stream processing with Kafka Streams

![remote code compiler kafka mode](images/kafka-streams.png?raw=true "Compiler Kafka Mode")

You can use the compiler with an event driven architecture.
To enable kafka mode you should pass to the container the following env variables :
* **ENABLE_KAFKA_MODE** : True or False
* **KAFKA_INPUT_TOPIC** : Input topic, json request
* **KAFKA_OUTPUT_TOPIC** : Output topic, json response
* **KAFKA_CONSUMER_GROUP_ID** : Consumer group
* **KAFKA_HOSTS** : List of brokers
* **CLUSTER_API_KEY** : API key
* **CLUSTER_API_SECRET** : API Secret
* **KAFKA_THROTTLING_DURATION** : Throttling duration, by default set to 10000ms (when number of docker containers running reach MAX_REQUESTS, this value is used to do not lose the request and retry after this duration)

> Note:  
> Having More partitions => More Parallelism => Better performance

```shell
docker container run -p 8080:8082 -v /var/run/docker.sock:/var/run/docker.sock -e DELETE_DOCKER_IMAGE=true -e EXECUTION_MEMORY_MAX=10000 -e EXECUTION_MEMORY_MIN=0 -e EXECUTION_TIME_MAX=15 -e EXECUTION_TIME_MIN=0 -e ENABLE_KAFKA_MODE=true -e KAFKA_INPUT_TOPIC=topic.input -e KAFKA_OUTPUT_TOPIC=topic.output -e KAFKA_CONSUMER_GROUP_ID=compilerId -e KAFKA_HOSTS=ip_broker1,ip_broker2,ip_broker3 -e API_KEY=YOUR_API_KEY -e API_SECRET=YOUR_API_SECRET -t compiler
```

## Queueing system with RabbitMq

![remote code compiler rabbitMq mode](images/rabbitMq.png?raw=true "Compiler rabbitMq Mode")

To enable Rabbit MQ mode you should pass to the container the following env variables :
* **ENABLE_RABBITMQ_MODE** : True or False
* **RABBIT_QUEUE_INPUT** : Input queue, json request
* **RABBIT_QUEUE_OUTPUT** : Output queue, json response
* **RABBIT_USERNAME** : Rabbit MQ username  
* **RABBIT_PASSWORD** : Rabbit MQ password
* **RABBIT_HOSTS** : List of brokers
* **RABBIT_THROTTLING_DURATION** : Throttling duration, by default set to 10000ms (when number of docker containers running reach MAX_REQUESTS, this value is used to do not lose the request and retry after this duration)

```shell
docker container run -p 8080:8082 -v /var/run/docker.sock:/var/run/docker.sock -e DELETE_DOCKER_IMAGE=true -e EXECUTION_MEMORY_MAX=10000 -e EXECUTION_MEMORY_MIN=0 -e EXECUTION_TIME_MAX=15 -e EXECUTION_TIME_MIN=0 -e ENABLE_RABBITMQ_MODE=true -e RABBIT_QUEUE_INPUT=queue.input -e RABBIT_QUEUE_OUTPUT=queue.output -e RABBIT_USERNAME=username -e RABBIT_PASSWORD=password -e RABBIT_HOSTS=ip_broker1,ip_broker2,ip_broker3 -t compiler
```

## Monitoring

<p align="center">
<img height="100px" width="100px" src="https://upload.wikimedia.org/wikipedia/commons/thumb/3/38/Prometheus_software_logo.svg/1200px-Prometheus_software_logo.svg.png" alt="prometheus logo"/>
</p>

Check out exposed prometheus metrics using the following url : http://<IP:PORT>/prometheus

![Java execution counter](images/executions_metrics.png?raw=true "Executions counter")

![Parallel executions](images/parallel-executions-metrics.png?raw=true "Parallel Executions Metrics")

![Throttling counter](images/throttling-counter-metrics.png?raw=true "Throttling Counter Metrics")

Other metrics are available.

## Logging

<p>By default, only console logging is enabled.</p>

You can store logs in a file and access to it using /logfile endpoint by setting the environment variable **ROLLING_FILE_LOGGING** to true. 
All logs will be kept for 7 days with a maximum size of 1 GB.

<p align="center">
<img height="100px" width="100px" src="https://iconape.com/wp-content/png_logo_vector/elastic-logstash.png" alt="logstash logo"/>
</p>

You can also send logs to logstash pipeline by setting these environment variables **LOGSTASH_LOGGING** to true and 
**LOGSTASH_SERVER_HOST**, **LOGSTASH_SERVER_PORT** to logstash and port values respectively. 

## Author

- **Zakaria Maaraki** - _Initial work_ - [zakariamaaraki](https://github.com/zakariamaaraki)
