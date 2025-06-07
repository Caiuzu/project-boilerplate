# RESUMO DO MONO-REPO HACKATHON IA + MULTI-CLOUD

Este documento serve como um índice e visão geral de tudo que existe na codebase, incluindo os projetos, documentação e playbooks.

---

## Estrutura Geral do Repositório

- **backend-java/**: Backend em Java (Spring Boot, Gradle Kotlin DSL). API REST, exemplos de integração com banco, Swagger, Makefile, Docker.
- **backend-python/**: Backend em Python (FastAPI). Pronto para IA, exemplos de integração com LLMs, Makefile, Docker, scripts de setup e download de modelos.
- **backend-golang/**: Backend em Go (net/http, Swagger). Estrutura modular, Makefile, Docker, exemplos de API REST.
- **backend-kotlin/**: Backend em Kotlin (Spring Boot). Estrutura moderna, Makefile, Docker.
- **frontend-nextjs/**: Frontend em Next.js (TypeScript, Ant Design). Estrutura de app router, exemplos de integração com backend, Makefile, Docker.
- **infra/**: Infraestrutura como código (Terraform para AWS/GCP/Azure, Helm para K8s, LocalStack para simulação cloud local). Scripts, exemplos e documentação detalhada.
- **docs/**: Documentação geral do projeto, guias estratégicos, playbooks e catálogo de modelos de IA.

---

## Detalhamento dos Diretórios de Documentação

### docs/

- **STACK_OVERVIEW.md**: Visão geral da stack tecnológica, padrões de código, convenções de nomenclatura, links úteis para cada serviço, diagrama de interação dos serviços, padrões de commit e fluxo de branch.
- **PLAYBOOK/**: Diretório com guias práticos e estratégicos para hackathon.

#### docs/PLAYBOOK/

- **templates_and_boilerplates.md**: Links e dicas para templates, boilerplates e exemplos de código para frontend, backend, multi-cloud, IaC e modelos de IA. Inclui comandos, recomendações e exemplos práticos.
- **workflow.md**: Fluxo de trabalho ágil para hackathon, do entendimento do desafio ao pitch. Inclui Lean Canvas, priorização, arquitetura, desenvolvimento iterativo, preparação do pitch e checklist de entrega.
- **tech_stack_guide.md**: Guia de tecnologias de IA e multi-cloud, com dicas de escolha, exemplos de uso, justificativas para o pitch e recomendações para backend, frontend e DevOps.
- **team_setup.md**: Estrutura sugerida de equipe, papéis, responsabilidades, rituais ágeis mínimos, ferramentas de colaboração e cadência recomendada para hackathon.
- **hackathon_analysis.md**: Análise de hackathons anteriores do Itaú e Cubo Itaú, temas recorrentes, padrões de equipes vencedoras, recomendações estratégicas e ideias de projetos para cada tema macro.
- **ai_models_catalog.md**: Catálogo detalhado de modelos de IA (open-source e comerciais), com tabelas comparativas, dicas de hardware, exemplos de uso e recomendações para cada tarefa (LLMs, embeddings, visão, áudio, etc).
- **pitch_guide.md**: Guia completo para criar e apresentar um pitch vencedor, estrutura de slides, checklist de storytelling, preparação para Q&A e dicas para a apresentação final.

---

## Como Navegar e Usar

- Consulte o [README.md](./README.md) para instruções de uso, comandos e visão geral rápida.
- Use este RESUMO.md como índice para encontrar rapidamente o conteúdo desejado.
- Para detalhes de cada stack, veja os READMEs dentro de cada subdiretório de projeto.
- Para dicas de hackathon, estratégias, exemplos e modelos de IA, explore os documentos do diretório `docs/PLAYBOOK/`.
