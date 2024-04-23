output "kube_config" {
  value = azurerm_kubernetes_cluster.workshop_aks.kube_admin_config_raw

  sensitive = true
}

output "client_certificate" {
  value     = azurerm_kubernetes_cluster.workshop_aks.kube_config[0].client_certificate
  sensitive = true
}