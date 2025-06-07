#!/bin/bash

SCRIPT_DIR=$(dirname "$(readlink -f "$0")")
PROJECT_ROOT=$(dirname "$SCRIPT_DIR")

echo "===== Aplicando migrações Liquibase ====="

# Navegar para o diretório do projeto
cd "$PROJECT_ROOT"

# Verificar se Docker está rodando
if ! docker info >/dev/null 2>&1; then
    echo "ERRO: Docker não está rodando. Por favor, inicie o Docker antes de continuar."
    exit 1
fi

# Verificar se o banco de dados está rodando
DB_CONTAINER=$(docker ps -q -f "name=app-core-db")
if [ -z "$DB_CONTAINER" ]; then
    echo "AVISO: Container do banco de dados não encontrado. Iniciando..."
    cd "$PROJECT_ROOT/docker"
    docker-compose up -d db
    sleep 10  # Aguardar banco inicializar
    DB_CONTAINER=$(docker ps -q -f "name=app-core-db")
    if [ -z "$DB_CONTAINER" ]; then
        echo "ERRO: Falha ao iniciar o banco de dados. Verifique os logs: docker-compose logs db"
        exit 1
    fi
    cd "$PROJECT_ROOT"
fi

# Compilar o projeto para extrair dependências
echo "-> Compilando o projeto para extrair dependências..."
./gradlew build -x test

# Detectar a rede do container do banco de dados
NETWORK=$(docker inspect $DB_CONTAINER -f '{{range $k, $v := .NetworkSettings.Networks}}{{$k}}{{end}}')
DB_HOST=$(docker inspect $DB_CONTAINER --format '{{ .Name }}' | sed 's|^/||')

echo "-> Aplicando migrações Liquibase..."
docker run --rm \
  --network=$NETWORK \
  -v $(pwd)/src/main/resources:/liquibase/changelog \
  -v $(pwd)/build/dependencies:/liquibase/lib \
  -e TZ=America/Sao_Paulo \
  liquibase/liquibase:4.25.1 \
  --driver=org.postgresql.Driver \
  --classpath=/liquibase/changelog:/liquibase/lib/postgresql.jar \
  --url="jdbc:postgresql://$DB_HOST:5432/fastapp" \
  --username=postgres \
  --password=postgres \
  --changeLogFile=db/changelog/db.changelog-master.yaml \
  update

if [ $? -ne 0 ]; then
    echo "ERRO: Falha ao aplicar migrações Liquibase."
    exit 1
fi

echo "-> Obtendo status do Liquibase..."
docker run --rm \
  --network=$NETWORK \
  -v $(pwd)/src/main/resources:/liquibase/changelog \
  -v $(pwd)/build/dependencies:/liquibase/lib \
  -e TZ=America/Sao_Paulo \
  liquibase/liquibase:4.25.1 \
  --driver=org.postgresql.Driver \
  --classpath=/liquibase/changelog:/liquibase/lib/postgresql.jar \
  --url="jdbc:postgresql://$DB_HOST:5432/fastapp" \
  --username=postgres \
  --password=postgres \
  --changeLogFile=db/changelog/db.changelog-master.yaml \
  status

echo "===== Migrações aplicadas com sucesso! ====="
echo "Banco de dados atualizado com todas as migrações."
echo ""
echo "Para acessar o banco via Adminer: http://localhost:8081"
echo "  Sistema: PostgreSQL"
echo "  Servidor: $DB_HOST"
echo "  Usuário: postgres"
echo "  Senha: postgres"
echo "  Banco de dados: fastapp"