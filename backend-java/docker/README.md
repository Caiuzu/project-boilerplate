# Docker no Projeto App Core API

Este diretório contém as configurações Docker do projeto, organizadas em subdiretórios para melhor manutenção.

## Estrutura de Diretórios

- `/docker`
  - `/sonar` - Configuração do SonarQube para análise de código
  - `/act` - Configuração do Act para execução local de GitHub Actions

## Redes Docker

O projeto utiliza redes Docker para comunicação entre serviços:

- `fastapp-network` - Rede compartilhada entre aplicação principal e serviços auxiliares

## Otimizações Implementadas

Para melhorar a eficiência dos recursos, as seguintes otimizações foram implementadas:

### 1. Padronização de Imagens

- Uso de PostgreSQL 16-alpine em todos os serviços
- Reutilização de camadas de imagens Docker

### 2. Limites de Recursos

Todos os serviços possuem limites de CPU e memória configurados para evitar consumo excessivo:

```yaml
deploy:
  resources:
    limits:
      cpus: '1.0'
      memory: 512M
```

### 3. Redes Compartilhadas

Serviços que precisam se comunicar usam a mesma rede Docker:

```yaml
networks:
  - fastapp-network
```

### 4. Healthchecks

Todos os serviços possuem healthchecks para garantir que estão funcionando corretamente:

```yaml
healthcheck:
  test: ["CMD", "..."]
  interval: 10s
  timeout: 5s
  retries: 3
```

## Uso

O script `app` na raiz do projeto é usado para gerenciar os containers. Veja a ajuda para mais informações:

```bash
./app help
```
