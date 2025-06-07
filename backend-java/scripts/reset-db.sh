#!/bin/bash

SCRIPT_DIR=$(dirname "$(readlink -f "$0")")
PROJECT_ROOT=$(dirname "$SCRIPT_DIR")

echo "===== Resetando o banco de dados ====="

# Verificar se é modo forçado
if [ "$1" == "--force" ] || [ "$1" == "-f" ]; then
  FORCE_RESET=true
else
  FORCE_RESET=false
fi

# Verificar se o usuário tem certeza
if [ "$FORCE_RESET" != "true" ]; then
  read -p "Isso apagará TODOS os dados do banco. Tem certeza? (s/N) " CONFIRM
  if [[ ! "$CONFIRM" =~ ^[sSyY]$ ]]; then
    echo "Operação cancelada pelo usuário."
    exit 0
  fi
fi

# Verificar se o banco de dados está em execução
DB_CONTAINER=$(docker ps -q -f "name=app-core-db")
if [ -z "$DB_CONTAINER" ]; then
  echo "-> O banco de dados não está em execução. Iniciando..."
  cd "$PROJECT_ROOT/docker"
  docker-compose up -d db
  sleep 10
  DB_CONTAINER=$(docker ps -q -f "name=app-core-db")
fi

echo "-> Dropando e recriando o banco de dados..."
docker exec $DB_CONTAINER psql -U postgres -c "DROP DATABASE IF EXISTS fastapp;"
docker exec $DB_CONTAINER psql -U postgres -c "CREATE DATABASE fastapp;"

# Aplicar migrações usando o script existente
echo "-> Aplicando migrações..."
"$SCRIPT_DIR/apply-migrations.sh"

if [ $? -eq 0 ]; then
  echo "===== Banco de dados resetado com sucesso! ====="
  echo "Todas as tabelas foram recriadas e os dados iniciais foram inseridos."
  echo ""
  echo "Para acessar o banco via Adminer: http://localhost:8081"
  echo "  Sistema: PostgreSQL"
  echo "  Servidor: $DB_HOST"
  echo "  Usuário: postgres"
  echo "  Senha: postgres"
  echo "  Banco de dados: fastapp"
else
  echo "ERRO: Falha ao aplicar migrações após o reset."
  echo "Verifique os logs para mais detalhes."
  exit 1
fi 