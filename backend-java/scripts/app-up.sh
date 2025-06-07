#!/bin/bash

SCRIPT_DIR=$(dirname "$(readlink -f "$0")")
PROJECT_ROOT=$(dirname "$SCRIPT_DIR")
APP_PORT=8083

echo "===== Iniciando a aplicação no container Docker ====="

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

# Verificar se a porta já está em uso
check_port() {
  if command -v lsof >/dev/null 2>&1; then
    lsof -i :"$1" >/dev/null 2>&1
    return $?
  elif command -v netstat >/dev/null 2>&1; then
    netstat -tuln | grep -q ":$1 "
    return $?
  else
    # Se não temos ferramentas para verificar, assumimos que está livre
    return 1
  fi
}

# Verificar se a porta está em uso e informar o usuário
if check_port "$APP_PORT"; then
  echo "AVISO: Porta $APP_PORT já está em uso."
  echo "Há processos em execução nesta porta:"
  if command -v lsof >/dev/null 2>&1; then
    lsof -i :"$APP_PORT"
  elif command -v netstat >/dev/null 2>&1; then
    netstat -tuln | grep ":$APP_PORT "
  fi
  
  read -p "Deseja tentar finalizar estes processos? (s/n): " resposta
  if [[ "$resposta" =~ ^[Ss]$ ]]; then
    echo "Tentando finalizar processos na porta $APP_PORT..."
    if command -v lsof >/dev/null 2>&1; then
      # Obter PIDs e matá-los um a um para garantir 
      PIDs=$(lsof -i :"$APP_PORT" -t)
      for PID in $PIDs; do
        # Verificar se o processo pertence ao Docker
        PROCESS_NAME=$(ps -p "$PID" -o comm= 2>/dev/null || echo "")
        if [[ "$PROCESS_NAME" == *"docker"* || "$PROCESS_NAME" == *"Docker"* ]]; then
          echo "AVISO: Evitando terminar processo Docker ($PID): $PROCESS_NAME"
          echo "A porta $APP_PORT está sendo usada pelo Docker Desktop."
          echo "Por favor, escolha outra porta para a aplicação em docker-compose.yml (ports: \"NOVA_PORTA:8080\")"
          exit 1
        else
          echo "Finalizando processo $PID..."
          kill -9 "$PID" 2>/dev/null
        fi
      done
      
      # Verificar se os processos foram realmente finalizados
      sleep 2
      if check_port "$APP_PORT"; then
        echo "ERRO: Não foi possível finalizar todos os processos. Por favor, finalize-os manualmente."
        exit 1
      fi
    elif command -v netstat >/dev/null 2>&1; then
      # Opção mais básica, pode não funcionar em todos os sistemas
      echo "Não foi possível identificar o PID. Finalize manualmente."
      exit 1
    fi
  else
    echo "Abortando para evitar conflito de portas."
    echo "Você pode:"
    echo "1. Finalizar o processo que está usando a porta $APP_PORT"
    echo "2. Editar docker-compose.yml para usar outra porta"
    exit 1
  fi
fi

# Verificar se já existe um container com o mesmo nome rodando
EXISTING_CONTAINER=$(docker ps -a -q -f "name=app-core-api")
if [ -n "$EXISTING_CONTAINER" ]; then
  echo "-> Removendo container existente..."
  docker rm -f "$EXISTING_CONTAINER" > /dev/null
fi

# Verificar se o projeto precisa ser construído
if [ "$1" == "--build" ] || [ "$1" == "-b" ]; then
  echo "-> Construindo a imagem Docker da aplicação..."
  docker-compose build app
fi

# Subir a aplicação
echo "-> Iniciando o container da aplicação..."
docker-compose up -d app

# Verificar status
APP_CONTAINER=$(docker ps -q -f "name=app-core-api")
if [ -z "$APP_CONTAINER" ]; then
  echo "ERRO: Falha ao iniciar o container da aplicação."
  echo "Verifique os logs com: docker-compose logs app"
  exit 1
fi

echo "-> Aguardando aplicação inicializar..."
MAX_RETRIES=100
RETRY_COUNT=0

while [ $RETRY_COUNT -lt $MAX_RETRIES ]; do
  HEALTH_STATUS=$(docker inspect --format='{{.State.Health.Status}}' "$APP_CONTAINER")
  
  if [ "$HEALTH_STATUS" == "healthy" ]; then
    APP_PORT=$(docker ps --filter "id=$APP_CONTAINER" --format "{{.Ports}}" | grep -oP '\d+(?=->)' | head -n 1)
    echo "===== Aplicação iniciada com sucesso! ====="
    echo "- API: http://localhost:$APP_PORT"
    echo "- Documentação Swagger: http://localhost:$APP_PORT/api/swagger-ui.html"
    exit 0
  fi
  
  RETRY_COUNT=$((RETRY_COUNT+1))
  echo "Aguardando aplicação inicializar... ($RETRY_COUNT/$MAX_RETRIES)"
  sleep 2
done

echo "AVISO: Tempo limite excedido, mas o container foi iniciado."
echo "Verifique o status da aplicação manualmente."
echo "- Container: $APP_CONTAINER"
echo "- URL: http://localhost:$APP_PORT"
echo "- Logs: docker logs $APP_CONTAINER" 