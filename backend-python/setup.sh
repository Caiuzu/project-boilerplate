#!/bin/bash

set -e

# Detecta o SO
OS="$(uname -s)"
PYTHON_VERSION="3.11"

function install_python_linux() {
  echo "[INFO] Instalando Python $PYTHON_VERSION no Linux..."
  sudo apt-get update
  sudo apt-get install -y python$PYTHON_VERSION python$PYTHON_VERSION-venv python$PYTHON_VERSION-dev python3-pip
  sudo update-alternatives --install /usr/bin/python3 python3 /usr/bin/python$PYTHON_VERSION 1
}

function install_python_mac() {
  echo "[INFO] Instalando Python $PYTHON_VERSION no macOS..."
  if ! command -v brew &> /dev/null; then
    echo "[INFO] Homebrew não encontrado. Instalando..."
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
    eval "$($(brew --prefix)/bin/brew shellenv)"
  fi
  brew update
  brew install python@$PYTHON_VERSION || brew upgrade python@$PYTHON_VERSION
}

function ensure_python() {
  if ! python3 --version | grep -q "$PYTHON_VERSION"; then
    if [ "$OS" = "Darwin" ]; then
      install_python_mac
    elif [ "$OS" = "Linux" ]; then
      install_python_linux
    else
      echo "[ERRO] SO não suportado: $OS"; exit 1
    fi
  else
    echo "[INFO] Python $PYTHON_VERSION já instalado."
  fi
}

function create_venv() {
  echo "[INFO] Criando virtualenv..."
  python3 -m venv .venv
  source .venv/bin/activate
}

function install_requirements() {
  echo "[INFO] Instalando dependências do projeto..."
  pip install --upgrade pip
  pip install -r requirements.txt
}

echo "[SETUP] Detectando SO e preparando ambiente Python..."
ensure_python
create_venv
install_requirements

echo "[SETUP] Ambiente pronto! Ative com: source .venv/bin/activate"
echo "[SETUP] Para rodar: make run" 