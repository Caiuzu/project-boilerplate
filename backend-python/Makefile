# Makefile para backend-python (FastAPI + IA)

.PHONY: help install run run-prod lint format test download

help:
	@echo "Comandos disponíveis:"
	@echo "  make install     - Instala as dependências do projeto"
	@echo "  make run         - Sobe o servidor em modo desenvolvimento (hot reload)"
	@echo "  make run-prod    - Sobe o servidor em modo produção (Uvicorn)"
	@echo "  make lint        - Roda o flake8 para linting (se instalado)"
	@echo "  make format      - Roda o black para formatar o código (se instalado)"
	@echo "  make test        - Roda os testes (pytest, se instalado)"
	@echo "  make download    - Baixa modelos do HuggingFace usando o script download_models.py"

install:
	pip install --upgrade pip
	pip install -r requirements.txt

run:
	uvicorn app.main:app --reload --host 0.0.0.0 --port 8000

run-prod:
	uvicorn app.main:app --host 0.0.0.0 --port 8000

lint:
	flake8 app || echo 'flake8 não instalado. Rode: pip install flake8'

format:
	black app || echo 'black não instalado. Rode: pip install black'

test:
	pytest || echo 'pytest não instalado. Rode: pip install pytest'

download:
	python download_models.py $(MODELS)

# Exemplo de uso:
# make download MODELS="mistralai/Mistral-7B-Instruct-v0.3 sentence-transformers/all-MiniLM-L6-v2" 