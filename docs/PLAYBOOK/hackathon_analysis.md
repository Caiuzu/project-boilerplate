# Análise de Hackathons Anteriores do Itaú e Cubo Itaú

Este documento consolida informações e aprendizados de edições passadas de hackathons promovidos pelo Itaú Unibanco e Cubo Itaú. O objetivo é fornecer um panorama estratégico para as equipes da TINNOVA se prepararem para o Hackathon de IA e Multi-Cloud 2025.

## Fontes Principais

* Pesquisa web (links e resumos abaixo).
* Informações do Cubo Itaú e canais oficiais do Itaú.

## Histórico e Temas Recorrentes

O Itaú Unibanco tem um histórico de promover hackathons, frequentemente chamados de "Batalha de Dados", com diversos focos temáticos. As edições mais recentes, especialmente as em parceria com o Cubo Itaú, têm explorado tecnologias emergentes e desafios de negócio relevantes.

### Temas por Edição (Baseado em Pesquisa Web e Histórico Interno)

| Ano  | Evento/Edição                      | Temas Principais                                                                 | Foco/Objetivo                                                                                                | Parceiros Notáveis |
|------|------------------------------------|----------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------|--------------------|
| 2018 | 1ª Batalha de Dados (externa)      | Educação, saúde, segurança pública                                               | Desenvolver soluções sociais utilizando dados e analytics.                                                   | -                  |
| 2018 | Itaú Insights (2ª ed.)             | Economia 4.0 (cidades inteligentes, IoT, economia compartilhada, novas profissões) | Facilitar atividades do pequeno empreendedor; otimizar processos com novas tecnologias.                      | Shawee             |
| 2021 | Batalha de Dados Social            | Gestão pública da educação                                                       | Aplicar tecnologia para auxiliar gestores públicos e profissionais da educação.                              | FGV, Inst. Unibanco|
| 2021 | Hackathon Itaú Asset (Bloomberg)   | Otimização de portfólio (algoritmo de Markowitz)                                 | Criação de "robô consultor" para otimização de portfólio e fronteira eficiente.                              | Bloomberg          |
| 2022 | Hackathon Conexão Itaú             | Soluções de pagamentos para empreendedores                                       | Criar soluções para as necessidades dos clientes empreendedores.                                             | Mastercard, Microsoft |
| 2023 | Batalha de Quantum                 | Computação quântica aplicada à otimização de portfólio                            | Desenvolver soluções utilizando algoritmos quânticos híbridos.                                               | -                  |
| 2024 | Batalha de Dados                   | Cloud computing, arquitetura data mesh                                           | Trabalhar com grande volume de dados em data mesh na nuvem, data storytelling, visualização.                 | -                  |
| 2024 | Llama Impact Hackathon             | Educação e cultura; saúde; desenvolvimento econômico; meio ambiente; questões sociais | Desenvolver soluções utilizando os modelos Llama da Meta para desafios atuais do Brasil.                       | Meta, Cubo Itaú    |
| 2025 | Hackathon de IA e Multi-Cloud      | Inteligência Artificial, Estratégias Multi-Cloud                                 | Desenvolver soluções inovadoras para problemas reais do mercado, prototipagem rápida, aprendizado prático.   | Cubo Itaú          |

### Padrões de Temas e Tecnologias

* **Impacto Social e Relevância para o Negócio:** Muitos hackathons buscam soluções com aplicabilidade real, seja para desafios sociais (educação, saúde) ou para otimizar processos e produtos do próprio Itaú (fraudes, retenção de clientes, pagamentos, investimentos).
* **Tecnologias Emergentes:** Forte inclinação para o uso de tecnologias de ponta como IA (Generativa, Machine Learning), Computação Quântica, Cloud (incluindo Data Mesh e Multi-Cloud), e análise de grandes volumes de dados.
* **Dados Abertos e APIs:** Incentivo ao uso de dados públicos e, possivelmente, APIs fornecidas pela organização ou parceiros.
* **Foco no Usuário/Cliente:** Soluções que melhorem a experiência do cliente ou resolvam dores específicas de um público-alvo (ex: pequenos empreendedores, gestores públicos).

## Características das Equipes e Projetos Vencedores (Aprendizados de Edições Anteriores e Pesquisa)

