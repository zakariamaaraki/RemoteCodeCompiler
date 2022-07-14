resourceGroupName="remote-code-compiler";
AcrName="remotecodecompileracr"; # Connected registry name must use only lowercase, and follow this pattern '^[a-zA-Z0-9]*$'.
AksClusterName="remote-code-compiler-aks";
sshKey="public ssh key"
principalClientId="principalClientId"
principalClientSecret="principalClientSecret"
nbVmNodes=1
vmSize="Standard_A2_v2"

# Create resource group
az group create -l westus --resource-group $resourceGroupName;

# Provision Aks Cluster using ARM template
az deployment group create --resource-group $resourceGroupName --template-file ./aks.json --parameters \
clusterName=$AksClusterName \
publicSshKey="$sshKey" \
principalClientId=$principalClientId \
principalClientSecret=$principalClientSecret \
nbVmNodes=$nbVmNodes \
vmSize=$vmSize

# Run the following line to create an Azure Container Registry if you do not already have one
az acr create -n $AcrName -g $resourceGroupName --sku basic

# Configure ACR integration for the AKS cluster
az aks update -n $AksClusterName -g $resourceGroupName --attach-acr $AcrName

