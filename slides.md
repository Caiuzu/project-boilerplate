### Slide 1 – Gancho: O Custo Invisível da Observabilidade

- Bancos perdem milhões/ano com ruído, recursos zumbis e falta de padronização em ambientes multicloud.
- E se fosse possível automatizar a detecção, sugerir correções e conversar sobre custos em tempo real?
- **[Dica visual:]** Fundo escuro, número de impacto, logo Itaú/Cubo discreto.

---

### Slide 2 – O Problema & Métrica-NORTE

- Desperdício massivo: logs, métricas e spans inúteis aumentam custos.
- Recursos zumbis consomem orçamento.
- Falta de padronização dificulta governança.
- **Métrica-NORTE:** Reduzir em 30% o custo de observabilidade e detectar span inútil em <10min.
- **[Dica visual:]** Gráfico de pizza ou barra mostrando o “antes” e a meta.

---

### Slide 3 – Persona & Jornada Atual

- Persona: Engenheiro(a) de Observabilidade/FinOps em grandes bancos.
- Jornada: Gasta horas caçando ruído, justificando custos e tentando padronizar práticas entre dezenas de times.
- **[Dica visual:]** Mapa de jornada com emojis de dor/frustração.

---

### Slide 4 – Nossa Solução: FOG – FinOps & Observability Guardian

- Plataforma multicloud que detecta desperdício, padroniza rastros e conversa com os times via chat-LLM.
- Propostas de valor:
  - Padronização preventiva (PR Guard)
  - Redução sistemática de custos & ruído (Analyzers + GhostOps)
  - Insights conversacionais (FinOpsGPT)
  - Correções prontas via Auto-PR
- **[Dica visual:]** Logo da solução, tagline curta, mock da tela inicial.

---

### Slide 5 – Demo ao Vivo (MVP)

- AutoTrace Simplifier: agrupa spans repetidos e sugere cortes.
- Observability Bloat Analyzer: heatmap de logs/métricas duplicados.
- GhostOps Detector: caça recursos zumbis.
- FinOpsGPT: copiloto em PT-BR para dúvidas de custo.
- PR Guard: valida diffs das PRs com LLM.
- Report & Alert Engine: PDF diário, alertas críticos.
- Ciclo Auto-PR: Lambda gera PR draft → revisão humana → merge.
- **[Dica visual:]** Screencast curto ou prints do MVP.

---

### Slide 6 – Arquitetura Multi-Cloud

- Ingestão: OTEL, Loki/Tempo/Mimir, Cost Explorer, GCP/Azure exports.
- Processamento/IA: SageMaker (Llama-3-8B), Glue, Lambdas.
- Persistência: S3 Lake, Aurora PG, DynamoDB.
- Interface: Grafana, QuickSight, Teams bot.
- Escala: 12.000 contas AWS, dados multicloud no mesmo pipeline.
- **[Dica visual:]** Diagrama simples com logos das clouds e principais serviços.

---

### Slide 7 – Inteligência Artificial em Ação

- FinOpsGPT: LLM em PT-BR para explicação de custos e recomendações.
- PR Guard: LLM valida padrões de código e sugere melhorias.
- Analyzers: IA detecta padrões de ruído e desperdício.
- **[Dica visual:]** Pipeline de IA ou exemplo de output.

---

### Slide 8 – Impacto & ROI

- Detectar span inútil <10min.
- -30% logs armazenados em 90 dias.
- ≥15% de economia com GhostOps.
- 100% repositórios novos usando PR Guard até Q2.
- Insights traduzidos para “R$ economia”.
- **[Dica visual:]** Tabela antes/depois, gráfico de barras.

---

### Slide 9 – Roadmap (Próximos Passos)

- Integração com mais clouds (OCI, IBM).
- FinOpsGPT em inglês/espanhol.
- Módulo de remediação automática.
- Marketplace de plugins de análise.
- **[Dica visual:]** Linha do tempo com marcos futuros.

---

### Slide 10 – Equipe & Superpoderes

- Engenheiros(as) de Observabilidade, FinOps, IA e DevOps.
- Experiência prática em multicloud, automação e IA generativa.
- Foco em impacto real e entregas rápidas.
- **[Dica visual:]** Mosaico de fotos/avatares e “superpoder” de cada um.

---

### Slide 11 – O “Ask” (Chamada à Ação)

- Sandbox para integração com ambientes reais do Itaú.
- Mentoria de times de FinOps e Observabilidade.
- Acesso a dados anonimizados para refinar os modelos.
- **[Dica visual:]** Logo Itaú em destaque, frase de call to action.
