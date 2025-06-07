# Act - Execução Local de GitHub Actions

Este diretório contém a configuração para o Act, uma ferramenta que permite executar GitHub Actions localmente.

## O que é o Act?

O Act (<https://github.com/nektos/act>) é uma ferramenta de linha de comando que permite executar seus GitHub Actions localmente sem precisar fazer commit e push para o GitHub.

## Pré-requisitos

Para usar o Act, você precisa ter:

1. Docker instalado
2. Act instalado na sua máquina

## Instalação do Act

### macOS

```bash
brew install act
```

### Linux

```bash
curl -s https://raw.githubusercontent.com/nektos/act/master/install.sh | sudo bash
```

### Windows

```bash
choco install act-cli
```

## Uso

Após instalar o Act, você pode executar seus GitHub Actions localmente usando o script `app`:

```bash
# Executar todos os workflows
./app run:action

# Executar um workflow específico
./app run:workflow .github/workflows/ci-cd.yml
```

## Configuração

A configuração do Act está no arquivo `.actrc` na raiz do projeto, que define:

- Imagens Docker para os diferentes runners
- Opções de vinculação de diretórios
- Configurações de artefatos

## Limitações

- Algumas ações do GitHub podem não funcionar perfeitamente em ambiente local
- Secrets precisam ser configurados localmente
- Nem todos os recursos do GitHub Actions estão disponíveis

## Mais Informações

Para mais detalhes, consulte a [documentação oficial do Act](https://github.com/nektos/act).
