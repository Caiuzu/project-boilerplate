#!/bin/bash

SCRIPT_DIR=$(dirname "$(readlink -f "$0")")
PROJECT_ROOT=$(dirname "$SCRIPT_DIR")

echo "===== Parando infraestrutura ====="

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

# Verificar quais contêineres de infraestrutura estão rodando
INFRA_CONTAINERS=$(docker ps --filter "name=app-core-(db)" --format "{{.Names}}")

if [ -z "$INFRA_CONTAINERS" ]; then
  echo "Nenhum contêiner de infraestrutura em execução."
  exit 0
fi

echo "Contêineres de infraestrutura em execução:"
echo "$INFRA_CONTAINERS"
echo

# Parar os contêineres de infraestrutura
echo "-> Parando serviços de infraestrutura..."
docker-compose stop db

# Verificar se todos os contêineres foram parados
STILL_RUNNING=$(docker ps --filter "name=app-core-(db)" --format "{{.Names}}")
if [ -z "$STILL_RUNNING" ]; then
  echo "===== Infraestrutura parada com sucesso! ====="
else
  echo "AVISO: Alguns contêineres ainda estão em execução:"
  echo "$STILL_RUNNING"
  echo
  echo "Forçando a parada de todos os contêineres..."
  docker-compose down
  
  # Verificação final
  STILL_RUNNING=$(docker ps --filter "name=app-core-(db)" --format "{{.Names}}")
  if [ -z "$STILL_RUNNING" ]; then
    echo "===== Infraestrutura parada com sucesso! ====="
  else
    echo "ERRO: Não foi possível parar todos os contêineres."
    echo "Por favor, verifique e pare-os manualmente com: docker ps"
  fi
fi 