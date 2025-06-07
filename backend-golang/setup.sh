#!/bin/bash

# Função para verificar se um comando existe
command_exists() {
  command -v "$1" >/dev/null 2>&1
}

# Verifica se o Go está instalado
if command_exists go; then
  echo "Go já está instalado."
  go version
else
  echo "Go não encontrado. Tentando instalar..."
  OS_TYPE=$(uname -s)
  if [ "$OS_TYPE" = "Darwin" ]; then
    echo "Sistema macOS detectado. Tentando instalar via Homebrew..."
    if command_exists brew; then
      brew install go
    else
      echo "Homebrew não encontrado. Por favor, instale o Homebrew primeiro (https://brew.sh/) ou instale o Go manualmente."
      exit 1
    fi
  elif [ "$OS_TYPE" = "Linux" ]; then
    echo "Sistema Linux detectado."
    if command_exists apt-get; then
      echo "Tentando instalar via apt-get..."
      sudo apt-get update
      sudo apt-get install -y golang-go
    elif command_exists yum; then
      echo "Tentando instalar via yum..."
      sudo yum install -y golang
    elif command_exists dnf; then
      echo "Tentando instalar via dnf..."
      sudo dnf install -y golang
    else
      echo "Gerenciador de pacotes (apt-get, yum, dnf) não encontrado. Por favor, instale o Go manualmente."
      exit 1
    fi
  else
    echo "Sistema operacional não suportado para instalação automática do Go ($OS_TYPE). Por favor, instale o Go manualmente."
    exit 1
  fi

  # Verifica novamente se o Go foi instalado
  if command_exists go; then
    echo "Go instalado com sucesso."
    go version
  else
    echo "Falha ao instalar o Go. Por favor, verifique os erros acima ou instale o Go manualmente."
    exit 1
  fi
fi

echo ""
echo "Baixando dependências do Go..."
if [ -f go.mod ]; then
  go mod tidy
  go mod download
  echo "Dependências baixadas com sucesso."
else
  echo "go.mod não encontrado. Certifique-se de que está no diretório correto do projeto Go."
  exit 1
fi

echo ""
echo "Configuração inicial concluída."
# A geração do Swagger e o início da aplicação agora são feitos via Makefile.
# Este script foca na configuração do ambiente (Go, Swag CLI).

echo ""
echo "Verificando/Instalando Swag CLI..."
# Certifique-se de que o Swag CLI está instalado: go install github.com/swaggo/swag/cmd/swag@latest
if ! command_exists swag; then
  echo "Comando swag não encontrado. Tentando instalar..."
  go install github.com/swaggo/swag/cmd/swag@latest
  # Adiciona o diretório bin do Go ao PATH para a sessão atual do script
  export PATH=$(go env GOPATH)/bin:$PATH
  if ! command_exists swag; then
    echo "Falha ao instalar o swag. Por favor, instale-o manualmente: go install github.com/swaggo/swag/cmd/swag@latest"
    exit 1
  fi
  echo "Swag CLI instalado com sucesso."
else
  echo "Swag CLI já está instalado."
fi

echo ""
echo "Setup do ambiente básico concluído."
echo "Use o Makefile para construir, rodar, gerar swagger, etc."
echo "Exemplos:"
echo "  make swagger"
	# echo "  make run"

# A linha de "go run cmd/server/main.go" foi removida, pois o Makefile controla isso.
 