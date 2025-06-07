# Guia de Contribuição - Hackathon Kit

Este documento fornece diretrizes para contribuir com o projeto Hackathon Kit.

## 1. Fluxo de Branch (Git Flow Simplificado)

Utilizaremos um fluxo de branch baseado no Git Flow, mas simplificado para a agilidade do hackathon:

*   **`main`**: Branch principal. Deve refletir o estado mais estável e "pronto para apresentação" do projeto. Somente merges de `develop` (ou `feature` branches urgentes e bem testadas) são permitidos.
*   **`develop`**: Branch de desenvolvimento principal. Novas features e correções são integradas aqui antes de irem para a `main`.
*   **`feature/<nome-da-feature>`**: Branches para desenvolvimento de novas funcionalidades. Criadas a partir de `develop`.
    *   Exemplo: `feature/user-authentication`, `feature/ia-model-integration`
*   **`fix/<nome-da-correcao>`**: Branches para correções de bugs. Criadas a partir de `develop` (ou `main` para hotfixes).
    *   Exemplo: `fix/login-bug`, `fix/api-performance`
*   **`hotfix/<nome-do-hotfix>`**: Para correções críticas diretamente na `main`. Deve ser mergeada de volta para `develop` e `main`.

**Fluxo Básico:**

1.  Atualize seu branch `develop` local: `git checkout develop && git pull origin develop`
2.  Crie um novo branch de feature: `git checkout -b feature/minha-nova-feature develop`
3.  Desenvolva e faça commits no seu branch de feature.
4.  Envie seu branch para o repositório remoto: `git push -u origin feature/minha-nova-feature`
5.  Abra um Pull Request (PR) de `feature/minha-nova-feature` para `develop`.
6.  Após revisão e aprovação, o PR é mergeado.
7.  Para releases ou antes de apresentações, um PR de `develop` é aberto para `main`.

## 2. Padrão de Mensagem de Commit (Conventional Commits)

Adotaremos o padrão [Conventional Commits](https://www.conventionalcommits.org/). Isso ajuda a manter o histórico do Git claro e facilita a geração de changelogs (se necessário).

**Formato:**

```
<tipo>[escopo opcional]: <descrição>

[corpo opcional]

[rodapé opcional]
```

**Tipos Comuns:**

*   `feat`: Uma nova feature.
*   `fix`: Uma correção de bug.
*   `docs`: Mudanças apenas na documentação.
*   `style`: Mudanças que não afetam o significado do código (espaçamento, formatação, ponto e vírgula ausente, etc).
*   `refactor`: Uma mudança de código que não corrige um bug nem adiciona uma feature.
*   `perf`: Uma mudança de código que melhora a performance.
*   `test`: Adicionando testes faltantes ou corrigindo testes existentes.
*   `build`: Mudanças que afetam o sistema de build ou dependências externas (ex: Gradle, npm, Dockerfile).
*   `ci`: Mudanças nos arquivos e scripts de configuração de CI (ex: GitHub Actions).
*   `chore`: Outras mudanças que não modificam arquivos `src` ou de teste (ex: atualização de tarefas do Makefile).
*   `revert`: Reverte um commit anterior.

**Exemplos:**

```
feat: Adicionar autenticação de usuário com JWT

Implementa o fluxo de login e registro utilizando JSON Web Tokens.
```

```
fix(api): Corrigir cálculo de pontuação no endpoint /score

O cálculo estava retornando um valor negativo incorretamente.
Closes #123
```

```
docs: Atualizar README com instruções de setup do backend-golang
```

## 3. Pull Requests (PRs)

*   **Título Claro:** Use o padrão de mensagem de commit para o título do PR.
*   **Descrição Detalhada:** Explique o que foi feito e por quê. Se o PR resolve uma Issue, mencione-a (ex: `Closes #42`).
*   **Pequenos e Focados:** PRs devem ser pequenos o suficiente para serem revisados facilmente. Evite PRs com muitas mudanças não relacionadas.
*   **Revisão:** Pelo menos um outro membro da equipe deve revisar o PR antes do merge (se o tempo permitir no hackathon).
*   **Verificações de CI:** Garanta que todas as verificações de CI (linters, testes) estão passando.

## 4. Estilo de Código e Linters

*   Siga os padrões de código definidos em `docs/STACK_OVERVIEW.md` para cada serviço.
*   Utilize os linters e formatadores configurados para cada projeto (ex: ESLint/Prettier para Frontend, Checkstyle/Spotless para Java, Black/Flake8 para Python, gofmt/golint para Go).
*   Idealmente, configure linters para rodar como pre-commit hooks ou nas actions de CI.

## 5. Comunicação

*   Use os canais de comunicação da equipe (Slack, Discord, etc.) para discutir features, problemas e coordenar o trabalho.
*   Seja claro e conciso em suas comunicações. 