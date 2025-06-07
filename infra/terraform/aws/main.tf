provider "aws" {
  region = var.aws_region
}

# Exemplo de recurso: um bucket S3 simples
resource "aws_s3_bucket" "example_bucket" {
  bucket = "meu-hackathon-bucket-exemplo-${random_id.bucket_suffix.hex}" # Nome do bucket deve ser único globalmente
  acl    = "private"

  tags = {
    Name        = "Meu Bucket Exemplo"
    Environment = "Hackathon"
  }
}

# Para garantir nomes de bucket únicos
resource "random_id" "bucket_suffix" {
  byte_length = 8
}
