#!/bin/bash

SCRIPT_DIR=$(dirname "$(readlink -f "$0")")
PROJECT_ROOT=$(dirname "$SCRIPT_DIR")

echo "===== Iniciando infraestrutura ====="

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

# Verificar se já existem containers rodando
RUNNING_CONTAINERS=$(docker ps --filter "name=app-core" --format "{{.Names}}")

if [ -n "$RUNNING_CONTAINERS" ]; then
  echo "AVISO: Existem containers da aplicação em execução:"
  echo "$RUNNING_CONTAINERS"
  
  if [ "$1" != "--force" ]; then
    read -p "Deseja parar os containers existentes e iniciar novamente? (s/n): " resposta
    if [[ ! "$resposta" =~ ^[Ss]$ ]]; then
      echo "Operação cancelada."
      exit 0
    fi
  fi
  
  echo "-> Parando containers existentes..."
  docker-compose down
fi

# Iniciar serviços principais primeiro
echo "-> Iniciando principais serviços (DB)..."
docker-compose up -d db

# Verificar se o PostgreSQL está rodando
echo "-> Verificando status do PostgreSQL..."
MAX_RETRIES=30
RETRY_COUNT=0

while [ $RETRY_COUNT -lt $MAX_RETRIES ]; do
    if docker-compose exec db pg_isready -q; then
        echo "-> PostgreSQL está pronto!"
        break
    fi
    
    RETRY_COUNT=$((RETRY_COUNT+1))
    echo "Aguardando PostgreSQL inicializar... ($RETRY_COUNT/$MAX_RETRIES)"
    sleep 2
done

if [ $RETRY_COUNT -eq $MAX_RETRIES ]; then
    echo "AVISO: Tempo limite excedido aguardando o PostgreSQL inicializar"
fi

# Agora que os serviços principais estão prontos, iniciar o Adminer
echo "-> Iniciando serviços adicionais (Adminer)..."
docker-compose up -d adminer

echo "===== Infraestrutura iniciada com sucesso! ====="
echo "Serviços disponíveis:"
echo "- PostgreSQL: localhost:5432"
echo "- Adminer (gerenciador de BD): http://localhost:8081"
echo "  Sistema: PostgreSQL"
echo "  Servidor: db"
echo "  Usuário: postgres"
echo "  Senha: postgres"
echo "  Banco de dados: fastapp"