# Guia de Contribuição - backend-golang

Bem-vindo(a)! Siga estas orientações para contribuir com o projeto.

## Padrão de Commits

Utilize o padrão [Conventional Commits](https://www.conventionalcommits.org/pt-br/v1.0.0/):

- **feat:** Nova funcionalidade
- **fix:** Correção de bug
- **docs:** Apenas documentação
- **style:** Formatação, sem alteração de código (espaços, quebras de linha, etc)
- **refactor:** Refatoração de código, sem alteração de funcionalidade
- **test:** Adição ou ajuste de testes
- **chore:** Atualização de tarefas, configs, dependências, etc

**Exemplos:**
```
feat: adicionar endpoint de criação de item
fix: corrigir bug no middleware de autenticação
docs: atualizar README com instruções de uso
```

## Fluxo de Branches

- **main**: Branch principal, sempre estável
- **dev**: Branch de desenvolvimento (merge de features antes de ir para main)
- **feature/nome-da-feature**: Para novas funcionalidades
- **fix/nome-do-bug**: Para correções de bugs
- **test/nome-do-teste**: Para melhorias ou adição de testes
- **docs/nome-da-doc**: Para alterações de documentação

**Fluxo sugerido:**
1. Crie uma branch a partir de `dev`:
   ```bash
   git checkout dev
   git pull origin dev
   git checkout -b feature/nome-da-feature
   ```
2. Faça commits seguindo o padrão acima.
3. Ao finalizar, abra um Pull Request para `dev`.
4. Após revisão, será feito o merge em `dev` e, periodicamente, em `main`.

## Revisão de Código
- Sempre peça revisão via Pull Request.
- Adicione descrição clara do que foi feito.
- Adicione prints, logs ou exemplos de uso se necessário.

## Boas Práticas
- Escreva testes para novas funcionalidades.
- Atualize a documentação sempre que necessário.
- Siga os padrões de código e nomenclatura do projeto.

---

Dúvidas? Abra uma issue ou entre em contato com os mantenedores. 