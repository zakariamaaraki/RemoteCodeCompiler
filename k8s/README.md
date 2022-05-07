# K8s

We provide you with a helm chart that will let you run the compiler in a distributed way on K8s.

```shell
helm install compiler ./compiler
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

![monitoring](../images/prometheus-grafana.png?raw=true "monitoring")

We also provide you with a helm chart to monitor the compiler using **Prometheus** and **Grafana** on k8s.

```shell
helm install monitoring ./monitoring
```
