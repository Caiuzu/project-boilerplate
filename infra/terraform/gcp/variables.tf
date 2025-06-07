variable "gcp_project_id" {
  description = "ID do projeto GCP"
  type        = string
  # default     = "seu-gcp-project-id" # Adicione seu ID de projeto aqui
}

variable "gcp_region" {
  description = "Região GCP para provisionar os recursos"
  type        = string
  default     = "us-central1"
} 