provider "google" {
  project = var.gcp_project_id
  region  = var.gcp_region
}

# Exemplo de recurso: um bucket no Google Cloud Storage
resource "google_storage_bucket" "example_bucket" {
  name                        = "meu-hackathon-bucket-gcp-exemplo-${random_id.bucket_suffix.hex}" # Nome do bucket deve ser único globalmente
  location                    = var.gcp_region
  uniform_bucket_level_access = true

  labels = {
    environment = "hackathon"
  }
}

# Para garantir nomes de bucket únicos
resource "random_id" "bucket_suffix" {
  byte_length = 8
} 