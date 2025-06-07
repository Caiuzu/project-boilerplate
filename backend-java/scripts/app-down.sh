#!/bin/bash

SCRIPT_DIR=$(dirname "$(readlink -f "$0")")
PROJECT_ROOT=$(dirname "$SCRIPT_DIR")

echo "===== Parando a aplicação ====="

# Verificar se o Docker está rodando
check_docker() {
  docker info &>/dev/null
  return $?
}

if ! check_docker; then
  echo "ERRO: O Docker não está rodando."
  echo "Por favor, inicie o Docker Desktop e tente novamente."
  exit 1
fi

# Navegar para o diretório docker
cd "$PROJECT_ROOT/docker"

# Verificar se a aplicação está rodando
APP_CONTAINER=$(docker ps -q -f "name=app-core-api")
if [ -z "$APP_CONTAINER" ]; then
  echo "Nenhum container da aplicação em execução."
  exit 0
fi

# Parar o container da aplicação
echo "-> Parando o container da aplicação..."
docker-compose stop app

# Verificar se o container foi parado
STILL_RUNNING=$(docker ps -q -f "name=app-core-api")
if [ -z "$STILL_RUNNING" ]; then
  echo "===== Aplicação parada com sucesso! ====="
else
  echo "AVISO: O container ainda está em execução."
  echo "Forçando a parada do container..."
  docker rm -f "$STILL_RUNNING"
  
  # Verificação final
  STILL_RUNNING=$(docker ps -q -f "name=app-core-api")
  if [ -z "$STILL_RUNNING" ]; then
    echo "===== Aplicação parada com sucesso! ====="
  else
    echo "ERRO: Não foi possível parar o container."
    echo "Por favor, verifique e pare manualmente com: docker ps"
  fi
fi 