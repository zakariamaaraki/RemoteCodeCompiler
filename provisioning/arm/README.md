# Azure Resource Management

## Provisioning of an AKS cluster
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

3 - update **servicePrincipalProfile** section in **aks.json** file and specify the following values:
* Service principal client ID is your appId.
* Service principal client secret is the password value.
</P>

4 - update the ssh publicKey in **aks.json** file (put yours)

5 - run the deployment.sh script

```shell
./deployment.sh
```
