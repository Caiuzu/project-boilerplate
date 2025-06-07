# Makefile para o projeto Go backend-golang

# --- Configurações ---
# Nome do binário da aplicação
APP_NAME=hackathon_app
# Diretório do main.go
CMD_DIR=./cmd/server
# Diretório de saída do binário
OUTPUT_DIR=./bin

# Variáveis para Docker Compose
COMPOSE_FILE=docker-compose.yml
POSTGRES_VOLUME_NAME=postgres_data # Nome do volume definido no docker-compose.yml

# Variáveis para Migrations
# Certifique-se de que DATABASE_URL está no seu .env ou exportado no ambiente
# Exemplo: DATABASE_URL="postgres://postgres:postgres@localhost:5432/hackathondb?sslmode=disable"
MIGRATIONS_PATH=internal/database/migrations
MIGRATIONS_SOURCE_URL=file://$(MIGRATIONS_PATH)

# Variáveis para Swag (Swagger)
SWAG_CMD_DIR_ARG=cmd/server/main.go
SWAG_OUTPUT_DIR="./docs"
# Determina o caminho para o executável swag
SWAG_BIN_DIR := $(shell go env GOBIN)
ifeq ($(SWAG_BIN_DIR),)
    SWAG_BIN_DIR := $(shell go env GOPATH)/bin
endif
SWAG_CMD := $(SWAG_BIN_DIR)/swag

# Cores para output (opcional)
GREEN  := $(shell tput -Txterm setaf 2)
YELLOW := $(shell tput -Txterm setaf 3)
CYAN   := $(shell tput -Txterm setaf 6)
RESET  := $(shell tput -Txterm sgr0)


# --- Alvos Principais --- 
.PHONY: all run build clean tidy test swagger migrate migrate-up migrate-down migrate-create db-start db-stop db-restart db-logs dev-start db-recreate rebuild redis-cli redis-monitor

all: build

# Comando principal para desenvolvimento: garante dependências, swagger, build e roda a app
dev-start: db-start tidy swagger build
	@echo "$(CYAN)🚀 Iniciando a aplicação $(APP_NAME)...$(RESET)"
	@$(OUTPUT_DIR)/$(APP_NAME)

run: dev-start # Alias para dev-start para manter compatibilidade ou preferência

# Alvo para reconstruir tudo do zero (clean + tidy + swagger + build)
rebuild: clean tidy swagger build
	@echo "$(GREEN)✅ Reconstrução completa concluída!$(RESET)"

build: # Constrói o binário da aplicação
	@echo "$(YELLOW)🚧 Construindo o binário da aplicação...$(RESET)"
	@mkdir -p $(OUTPUT_DIR)
	@go build -o $(OUTPUT_DIR)/$(APP_NAME) $(CMD_DIR)/main.go
	@echo "$(GREEN)✅ Binário construído em $(OUTPUT_DIR)/$(APP_NAME)$(RESET)"

clean: # Limpa binários e arquivos temporários
	@echo "$(YELLOW)🧹 Limpando arquivos de build...$(RESET)"
	@rm -rf $(OUTPUT_DIR)
	@rm -rf $(SWAG_OUTPUT_DIR) # Limpa docs do Swagger também
	@echo "$(GREEN)✅ Limpeza concluída.$(RESET)"

tidy: # Garante que o go.mod e go.sum estão corretos e baixa dependências
	@echo "$(YELLOW)🧹 Organizando e baixando dependências Go (go mod tidy && go mod download)...$(RESET)"
	@go mod tidy
	@go mod download
	@echo "$(GREEN)✅ Dependências Go organizadas e baixadas.$(RESET)"

test: tidy # Roda os testes (precisa de arquivos _test.go)
	@echo "$(YELLOW)🧪 Rodando testes...$(RESET)"
	@go test ./... -v

