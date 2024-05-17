output "acr" {
  value = {
    id = module.acr.workshop_acr_id
    url = module.acr.workshop_acr_url
  }
}

output "dns" {
  value = {
    name_servers = module.dns.nameservers
  }
}