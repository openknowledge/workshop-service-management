resource "azurerm_resource_group" "workshop" {
  name     = "workshop-rg-api-management-${var.workshop_name}"
  location = "West Europe"
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
  vm_size = "Standard_B2ms"

  count = 10
}


### New Setup

#resource "azurerm_resource_group" "workshop_west_europe" {
#  name     = "workshop-rg-api-management-${var.workshop_name}"
#  location = "West Germany Central"
#}
#
#resource "azurerm_resource_group" "workshop_west_germany" {
#  name     = "workshop-rg-api-management-${var.workshop_name}"
#  location = "West Europe"
#}
#
#module "aks" {
#  source = "./aks"
#  aks_name_suffix = "${var.workshop_name}-${count.index}"
#  location = azurerm_resource_group.workshop_west_europe.location
#  resource_group_name = azurerm_resource_group.workshop_west_europe.name
#  acr_id = module.acr.workshop_acr_id
#  vm_size = "Standard_B2ms"
#
#  count = 2
#}
#
#module "aks" {
#  source = "./aks"
#  aks_name_suffix = "${var.workshop_name}-${count.index}"
#  location = azurerm_resource_group.workshop_west_europe.location
#  resource_group_name = azurerm_resource_group.workshop_west_europe.name
#  acr_id = module.acr.workshop_acr_id
#  vm_size = "Standard_D2_v2"
#
#  count = 2
#}
#
#module "aks" {
#  source = "./aks"
#  aks_name_suffix = "${var.workshop_name}-${count.index}"
#  location = azurerm_resource_group.workshop_west_germany.location
#  resource_group_name = azurerm_resource_group.workshop_west_germany.name
#  acr_id = module.acr.workshop_acr_id
#  vm_size = "Standard_B2ms"
#
#  count = 2
#}
#
#module "aks" {
#  source = "./aks"
#  aks_name_suffix = "${var.workshop_name}-${count.index}"
#  location = azurerm_resource_group.workshop_west_germany.location
#  resource_group_name = azurerm_resource_group.workshop_west_germany.name
#  acr_id = module.acr.workshop_acr_id
#  vm_size = "Standard_D2_v2"
#
#  count = 2
#}
