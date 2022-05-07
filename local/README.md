# Local execution of the compiler

**Note**: running all these dependencies is not mandatory, the compiler is made to run in a standalone mode.

The provided docker-compose file contains these dependencies: 

* Grafana and Prometheus for monitoring
* Portainer to have a total view of running containers
* ZooKeeper and Kafka for the integration with Kafka.
* RabbitMQ for the integration with RabbitMQ.

```shell
docker-compose up --build
```

This docker-compose should be used only for development / testing purpose.