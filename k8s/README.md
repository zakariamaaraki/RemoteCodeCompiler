<p align="center">
<img height="150px" width="150px" src="https://upload.wikimedia.org/wikipedia/commons/thumb/3/39/Kubernetes_logo_without_workmark.svg/1200px-Kubernetes_logo_without_workmark.svg.png" alt="k8s logo"/>
</p>

# K8s Helm charts
For provisioning see [ARM provisioning folder](/provisioning/arm).\
We provide you with a helm chart that will let you run the compiler in a distributed way on K8s.

```shell
helm install compiler ./compiler
```

### With Kafka enabled
```shell
helm install -f compiler/values.yaml -f compiler/kafka-values.yaml  compiler ./compiler
```

### With RabbitMq enabled
```shell
helm install -f compiler/values.yaml -f compiler/rabbitmq-values.yaml  compiler ./compiler
```

### Minikube
Note if you are running k8s using Minikube :
* you can reuse the Docker daemon from Minikube by running the following command: 
```shell
eval $(minikube docker-env)
```
* set image pull policy to **Never** in the values.yaml file.
* Get the url using the following command:  
```shell
minikube service --url compiler
```

## Monitoring 


<p align="center">
<img src="https://www.mytinydc.com/images/blog/blog-prometheus+grafana.png" alt="prometheus and grafana logo"/>
</p>

We also provide you with a helm chart to monitor the compiler using **Prometheus** and **Grafana** on k8s.

```shell
helm install monitoring ./monitoring
```

* Get prometheus service url:
```shell
minikube service --url monitoring-prometheus
```

* Get grafana service url:
```shell
minikube service --url monitoring-grafana
```
