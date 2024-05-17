output "nameservers" {
  value = azurerm_dns_zone.azure_cloud_ok_services.name_servers
}