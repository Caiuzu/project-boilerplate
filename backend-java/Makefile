# Makefile para Backend Java Boilerplate (Spring Boot)

.PHONY: help build run test up down restart migrate reset-db lint clean

# Variáveis
GRADLE=./gradlew
DOCKER_COMPOSE=docker/docker-compose.yml

help:
	@echo "Comandos disponíveis:"
	@echo "  make build        - Build do projeto Java (Gradle)"
	@echo "  make run          - Sobe a aplicação localmente (Spring Boot)"
	@echo "  make test         - Roda os testes automatizados"
	@echo "  make up           - Sobe a infraestrutura (Docker Compose)"
	@echo "  make down         - Derruba a infraestrutura (Docker Compose)"
	@echo "  make restart      - Reinicia a infraestrutura (Docker Compose)"
	@echo "  make migrate      - Aplica as migrations do banco (Liquibase)"
	@echo "  make reset-db     - Reseta o banco de dados (DANGER!)"
	@echo "  make lint         - Roda o checkstyle/spotless (se configurado)"
	@echo "  make clean        - Limpa builds e artefatos temporários"

build:
	$(GRADLE) clean build

run:
	$(GRADLE) bootRun

test:
	$(GRADLE) test

up:
	docker compose -f $(DOCKER_COMPOSE) up -d

down:
	docker compose -f $(DOCKER_COMPOSE) down

restart:
	docker compose -f $(DOCKER_COMPOSE) down
	sleep 2
	docker compose -f $(DOCKER_COMPOSE) up -d

migrate:
	$(GRADLE) update

reset-db:
	$(GRADLE) dropAll update

lint:
	$(GRADLE) spotlessCheck || echo 'Configure o plugin Spotless/Checkstyle para lint.'

clean:
	$(GRADLE) clean 