output "workshop_acr_id" {
  value = azurerm_container_registry.acr.id
}
output "workshop_acr_url" {
  value = azurerm_container_registry.acr.login_server
}