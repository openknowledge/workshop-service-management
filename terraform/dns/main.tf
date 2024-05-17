resource "azurerm_dns_zone" "azure_cloud_ok_services" {
  name                = "azure.cloud.openknowledge.services"
  resource_group_name = var.resource_group_name
}