swagger: # Gera a documentação Swagger
	@echo "$(YELLOW)📚 Gerando documentação Swagger...$(RESET)"
	@echo "$(YELLOW)Usando comando swag em: $(SWAG_CMD)$(RESET)"
	@if ! test -x "$(SWAG_CMD)"; then \
		echo "$(YELLOW)CLI 'swag' não encontrado ou não executável em $(SWAG_CMD). Tentando instalar...$(RESET)"; \
		go install github.com/swaggo/swag/cmd/swag@latest; \
		if ! test -x "$(SWAG_CMD)"; then \
			echo "$(YELLOW)Falha ao instalar ou encontrar 'swag' em $(SWAG_CMD) após a instalação.$(RESET)"; \
			echo "$(YELLOW)Verifique seu GOPATH/GOBIN e PATH, ou se a instalação do 'swag' teve sucesso.$(RESET)"; \
			exit 1; \
		fi; \
	fi
	@echo "$(YELLOW)Executando: $(SWAG_CMD) init -g $(SWAG_CMD_DIR_ARG) --output $(SWAG_OUTPUT_DIR) --parseDependency --parseInternal$(RESET)"
	@"$(SWAG_CMD)" init -g $(SWAG_CMD_DIR_ARG) --output $(SWAG_OUTPUT_DIR) --parseDependency --parseInternal
	@echo "$(GREEN)✅ Documentação Swagger gerada em $(SWAG_OUTPUT_DIR)$(RESET)"


# --- Alvos do Banco de Dados (Docker Compose) --- 
db-start: # Inicia o container do PostgreSQL se não estiver rodando
	@echo "$(YELLOW)🐳 Verificando e iniciando container do PostgreSQL...$(RESET)"
	@docker-compose -f $(COMPOSE_FILE) up -d postgres
	@echo "$(GREEN)✅ Container do PostgreSQL pronto ou já estava rodando.$(RESET)"
	@echo "$(YELLOW)🕒 Aguardando o banco de dados ficar completamente acessível...$(RESET)"
	@# Loop simples para esperar o Postgres aceitar conexões (melhorar se necessário)
	@timeout=30; \
	while ! docker exec hackathon_postgres pg_isready -U $${DB_USER:-postgres} -d $${DB_NAME:-hackathondb} -q && [ $$timeout -gt 0 ]; do \
	  sleep 1; \
	  timeout=$$((timeout-1)); \
	done; \
	if [ $$timeout -eq 0 ]; then \
	  echo "$(YELLOW)⚠️  Timeout esperando pelo PostgreSQL.$(RESET)"; \
	else \
	  echo "$(GREEN)🐘 PostgreSQL pronto para conexões.$(RESET)"; \
	fi

db-stop: # Para o container do PostgreSQL
	@echo "$(YELLOW)🐳 Parando container do PostgreSQL...$(RESET)"
	@docker-compose -f $(COMPOSE_FILE) down
	@echo "$(GREEN)✅ Container do PostgreSQL parado.$(RESET)"

db-restart: db-stop db-start # Reinicia o container do PostgreSQL

db-logs: # Mostra os logs do container do PostgreSQL
	@echo "$(YELLOW)📜 Logs do PostgreSQL:$(RESET)"
	@docker-compose -f $(COMPOSE_FILE) logs -f postgres

db-recreate: # Para, remove o volume de dados, inicia e aplica migrações
	@echo "$(YELLOW)⚠️  Recriando o banco de dados! Todos os dados serão perdidos. $(RESET)"
	$(MAKE) db-stop
	@echo "$(YELLOW)🗑️ Removendo volume de dados do PostgreSQL ($(POSTGRES_VOLUME_NAME))...$(RESET)"
	@docker volume rm $(POSTGRES_VOLUME_NAME) || true # O '|| true' evita erro se o volume não existir
	$(MAKE) db-start
	$(MAKE) migrate-up
	@echo "$(GREEN)✅ Banco de dados recriado e migrações aplicadas.$(RESET)"


# --- Alvos de Migrations (golang-migrate/migrate) --- 
# É necessário ter o migrate CLI instalado: https://github.com/golang-migrate/migrate/tree/master/cmd/migrate
# Ou rodar via Docker: https://github.com/golang-migrate/migrate/pkgs/container/migrate
# Makefile tentará instalar se não encontrar.

ensure-migrate-cli:
	@command -v migrate >/dev/null 2>&1 || (echo "$(YELLOW)CLI 'migrate' não encontrado. Tentando instalar...$(RESET)"; go install -tags 'postgres' github.com/golang-migrate/migrate/v4/cmd/migrate@latest; echo "$(GREEN)Tente rodar o comando make novamente.$(RESET)"; exit 1)

