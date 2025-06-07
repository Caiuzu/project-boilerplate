# Frontend Boilerplate (Next.js)

Este repositório é um **boilerplate moderno para aplicações Next.js**, pronto para uso em ambientes multicloud (AWS, GCP, Azure, etc.), com foco em escalabilidade, boas práticas de DevOps e arquitetura limpa. Ele serve como ponto de partida para novos projetos, acelerando o setup inicial e promovendo padrões de código e infraestrutura.

---

## 1. Visão Geral

- **Stack:** Next.js (App Router), TypeScript, Ant Design, ESLint, Prettier, Docker, Bun.
- **Pronto para produção:** Build standalone, multi-stage Docker, variáveis de ambiente, estrutura modular.
- **Multicloud:** Ideal para AWS, mas facilmente adaptável para outras clouds.
- **Foco:** Interface genérica, sem lógica de negócio acoplada, fácil de customizar para qualquer domínio (marketplace, dashboard, portal, etc.).

---

## 2. Estrutura de Pastas

```
dev-kit/
├── Dockerfile                # Imagem Docker otimizada (multi-stage)
├── docker-compose.yml        # Orquestração local e CI/CD
├── package.json              # Scripts e dependências
├── tsconfig.json             # Configuração TypeScript
├── next.config.js            # Configuração Next.js (output standalone, env, etc.)
├── public/                   # Arquivos estáticos
├── app/                      # App Router do Next.js (rotas, páginas, APIs)
├── components/               # Componentes React reutilizáveis
├── styles/                   # Estilos globais e modulares
├── i18n/                     # Internacionalização (Next-Intl)
├── .github/                  # Workflows de CI/CD
├── .eslintrc.json, .prettierrc, .editorconfig  # Qualidade de código
└── README.md                 # Este arquivo
```

---

## 3. Tecnologias Utilizadas

- **Next.js**: Framework React para produção, com App Router e Server Components.
- **TypeScript**: Tipagem estática para maior robustez.
- **Ant Design**: Biblioteca de UI moderna.
- **Bun**: Gerenciador de pacotes e runtime rápido.
- **Docker**: Containerização multi-stage para builds leves e seguros.
- **ESLint/Prettier**: Padrões de código e formatação.
- **Internacionalização**: Pronto para múltiplos idiomas via Next-Intl.

---

## 4. Como Usar (Desenvolvimento)

### Pré-requisitos

- Node.js 20+
- Bun instalado globalmente: `npm install -g bun`

### Passos

```bash
bun install
bun dev
```

Acesse `http://localhost:3000`.

---

## 5. Como Rodar com Docker

### Build e execução local

```bash
docker-compose build
docker-compose up
```

Acesse `http://localhost:3000`.

### Variáveis de ambiente

Configure as variáveis no arquivo `.env` (veja `.env.example`).

---

## 6. Deploy Multicloud (Exemplo AWS)

- **ECS/Fargate:** Use a imagem gerada pelo Dockerfile.
- **EKS/Kubernetes:** Adapte o deployment.yaml para usar a imagem.
- **Amplify/Elastic Beanstalk:** Suporte nativo a Next.js.
- **Variáveis de ambiente:** Sempre configure via painel da cloud ou secrets manager.

---

## 7. Customização

- **Remova ou adapte páginas, componentes e estilos conforme seu domínio.**
- **Renomeie rotas, interfaces e serviços para refletir o propósito do seu produto.**
- **Adicione integrações (APIs, Auth, etc.) conforme necessário.**

---

## 8. Scripts Úteis

- `bun dev` — Desenvolvimento
- `bun build` — Build de produção
- `bun start` — Start em produção
- `bun lint` — Lint do código

---

## 9. Boas Práticas e Dicas

- **Use variáveis de ambiente para todas as URLs e segredos.**
- **Mantenha o Dockerfile enxuto e seguro.**
- **Adote CI/CD (GitHub Actions já incluso).**
- **Documente customizações no README do seu projeto derivado.**

---

## 10. Links Úteis

- [Next.js Docs](https://nextjs.org/docs)
- [Bun Docs](https://bun.sh/docs)
- [Ant Design](https://ant.design/components/overview/)
- [AWS ECS](https://docs.aws.amazon.com/ecs/)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)

---

> **Este boilerplate é um ponto de partida. Remova, adapte e evolua conforme as necessidades do seu produto!**