Os projetos e equipes que se destacam geralmente apresentam os seguintes atributos:

1. **Solução Clara para um Problema Real:**
    * **Problema Bem Definido:** Conseguem articular claramente qual dor estão resolvendo.
    * **Métrica de Impacto:** Quantificam o benefício da solução (ex: redução de X%, aumento de Y, tempo economizado).
    * O projeto PY Shield (2018) cruzou dados para indicar rotas seguras, um problema social claro.
    * As plataformas educacionais de 2021 atacaram a evasão escolar com metas claras.
    * A otimização de portfólio de 2023 visava superar benchmarks financeiros.

2. **MVP Funcional e Convincente:**
    * **Protótipo Navegável:** Apresentam algo que pode ser clicado e demonstrado, não apenas slides. Um MVP (Minimum Viable Product) que mostra a essência da solução.
    * **Foco na Prototipagem Rápida:** O evento de 2025 explicitamente menciona "prototipagem rápida".
    * **Tecnologia Aplicada:** Demonstram o uso efetivo das tecnologias centrais do tema do hackathon (ex: IA, algoritmos quânticos, cloud).

3. **Equipe Multidisciplinar e Colaborativa:**
    * **Diversidade de Habilidades:** Composição por desenvolvedores (backend, frontend), cientistas de dados, designers, e especialistas no domínio do problema.
    * **Sinergia:** Demonstram boa colaboração e divisão de papéis.

4. **Pitch Impactante e Bem Estruturado:**
    * **Storytelling Claro:** Comunicam a ideia, o problema, a solução e o impacto de forma concisa e envolvente.
    * **Demo ao Vivo (se possível):** Mostrar o MVP funcionando é crucial.
    * **Tempo Controlado:** Geralmente pitches curtos (ex: < 5 minutos).
    * (Veja o documento `pitch_guide.md` para um framework detalhado).

5. **Uso Estratégico de Dados e Tecnologias:**
    * **Dados Abertos/Fornecidos:** Aproveitam bases de dados disponíveis.
    * **Tecnologias do Tema:** Alinham a stack tecnológica com as expectativas do desafio (ex: Qiskit em 2023, modelos Llama em 2024).
    * **Lição:** Atenção aos detalhes técnicos, documentação clara do método (não só do código) e generalização da solução para além do exemplo fornecido.

6. **Viabilidade e Potencial de Implementação:**
    * **Soluções Práticas:** Propostas que parecem factíveis de serem implementadas ou que resolvem um problema real do Itaú ou do mercado.
    * **Escalabilidade (Consideração):** Pensar em como a solução poderia crescer ou ser adotada em maior escala.

### Feedback Específico (Exemplo do Hackathon Itaú Asset 2021 via GitHub)

* **Positivo:** Entregar pacote Python conforme solicitado; estimar corretamente matriz de covariância e fronteira eficiente.
* **Negativo:** Não documentar o algoritmo apresentado (embora o código estivesse parcialmente documentado); solução incorreta (violava vínculos); gráfico específico do exemplo e não genérico; erro no cálculo da recomendação de portfólio.
  * **Lição:** Atenção aos detalhes técnicos, documentação clara do método (não só do código) e generalização da solução para além do exemplo fornecido.

## Recomendações Gerais para Participantes

* **Formar equipe diversa.**
* **Entender profundamente o problema.**
* **Utilizar dados abertos/fornecidos estrategicamente.**
* **Desenvolver um MVP funcional.**
* **Preparar um pitch convincente.**
* **Adotar metodologias ágeis e Design Thinking.**
* **Foco no impacto social ou de negócio.**
* **Valorizar a colaboração e sinergia da equipe.**

## O Hackathon de IA e Multi-Cloud 2025

* **Tema Central:** Desenvolver soluções inovadoras utilizando Inteligência Artificial e estratégias de Multi-Cloud para resolver problemas reais do mercado.
* **Local:** Itaú - Torre Jabaquara, São Paulo, SP.
* **Foco:** Prototipagem rápida, aprendizado prático, colaboração.
* **Possíveis Tipos de Soluções Sugeridas:**
  * Automação de Processos com IA.
  * Personalização de Experiências.
  * Plataformas de Data Insights com Data Mesh.
  * Segurança e Conformidade Automatizadas.
  * Soluções ESG orientadas por IA.
  * Infraestrutura Resiliente Multi-Cloud.

