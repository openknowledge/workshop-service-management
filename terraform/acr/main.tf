resource "azurerm_container_registry" "acr" {
  name                = "workshopacr${var.workshop_name}"
  location            = var.location
  resource_group_name = var.resource_group_name
  sku                 = "Basic"
  admin_enabled       = false
}