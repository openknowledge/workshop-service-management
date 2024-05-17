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

resource "azurerm_role_assignment" "workshop_aks_acr" {
  principal_id                     = azurerm_kubernetes_cluster.workshop_aks.kubelet_identity[0].object_id
  role_definition_name             = "AcrPull"
  scope                            = var.acr_id
  skip_service_principal_aad_check = true
}