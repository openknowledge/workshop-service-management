resource "azurerm_kubernetes_cluster" "workshop_aks" {
  name                = "workshop-cluster-${var.aks_name_suffix}"
  location            = var.location
  resource_group_name = var.resource_group_name
  dns_prefix          = var.aks_name_suffix

  web_app_routing {
    dns_zone_id = ""
  }

  default_node_pool {
    name       = "default"
    node_count = 2
    vm_size    = var.vm_size
    max_pods = 100
  }

  identity {
    type = "SystemAssigned"
  }
}

#resource "azurerm_kubernetes_cluster_node_pool" "uit_workshop_nodepool" {
#  kubernetes_cluster_id = azurerm_kubernetes_cluster.uit_workshop_aks.id
#  name                  = "uit-workshop-nodepool"
#  vm_size               = "Standard B2ms"
#  node_count = 2
#  max_pods = 100
#}

resource "azurerm_role_assignment" "uit_workshop_aks_acr" {
  principal_id                     = azurerm_kubernetes_cluster.workshop_aks.kubelet_identity[0].object_id
  role_definition_name             = "AcrPull"
  scope                            = var.acr_id
  skip_service_principal_aad_check = true
}