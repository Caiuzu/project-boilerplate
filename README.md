# Hackathon Kit - IA + Multi-Cloud 2025

> **Índice**
>
> - [Resumo do Repositório](./RESUMO.md)
> - [🎯 Objetivos do Kit](#🎯-objetivos-do-kit)
> - [📂 Estrutura do Projeto](#📂-estrutura-do-projeto)
> - [🚀 Começando](#🚀-começando)
> - [🛠️ Comandos Úteis com Makefile](#🛠️-comandos-úteis-com-makefile)
> - [📖 Documentação Adicional](#📖-documentação-adicional)
> - [🤝 Contribuindo](#🤝-contribuindo)

Este repositório serve como um kit de desenvolvimento inicial, focado em Inteligência Artificial e Multi-Cloud. Ele fornece uma estrutura de mono-repo com diversos serviços backend, um frontend e configurações de infraestrutura como código.

## 🎯 Objetivos do Kit

- Fornecer uma base de código inicial para cada tecnologia principal (Java, Python, Go, Next.js).
- Demonstrar a integração entre serviços via Docker Compose.
- Incluir exemplos de documentação de API (Swagger/OpenAPI).
- Oferecer uma estrutura para gerenciamento de infraestrutura como código (Terraform, LocalStack).
- Padronizar o ambiente de desenvolvimento e as contribuições através de Makefiles e guias.

## 📂 Estrutura do Projeto

O projeto é organizado como um mono-repo utilizando submódulos Git para cada serviço/componente:

- [`backend-java/`](backend-java/): Backend em Spring Boot ([README](backend-java/README.md))
- [`backend-python/`](backend-python/): Backend em FastAPI para pipelines de IA ([README](backend-python/README.md))
- [`backend-golang/`](backend-golang/): Backend em Go (net/http) com Swagger ([README](backend-golang/README.md))
- [`backend-kotlin/`](backend-kotlin/): Backend em Kotlin Spring Boot ([README](backend-kotlin/README.md))
- [`frontend-nextjs/`](frontend-nextjs/): Aplicação Next.js com TypeScript ([README](frontend-nextjs/README.md))
- [`infra/`](infra/): Configurações de infraestrutura como código ([README](infra/README.md))
- [`docs/`](docs/): Documentação geral do projeto e da stack

Consulte o [`docs/STACK_OVERVIEW.md`](docs/STACK_OVERVIEW.md) para uma visão detalhada de cada componente, suas tecnologias, padrões de código e convenções de nomenclatura.

## 🚀 Começando

### Pré-requisitos Principais

- **Git**: Para controle de versão.
- **Docker**: Para executar os contêineres dos serviços.
- **Docker Compose**: Para orquestrar os contêineres.
- **Make**: (Opcional, mas recomendado) Para utilizar os comandos simplificados do `Makefile` na raiz.

*Nota: Para executar comandos de build, teste ou lint nativamente (fora do Docker, via `make lint` ou `make test`), ou para desenvolvimento direto nos submódulos, você precisará das SDKs e ferramentas específicas de cada linguagem/serviço instaladas (ex: JDK, Node.js, pnpm, Python, Go, Swag CLI). Consulte o README de cada submódulo para detalhes.*

### Clonando o Repositório

Este projeto utiliza submódulos Git. Para clonar o repositório principal e todos os submódulos, use:

```bash
git clone --recursive <URL_DO_SEU_REPOSITORIO_PRINCIPAL>
cd <NOME_DO_REPOSITORIO_PRINCIPAL>
```

Se você já clonou o repositório sem a flag `--recursive`, navegue até o diretório do projeto e execute:

```bash
git submodule update --init --recursive
```

Para atualizar os submódulos para suas versões mais recentes (conforme o commit apontado no repositório principal):

```bash
git submodule update --remote --merge # Ou --rebase
```

### Configurando Permissões (Primeira Vez)

Alguns scripts (como o `gradlew` do Java) podem precisar de permissão de execução. O `Makefile` tenta lidar com isso, mas você pode executar manualmente se necessário:

```bash
make set-permissions
```

## 🛠️ Comandos Úteis com Makefile

O `Makefile` na raiz do projeto centraliza os comandos mais comuns. Certifique-se de estar na raiz do projeto ao executá-los.

### Ambiente de Desenvolvimento

- **Subir todos os serviços (modo desenvolvimento):**

    ```bash
    make dev
    ```

    (Este comando é um alias para `make up`, que constrói as imagens se necessário e sobe os contêineres em modo detached.)

- **Parar e remover todos os serviços, redes e volumes:**

    ```bash
    make down
    ```

### Logs e Gerenciamento de Contêineres

- **Visualizar logs de todos os serviços:**

    ```bash
    make logs
    ```

- **Visualizar logs de um serviço específico:**

    ```bash
    make logs-service service=<nome-do-servico> # ex: make logs-service service=backend-java
    ```

- **Reiniciar todos os serviços:**

    ```bash
    make restart
    ```

- **Reiniciar um serviço específico:**

    ```bash
    make restart-service service=<nome-do-servico>
    ```

- **Listar contêineres em execução:**

    ```bash
    make ps
    ```

### Build, Teste e Lint

- **Construir/Reconstruir todas as imagens Docker:**

    ```bash
    make build
    ```

- **Executar testes de todos os serviços (requer SDKs e dependências de teste locais):**

    ```bash
    make test
    ```

    *Nota: Verifique o `Makefile` e os READMEs dos submódulos para pré-requisitos de teste específicos de cada serviço.*

- **Executar linters em todos os serviços (requer SDKs e dependências de lint locais):**

    ```bash
    make lint
    ```

    *Nota: Verifique o `Makefile` e os READMEs dos submódulos para pré-requisitos de linting específicos de cada serviço.*

### Limpeza

- **Limpar artefatos de build e Docker (CUIDADO):**

    ```bash
    make clean
    ```

### Ajuda

- **Ver todos os comandos disponíveis no Makefile:**

    ```bash
    make help
    ```

Para rodar cada serviço individualmente fora do Docker, ou para configurações mais detalhadas, consulte o `README.md` específico dentro de cada diretório de submódulo.

## 📖 Documentação Adicional

- **Guia de Contribuição:** [`CONTRIBUTING.md`](CONTRIBUTING.md) (detalha o fluxo de branches, padrão de commits, etc.)
- **Visão Geral da Stack:** [`docs/STACK_OVERVIEW.md`](docs/STACK_OVERVIEW.md) (consolida as tecnologias, padrões e convenções de cada serviço).

## 🤝 Contribuindo

Leia o [`CONTRIBUTING.md`](CONTRIBUTING.md) antes de começar a desenvolver.