## Próximos Passos

Com base nesta análise, os próximos documentos irão detalhar:

* Um guia estratégico com um fluxo de trabalho sugerido.
* Dicas aprofundadas para ideação e desenvolvimento.
* Um guia completo para o pitch.

---

*Este documento será atualizado conforme novas informações relevantes sejam encontradas.*

## Catálogo de Temas → Ideias de Projetos

*(Pensado para o Hackathon IA + Multi-Cloud, mas serve para qualquer edição Itaú/Cubo; cada tema traz caminhos que já apareceram em hackathons do banco **ou** que encaixam no core-business do Itaú em 2025)*

| **Tema macro**                                    | **Por que interessa ao Itaú / Cubo**                                     | **Ideias de projeto (MVP em 30 h)**                                                                                                                                                                                                          |
| ------------------------------------------------- | ------------------------------------------------------------------------ | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **1. FinOps & Cloud Cost Ops**                    | Itaú opera milhares de contas multi-cloud; reduzir TCO = impacto direto. | • "CloudGuard AI" (detecção de gastos zumbis)  <br>• Simulador de "spot vs reserved" com RL <br>• Previsor de pico de demanda (LLM + séries temporais) <br>• Dashboard cross-provider de carbono (AWS + GCP + Azure)                         |
| **2. Risco & Fraude em Tempo-Real**               | Compliance é core; ataques a Pix e cartões sobem todo trimestre.         | • Anomalia Pix sub-segundo (gRPC Go + FeatureStore) <br>• Verificador AML que lê listas PEP em OCR + Llama <br>• "Trust Score" para Open Finance usando graph embeddings <br>• Motor de alertas de cartão fantasma (gastos incomuns por NFC) |
| **3. Experiência do Cliente (Retenção / Upsell)** | Jornada omnichannel = meta do banco; NPS é KPI público.                  | • IA que gera outbound offer 100 % customizada via WhatsApp <br>• "Financial Coach" com LLM, integrado a Open Finance e Nubank Open APIs <br>• Recomendações ESG-fund friendly p/ investidores iniciantes                                    |
| **4. ESG & Carbono**                              | Pressão regulatória + marketing verde.                                   | • Calculadora de pegada carbono para compra no e-commerce (multi-cloud micro-serviço) <br>• Tokenização de créditos de carbono na SC-FUSE (blockchain permissionada) <br>• IA que sugere metas de redução de energia para data centers Itaú  |
| **5. Inclusão Financeira & Social**               | Responsabilidade social = critério em edições passadas.                  | • "Micro-Crédito Justo" – scoring com dados alternativos (conta de luz, celular pré-pago) <br>• Plataforma de doação redonda via Pix + cashback social <br>• Chatbot LIBRAS com síntese de vídeo para atendimento PCD                        |
| **6. Educação & Upskilling**                      | Itaú é parceiro do Instituto Unibanco.                                   | • LLM que gera trilhas de aprendizado personalizadas para funcionários <br>• Game-based learning sobre finanças para escolas públicas (React Native) <br>• Sistema de mentoria P2P que sugere matches usando embeddings de perfil            |
| **7. PIX 3.0 / Open Finance**                     | Brasil é vanguarda; BC planeja novidades anuais.                         | • "Pix Offline" via QR dinâmico + assinatura criptográfica <br>• Score de risco para Débito Direto no Pix (DDP) <br>• Motor de recomendação de portabilidade automática de salário                                                           |
| **8. Data Mesh & Governança**                     | Tema oficial da Batalha 2024; ainda quente.                              | • Gerador de contrato de dados (Schema Registry + AI) <br>• "Data Product Marketplace" – vitrine React + APIs mesh <br>• Linhagem de dados real-time com grafos (Neo4j + Kafka)                                                              |
| **9. Multi-Cloud Reliability**                    | Justifica o "multi-cloud" do evento.                                     | • Orquestrador de failover K8s → K8s em outra nuvem em < 30 s <br>• Balancer de custo x latência que move pods por spot price <br>• Observabilidade unificada (OpenTelemetry + Loki)                                                         |
| **10. Generative AI no Core Banking**             | "GenAI" foi tema de hack interno 2024.                                   | • "LLM Guardrails" – inferência segura com red teaming automático <br>• Resumo automático de PDFs contratuais (Java Spring plug-in) <br>• Gerador de scripts SQL Otimizados via prompt-to-plan                                               |
| **11. Quantum Adjacent (Híbrido)**                | Itaú já testou Qiskit; pode voltar como faixa bônus.                     | • Otimizador de liquidez intraday via QAOA-simulado <br>• Scheduler de lotes COBOL→Cloud que usa annealing quântico (D-Wave API mock)                                                                                                        |
| **12. Seguros & Saúde**                           | Itaú Seguros / Rede D'Or parceria.                                       | • IA que precifica seguro-viagem em tempo real (clima + epidemias) <br>• "Wellness Score" para vender produtos de saúde + cashback academia                                                                                                  |
| **13. Small Biz / Empreendedor**                  | Tema do Hackathon Conexão 2022.                                          | • "FacturaFlex" – adiantamento de fatura cartão com ML de risco <br>• Marketplace de maquininhas com simulação de MDR ideal <br>• Coach de fluxo de caixa (Dash + Forecast Prophet)                                                          |
| **14. Infraestrutura Regulatória (DREX / CBDC)**  | Real Digital (DREX) em piloto 2025.                                      | • Wallet white-label para DREX com script off-line <br>• Bridge entre contratos Drex e boleto inteligente <br>• Analítico de liquidez tokenizada                                                                                             |
| **15. Developer Experience interna**              | Hackathons Itaú valorizam dev-efficiency.                                | • "One-Click Sandbox" – cria stack dev no cloud em 90 s <br>• Linter de políticas IaC multi-cloud <br>• Visual Prometheus Query Builder com AI                                                                                               |
| **16. Compliance & LGPD / IA Act**                | Regulação europeia inspira Brasil.                                       | • Classificador de sensibilidade de coluna de DB via LLM <br>• "Consent Manager" – painel para usuário revogar permissão de dado <br>• Chatbot que explica cláusulas LGPD em linguagem simples                                               |
| **17. Customer Care Omnichannel**                 | Central de atendimento Itaú = +40 MM ligações/ano.                       | • "Voice-to-Ticket" (Whisper + GPT summarizer → Zendesk) <br>• Detecção de sentimento em call ao vivo @Twilio <br>• Roteamento inteligente para squads + SLA preditivo                                                                       |
| **18. Wealth & Investment Tech**                  | Itaú Personnalité foca high-net-worth.                                   | • Robo-advisor que usa embeddings de perfil + LLM geral <br>• Score de sustentabilidade em portfólio <br>• Simulador de renda variável com gamificação                                                                                       |
| **19. Marketplace Banking / APIs**                | Banco oferece APIs BaaS.                                                 | • Gateway GraphQL que unifica APIs Itaú, Stripe, AWS Pay <br>• Sandbox self-service com tokenização automática de dados                                                                                                                      |
| **20. Smart Branch / IoT**                        | Agências físicas seguem relevantes.                                      | • Heatmap de filas via câmeras edge AI (Jetson) <br>• Monitor de cofre inteligente multi-cloud MQTT → Grafana <br>• ESG: sensor CO₂ + uso ar-condicionado otimizado                                                                          |

### Como usar a lista de Temas e Ideias

1. **Escolha 2–3 temas** que se encaixem na missão da TINNOVA *e* nos perfis dos dois times (Java-heavy & Python-IA).
2. Para cada tema faça um **quick-score**:
   * Impacto de negócio (0-3).
   * Viabilidade em 30 h (0-3).
   * "Wow-factor" para banca (0-3).
   * Alinhamento com multi-cloud (0-1).
3. Foque no top-score; monte **Canvas Lean de 1 página** antes de codar (veja `workflow.md` para detalhes sobre Lean Canvas).
4. Garanta que o pitch mostre: **dor quantificada → solução → demo → KPI** (padrão vencedor dos anos anteriores, veja `pitch_guide.md`).

Esta grade cobre vetores que já renderam destaque em hackathons anteriores e introduz apostas que podem surgir como "tema surpresa".
