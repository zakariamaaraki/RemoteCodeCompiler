# Azure Resource Management

## Provisioning of ACR and AKS cluster

<p align="center">
<img height="150px" width="150px" src="https://www.turbonomic.com/wp-content/uploads/2020/11/Azure-Kubernetes.png" alt="aks logo" />
<img height="150px" width="250px" src ="https://sysadminas.eu/assets/images/post16/ACR.png" alt="Acr logo" />
</p>


<p>Before you begin you should create an Azure AD service principal, you must have permissions to register an application with your Azure AD tenant, and to assign the application to a role in your subscription.</p>

<P>
1 - Login to your account

```shell
az login
```

2 - Create a service principal

```shell
az ad sp create-for-rbac --name <AKSClusterServicePrincipal>
```

you'll get as an output something that looks like this:
```json
{
  "appId": "<appId>",
  "displayName": "AKSClusterServicePrincipal",
  "name": "http://AKSClusterServicePrincipal",
  "password": "<password>",
  "tenant": "<yourTenant>"
}
```

3 - Update **servicePrincipalProfile** section in **deployment.sh** file and specify the following values:
* Service principal client ID is your appId.
* Service principal client secret is the password value.
</P>

4 - Update the ssh publicKey in **deployment.sh** file (put yours)

5 - Run the deployment.sh script

```shell
./deployment.sh
```

6 - Connect to ACR using the following command

```shell
az acr login --name remotecodecompileracr
```

7 - Connect to AKS cluster using the following command

```shell
az aks get-credentials --resource-group remote-code-compiler --name remote-code-compiler-aks
```
