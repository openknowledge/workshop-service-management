resource "azurerm_resource_group" "workshop" {
  name     = "workshop-rg-${var.workshop_name}"
  location = "Germany West Central"
}

module "acr" {
  source = "./acr"
  resource_group_name = azurerm_resource_group.workshop.name
  location = azurerm_resource_group.workshop.location
  workshop_name = var.workshop_name
}

module "aks" {
  source = "./aks"
  aks_name_suffix = "${var.workshop_name}-${count.index}"
  location = azurerm_resource_group.workshop.location
  resource_group_name = azurerm_resource_group.workshop.name
  acr_id = module.acr.workshop_acr_id

  count = 2
}