migrate-status: ensure-migrate-cli db-start # Mostra o status das migrations
	@echo "$(YELLOW)📊 Verificando status das migrações...$(RESET)"
	@migrate -source $(MIGRATIONS_SOURCE_URL) -database "$$(grep DATABASE_URL .env | cut -d '=' -f2- | tr -d '\"')" version

migrate: ensure-migrate-cli db-start migrate-up # Alias para migrate-up

migrate-up: ensure-migrate-cli db-start # Aplica todas as migrações 'up' pendentes
	@echo "$(YELLOW)⬆️  Aplicando migrações 'up'...$(RESET)"
	@migrate -source $(MIGRATIONS_SOURCE_URL) -database "$$(grep DATABASE_URL .env | cut -d '=' -f2- | tr -d '\"')" up
	@echo "$(GREEN)✅ Migrações 'up' aplicadas.$(RESET)"

migrate-down: ensure-migrate-cli db-start # Reverte a última migração 'up' aplicada
	@echo "$(YELLOW)⬇️  Revertendo última migração 'down'...$(RESET)"
	@migrate -source $(MIGRATIONS_SOURCE_URL) -database "$$(grep DATABASE_URL .env | cut -d '=' -f2- | tr -d '\"')" down 1
	@echo "$(GREEN)✅ Última migração revertida.$(RESET)"

migrate-down-all: ensure-migrate-cli db-start # Reverte TODAS as migrações
	@echo "$(YELLOW)⬇️⬇️ Revertendo TODAS as migrações 'down'...$(RESET)"
	@migrate -source $(MIGRATIONS_SOURCE_URL) -database "$$(grep DATABASE_URL .env | cut -d '=' -f2- | tr -d '\"')" down -all
	@echo "$(GREEN)✅ Todas as migrações revertidas.$(RESET)"

migrate-force: ensure-migrate-cli db-start # Força uma versão específica da migração (CUIDADO!)
	@echo "$(YELLOW)❗️ Forçando versão da migração (ex: para resolver estado 'dirty'). Use com CUIDADO! $(RESET)"
	@echo "$(YELLOW)Por favor, informe a versão para forçar: $(RESET)"
	@read version; \
	migrate -source $(MIGRATIONS_SOURCE_URL) -database "$$(grep DATABASE_URL .env | cut -d '=' -f2- | tr -d '\"')" force $$version
	@echo "$(GREEN)✅ Versão forçada para $$version.$(RESET)"

# Para usar: make migrate-create NAME=nome_da_sua_migration
migrate-create: ensure-migrate-cli # Cria novos arquivos de migração (up e down)
	@if [ -z "$(NAME)" ]; then \
		echo "$(YELLOW)❗️ Por favor, especifique o nome da migração. Uso: make migrate-create NAME=<nome_da_migration>$(RESET)"; \
		exit 1; \
	fi
	@echo "$(YELLOW)✨ Criando novos arquivos de migração para: $(NAME)...$(RESET)"
	@migrate create -ext sql -dir $(MIGRATIONS_PATH) -seq $(NAME)
	@echo "$(GREEN)✅ Arquivos de migração criados em $(MIGRATIONS_PATH)$(RESET)"


