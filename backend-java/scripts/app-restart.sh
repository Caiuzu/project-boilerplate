#!/bin/bash

SCRIPT_DIR=$(dirname "$(readlink -f "$0")")
PROJECT_ROOT=$(dirname "$SCRIPT_DIR")

echo "===== Reiniciando a aplicação ====="

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

# Executar o script de parada
echo "-> Parando a aplicação atual..."
"$SCRIPT_DIR/app-down.sh"

# Verificar se a parada foi bem-sucedida
if [ $? -ne 0 ]; then
  echo "AVISO: Houve problemas ao parar a aplicação."
  echo "Tentando continuar mesmo assim..."
fi

# Pequena pausa para garantir que os recursos sejam liberados
sleep 2

# Opções para reconstruir a imagem
BUILD=""
if [ "$1" == "--build" ] || [ "$1" == "-b" ]; then
  BUILD="--build"
fi

# Executar o script de inicialização
echo "-> Iniciando a aplicação novamente..."
"$SCRIPT_DIR/app-up.sh" $BUILD 