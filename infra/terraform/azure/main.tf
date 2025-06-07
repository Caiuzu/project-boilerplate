provider "azurerm" {
  features {}
}

# Exemplo de recurso: um Resource Group
resource "azurerm_resource_group" "example_rg" {
  name     = "hackathon-exemplo-rg"
  location = var.azure_location

  tags = {
    environment = "hackathon"
  }
}

# Exemplo de recurso: uma Storage Account
resource "azurerm_storage_account" "example_sa" {
  name                     = "hackathonstorageacc${random_id.sa_suffix.hex}" # Nome da conta de armazenamento deve ser único globalmente e ter entre 3-24 caracteres alfanuméricos minúsculos
  resource_group_name      = azurerm_resource_group.example_rg.name
  location                 = azurerm_resource_group.example_rg.location
  account_tier             = "Standard"
  account_replication_type = "LRS"

  tags = {
    environment = "hackathon"
  }
}

# Para garantir nomes de conta de armazenamento únicos
resource "random_id" "sa_suffix" {
  byte_length = 8
} 