# --- Ajuda --- 
help: # Mostra esta mensagem de ajuda
	@echo "-------------------------------------------------------------------------"
	@echo "$(GREEN)Makefile para o Projeto Go - Hackathon Backend$(RESET)"
	@echo "-------------------------------------------------------------------------"
	@echo "Uso: make [alvo]"
	@echo ""
	@echo "Alvos Principais para Desenvolvimento:"
	@echo "  $(CYAN)dev-start (ou run)$(RESET)   Inicia DB, garante dependências, gera Swagger, constrói e roda a app."
	@echo "  $(YELLOW)rebuild$(RESET)            Limpa tudo e reconstrói (clean + tidy + swagger + build)."
	@echo "  $(YELLOW)build$(RESET)              Constrói o binário da aplicação."
	@echo "  $(YELLOW)tidy$(RESET)                Organiza dependências Go (go mod tidy & download)."
	@echo "  $(YELLOW)swagger$(RESET)            Gera a documentação Swagger."
	@echo "  $(YELLOW)test$(RESET)               Roda os testes (requer arquivos _test.go)."
	@echo "  $(YELLOW)clean$(RESET)              Remove arquivos de build e docs do Swagger."
	@echo ""
	@echo "Alvos do Banco de Dados (Docker Compose):"
	@echo "  $(YELLOW)db-start$(RESET)           Inicia o container do PostgreSQL."
	@echo "  $(YELLOW)db-stop$(RESET)            Para o container do PostgreSQL."
	@echo "  $(YELLOW)db-restart$(RESET)         Reinicia o container do PostgreSQL."
	@echo "  $(YELLOW)db-logs$(RESET)            Mostra os logs do container PostgreSQL."
	@echo "  $(YELLOW)db-recreate$(RESET)        ⚠️  PARA, LIMPA DADOS, inicia DB e aplica migrações."
	@echo ""
	@echo "Alvos de Migrations (golang-migrate/migrate):"
	@echo "  $(YELLOW)migrate-status$(RESET)      Mostra o status atual das migrações."
	@echo "  $(YELLOW)migrate (ou migrate-up)$(RESET) Aplica todas as migrações 'up' pendentes."
	@echo "  $(YELLOW)migrate-down$(RESET)        Reverte a última migração aplicada."
	@echo "  $(YELLOW)migrate-down-all$(RESET)    Reverte TODAS as migrações."
	@echo "  $(YELLOW)migrate-force VERSION=<num>$(RESET) Força uma versão específica da migração (use com cuidado)."
	@echo "  $(YELLOW)migrate-create NAME=<nome>$(RESET) Cria novos arquivos de migração."
	@echo ""
	@echo "Outros Alvos:"
	@echo "  $(YELLOW)help$(RESET)               Mostra esta mensagem de ajuda."
	@echo "-------------------------------------------------------------------------"

.DEFAULT_GOAL := help

# Variáveis
BINARY_NAME=hackathon_app
MAIN_FILE=cmd/server/main.go
SWAG_CMD_DIR_ARG=cmd/server/main.go
OUTPUT_DIR=./bin
GOPATH=$(shell go env GOPATH)

# Comandos
.PHONY: all build clean test run dev-start dev-stop swagger rebuild redis-cli redis-monitor tidy migrate

all: clean build

build:
	@echo "Construindo o binário..."
	@mkdir -p $(OUTPUT_DIR)
	go build -o $(OUTPUT_DIR)/$(BINARY_NAME) $(MAIN_FILE)

clean:
	@echo "Limpando arquivos gerados..."
	rm -f $(OUTPUT_DIR)/$(BINARY_NAME)
	rm -rf docs

tidy:
	@echo "Organizando e baixando dependências Go..."
	go mod tidy
	go mod download

test:
	@echo "Executando testes..."
	go test -v ./...

run: build
	@echo "Executando a aplicação..."
	$(OUTPUT_DIR)/$(BINARY_NAME)

dev-start:
	@echo "Iniciando ambiente de desenvolvimento..."
	docker-compose up -d
	@echo "Aguardando serviços iniciarem..."
	sleep 5
	@echo "Iniciando aplicação..."
	go run $(MAIN_FILE)

dev-stop:
	@echo "Parando ambiente de desenvolvimento..."
	docker-compose down

swagger:
	@echo "Verificando se swag está instalado..."
	@if ! command -v swag > /dev/null; then \
		echo "Instalando swag..."; \
		go install github.com/swaggo/swag/cmd/swag@latest; \
	fi
	@echo "Gerando documentação Swagger..."
	@if command -v swag > /dev/null; then \
		swag init -g $(SWAG_CMD_DIR_ARG) -o docs; \
	else \
		$(GOPATH)/bin/swag init -g $(SWAG_CMD_DIR_ARG) -o docs; \
	fi

rebuild: clean tidy swagger build

redis-cli:
	@echo "Conectando ao Redis CLI..."
	docker exec -it hktn-redis redis-cli

redis-monitor:
	@echo "Monitorando Redis..."
	docker exec -it hktn-redis redis-cli monitor

migrate:
	@echo "Executando migrações..."
	go run $(MAIN_FILE) migrate
