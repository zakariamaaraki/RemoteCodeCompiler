resourceGroupName="remote-code-compiler";

az group create -l westus --resource-group $resourceGroupName;

az deployment group create --resource-group $resourceGroupName --template-file ./aks.json

