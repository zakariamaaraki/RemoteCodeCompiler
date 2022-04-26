# Local execution of the compiler

The provided docker-compose file contains these dependencies : 
* Grafana and Prometheus for monitoring
* Portainer to have a total view of running containers
* ZooKeeper and Kafka for the integration with Kafka.

```shell
docker-compose up --build
```