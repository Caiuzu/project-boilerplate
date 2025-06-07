# Catálogo Detalhado de Modelos de IA para o Hackathon

Este documento é um guia de referência rápida e um catálogo detalhado de modelos de Inteligência Artificial (IA), com foco em opções open-source (especialmente do Hugging Face) e alguns modelos comerciais relevantes. O objetivo é auxiliar as equipes na escolha do modelo mais adequado para seu projeto no hackathon, considerando fatores como tarefa, desempenho, requisitos de hardware e licença.

As informações aqui são uma expansão do que foi introduzido em `tech_stack_guide.md`.

## Filosofia de Escolha de Modelos para o Hackathon

* **Foco no Zero-Shot ou Fine-Tuning Mínimo:** Dado o tempo limitado, priorize modelos que funcionem bem "out-of-the-box" para sua tarefa ou que necessitem de um fine-tuning muito rápido (ex: LoRA em datasets pequenos).
* **Hardware Realista:** Considere o hardware disponível para a equipe (GPUs, RAM). Modelos menores e quantizados são muitas vezes a melhor escolha.
* **Licenças Permissivas:** Para demos públicas, prefira modelos com licenças como Apache 2.0, MIT ou a Llama 3 Community License.
* **Velocidade de Iteração:** Escolha modelos com APIs simples ou que se integrem facilmente com bibliotecas como `transformers` do Hugging Face.

## Catálogo de Modelos de IA

Esta seção apresenta uma compilação de modelos de IA, com contexto para cada um, visando auxiliar na escolha para o hackathon.

### 1. Modelos Gerais de Linguagem (LLMs)

| Modelo                       | Tipo & Tamanho                                     | Idiomas (Foco PT-BR)      | Licença                        | HW Mínimo (VRAM/RAM)           | Melhor Uso no Hackathon                                                                  | Por que Escolher?                                                                                                |
| ---------------------------- | -------------------------------------------------- | ------------------------- | ------------------------------ | ------------------------------ | ---------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------- |
| **Mixtral-8x7B-Instruct**    | MoE LLM • 45B (ativa ~13B por token)               | Multilíngue (PT bom)      | Apache-2.0                     | 16GB VRAM (FP16) / 12GB (INT8) | Raciocínio complexo, sumarização de textos longos, geração de código, prompt-chaining.  | Excelente performance para seu tamanho "ativo", rápido. Ótimo para tarefas que exigem profundidade.                    |
| **Llama-3-8B-Instruct**      | LLM • 8B                                           | Inglês (PT muito bom)     | Llama 3 Community License      | 8GB VRAM (FP16) / 6GB (INT8)   | Chatbots, Q&A, geração de texto coeso em PT-BR, assistência de escrita.                 | Melhor LLM open-source na faixa de 8B params para PT-BR. Ótimo balanço entre performance e requisitos de hardware. |
| **Mistral-7B-Instruct v0.3** | LLM • 7B                                           | Multilíngue (PT bom)      | Apache-2.0                     | 8GB VRAM (FP16) / 5GB (INT8)   | Q&A rápido, geração de texto curta, classificação de intenção, funções de assistente.  | Um dos mais rápidos na categoria 7B, bom custo-benefício para tarefas que não exigem contexto muito longo.        |
| **Zephyr-7B-β**              | LLM • 7B (fine-tuned para conversação)             | Multilíngue (PT razoável) | MIT                            | 8GB VRAM (FP16) / 5GB (INT8)   | Chatbots com tom amigável, atendimento ao cliente, role-playing.                       | Especificamente treinado para ser um bom conversador.                                                                |
| **Qwen1.5-7B-Chat**          | LLM • 7B                                           | Multilíngue (PT bom)      | Apache-2.0                     | 8GB VRAM (FP16) / 5GB (INT8)   | RAG (Retrieval Augmented Generation) com documentos longos (contexto de 32k tokens).   | Bom para PT-BR e se destaca pela janela de contexto grande para um modelo 7B.                                      |
| **Phi-3-mini-4k-instruct**   | LLM • 3.8B                                         | Inglês (entende PT)       | MIT                            | 6-8GB RAM (CPU, GGUF) / 4GB VRAM | Respostas curtas, tarefas em dispositivos com pouca RAM, geração de código simples.        | Surpreendentemente capaz para seu tamanho. Roda bem em CPU.                                                          |
| **google/gemma-7b-it**       | LLM • 7B                                           | Multilíngue (PT bom)      | Gemma Terms of Use             | 8GB VRAM (FP16) / 5GB (INT8)   | Chat, Q&A, sumarização, geração de código. Similar ao Llama/Mistral 7B.                | Modelo do Google, boa performance geral.                                                                             |
| **Nous-Hermes-2-Mistral-7B** | LLM • 7B (Mistral fine-tuned)                      | Inglês (PT razoável)      | Apache-2.0                     | 8GB VRAM (FP16) / 5GB (INT8)   | Geração de texto criativo, role-playing, brainstorming.                                | Fine-tuning do Mistral-7B focado em instruções e conversas mais elaboradas.                                         |
| **Llama-3-70B-Instruct**     | LLM • 70B                                          | Inglês (PT muito bom)     | Llama 3 Community License      | 2x 24GB VRAM (ou 1x 40GB+)     | Tarefas que exigem raciocínio profundo, análise de documentos complexos, geração de alta qualidade. | Top-tier open-source, mas exige hardware parrudo. Usar se GPUs potentes estiverem disponíveis.                    |

### 2. Modelos de Código e Desenvolvimento (IaC, SQL, etc.)

| Modelo                   | Tipo & Tamanho     | Idiomas (Foco)   | Licença    | HW Mínimo         | Melhor Uso no Hackathon                                                                 | Por que Escolher?                                                                         |
| ------------------------ | ------------------ | ---------------- | ---------- | ----------------- | --------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------- |
| **StarCoder2-7B**        | Code LLM • 7B      | Múltiplas (Python, JS, Java, IaC) | BigCode OpenRAIL-M | 8GB VRAM          | Geração de código (Terraform, Helm, Kubernetes YAML), unit tests, scripts de automação. | Bom para DevEx, autocomplete, e gerar boilerplate de IaC. Contexto de 16k.                |
| **CodeLlama-7B-SQL**     | Code LLM • 7B      | SQL, Python      | Llama Community | 8GB VRAM          | Geração e explicação de queries SQL, criação de schemas, documentação de bancos de dados. | Especializado em SQL, útil para projetos de Data Mesh e governança de dados.                 |
| **CodeLlama-13B-Python** | Code LLM • 13B     | Python, Go       | Llama Community | 16GB VRAM         | Geração de código Python/Go, scripts ETL, funções serverless (lambdas).                | Melhor performance em Python que os 7B, bom para lógica de backend mais complexa.         |
| **DeepSeek-Coder-6.7B**  | Code LLM • 6.7B    | Múltiplas        | MIT        | 8GB VRAM          | Geração de código focado em APIs, integração de sistemas, scaffold de microserviços.      | Treinado com foco em APIs e desenvolvimento de software prático.                            |

### 3. Modelos Financeiros, Legais e ESG

| Modelo                          | Tipo & Especialidade                        | Idiomas (Foco PT-BR) | Licença            | HW Mínimo        | Melhor Uso no Hackathon                                                                          | Por que Escolher?                                                                                             |
| ------------------------------- | ------------------------------------------- | -------------------- | ------------------ | ---------------- | ------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------- |
| **FinBERT-Sentiment**           | BERT • Classificação de Sentimento (Fin)    | Inglês               | Apache-2.0         | <4GB RAM (CPU)   | Análise de sentimento em notícias financeiras, tweets sobre ações, para score de risco de mercado. | Leve, específico para finanças.                                                                                |
| **FinGPT-v4-small (6B)**      | LLM • Q&A e Geração de Texto (Fin)        | Inglês (treinado)    | MIT                | 8GB VRAM         | Chatbots sobre DREX, mercado de capitais, análise de tendências financeiras.                     | Focado em finanças, pode ter conhecimento específico do domínio. (Verificar disponibilidade do checkpoint) |
| **FinPAL-6B (Exemplo)**       | LLM • (Pode não existir, ilustrativo)       | PT-BR (hipotético)   | -                  | 8GB VRAM         | Chatbot para tirar dúvidas sobre produtos financeiros do Itaú, em português.                     | Se existisse um modelo assim, seria ideal para interações em PT-BR sobre o sistema financeiro.            |
| **Legal-BERT-pt-br**            | BERT • Entendimento Jurídico (PT-BR)        | PT-BR                | MIT / Apache-2.0   | <4GB RAM (CPU)   | Classificar cláusulas contratuais (LGPD), identificar dados sensíveis em textos legais.      | Específico para o português do Brasil e para o domínio legal. Leve.                                         |
| **ClimateBERT**                 | BERT • NLP para Documentos ESG              | Inglês (PT razoável) | Apache-2.0         | <4GB RAM (CPU)   | Extrair KPIs de sustentabilidade de relatórios, classificar notícias sobre impacto ambiental.    | Focado em ESG, útil para projetos de pegada de carbono ou análise de relatórios de sustentabilidade.       |

### 4. Modelos de Séries Temporais e Dados Tabulares

| Modelo                   | Tipo & Especialidade                      | HW Mínimo (Aprox) | Melhor Uso no Hackathon                                                                                             | Por que Escolher?                                                                                                |
| ------------------------ | ----------------------------------------- | ----------------- | ------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------- |
| **TimeGPT-1 (via API HF)** | Modelo Fundacional • Forecast Multivariado | API (sem HW local) | Previsão de custos de cloud, demanda de transações Pix, consumo de energia em data centers.                         | Fácil de usar (API), bom para forecasting rápido sem necessidade de treinar modelos do zero.                     |
| **tsai/PatchTST-large**  | Transformer (Visão) • Outlier & Forecast  | 8GB VRAM          | Detecção de anomalias em transações financeiras, previsão de séries temporais longas (ex: comportamento de ações). | Abordagem moderna para séries temporais, pode capturar padrões complexos. Roda em GPU única.                     |
| **TabPFN**               | Transformer • Classificação Tabular (0-shot) | CPU (<2GB RAM)    | Scoring de risco de crédito rápido com poucos dados, detecção de fraude em dados tabulares, sem treino.             | Incrivelmente rápido para inferência em dados tabulares, não requer treinamento (zero-shot). Ideal para MVPs. |

### 5. Modelos de Áudio e Visão

| Modelo                    | Tipo & Tamanho                             | Idiomas (Áudio) | Licença        | HW Mínimo       | Melhor Uso no Hackathon                                                                         | Por que Escolher?                                                                                              |
| ------------------------- | ------------------------------------------ | --------------- | -------------- | --------------- | ----------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------- |
| **Whisper-large-v3 (HF)** | ASR (Audio-to-Text) • 1.5B                 | Multilíngue (PT excelente) | MIT            | 8-10GB VRAM     | Transcrição de áudio de call center para análise, comandos de voz para Pix, assistentes de voz. | State-of-the-art em transcrição open-source, robusto a ruídos e sotaques.                                  |
| **Whisper-small-pt (HF)** | ASR (Audio-to-Text) • ~240MB (específico PT) | PT-BR           | MIT            | CPU / <4GB VRAM | Transcrição em português se VRAM for muito limitada.                                            | Leve, específico para PT-BR, mas pode ser mais lento e menos preciso que o Large.                             |
| **LLaVA-1.6-Mistral-7B**  | VLM (Vision-Language Model) • ~7B          | Inglês (texto)  | Apache-2.0     | 8-10GB VRAM     | Analisar imagens de documentos (OCR + entendimento), detectar objetos em agências, ler dashboards. | Combina LLM Mistral-7B com capacidades de visão. Bom para tarefas multimodais.                                  |
| **Moondream2**            | VLM (Vision-Language Model) • 1.8B         | Inglês (texto)  | Apache-2.0     | <6GB VRAM       | Análise de imagens em dispositivos com menos VRAM, IoT com câmeras (ex: contagem de pessoas em filas). | Pequeno e eficiente para um VLM, pode rodar em hardware mais modesto.                                         |

### 6. Modelos para Embeddings e Busca Semântica

| Modelo                         | Tipo & Tamanho                   | HW Mínimo (CPU) | Melhor Uso no Hackathon                                                                   | Por que Escolher?                                                                                      |
| ------------------------------ | -------------------------------- | --------------- | ----------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------ |
| **sentence-transformers/all-MiniLM-L6-v2** | Sentence Encoder • 22MB          | <1GB RAM        | Embeddings para busca semântica, similaridade de documentos, clustering, RAG (retriever). | Muito leve e rápido, excelente para gerar embeddings de sentenças/parágrafos em CPU.                     |
| **intfloat/multilingual-e5-large**   | Sentence Encoder • ~1.3GB        | <2GB RAM        | Embeddings multilíngues de alta qualidade para RAG, busca cross-lingual.                  | Ótima performance em benchmarks de embedding multilíngue (MTEB).                                       |
| **hkunlp/instructor-xl**       | Sentence Encoder • 1.5B          | <2GB RAM        | Embeddings que seguem instruções (ex: "represente a questão para busca em FAQs").       | Permite customizar o tipo de embedding gerado com base na tarefa, potencialmente melhorando a relevância. |

## Modelos Comerciais (Opções de Fallback Rápido)

Se os modelos open-source não atenderem rapidamente ou se precisar de capacidades muito específicas, as APIs comerciais são um ótimo plano B. Geralmente oferecem free tiers ou créditos iniciais suficientes para um hackathon.

| Serviço                     | Prova Grátis Típica        | Uso Comum no Hackathon                                                            | Pontos Fortes                                                              |
| --------------------------- | -------------------------- | --------------------------------------------------------------------------------- | -------------------------------------------------------------------------- |
| **OpenAI API (GPT-4o, GPT-3.5-turbo)** | Créditos Iniciais (Trial)  | Geração de código, brainstorming, Q&A complexo, sumarização, verificação factual. | Modelos muito capazes, fáceis de usar, GPT-4o é multimodal.                 |
| **Anthropic Claude API (Claude 3 Opus, Sonnet, Haiku)** | Créditos Iniciais (Trial)  | Análise de documentos longos (contexto de 200k+ tokens), RAG, redação cuidadosa. | Excelente para contextos longos, bom em seguir instruções complexas.         |
| **Google AI Studio / Vertex AI (Gemini 1.5 Pro, Flash, Ultra)** | Free Tier / Créditos     | Q&A em PT-BR, multimodalidade, embeddings, busca semântica, personalização.     | Modelos poderosos do Google, Gemini 1.5 Pro tem janela de contexto de 1M tokens. |

## Dicas Rápidas de Hardware e Seleção no Dia do Hackathon

**Referência Rápida VRAM (FP16) vs. Tamanho do Modelo:**

* **< 4-6 GB VRAM (ou CPU + 8GB RAM):** Modelos até ~3B (Phi-3-mini, Gemma 2B), modelos BERT, modelos de embedding pequenos, modelos quantizados (GGUF/AWQ) de até 7B.
* **8 GB VRAM (GPU de Laptop Gamer Típica):** Modelos de 7-8B em FP16 ou INT8 (Mistral-7B, Llama-3-8B, Qwen1.5-7B, Gemma 7B).
* **12-16 GB VRAM (Workstation ou GPU Desktop Boa):** Modelos de 13-15B (CodeLlama-13B), Mixtral-8x7B (usa quantization ou offloading).
* **>24 GB VRAM (GPUs de Data Science como A100, RTX 4090/3090):** Modelos de até 70B (Llama-3-70B), múltiplos modelos menores rodando em paralelo.

**Checklist para Escolha Rápida:**

1. **Qual é a Tarefa Principal?** (Geração de texto, código, Q&A, classificação, áudio, visão, RAG, etc.) → Filtre as tabelas por tipo.
2. **Qual Hardware Disponível?** (VRAM da GPU, RAM da CPU) → Filtre por "HW Mínimo". Considere quantização (GGUF para CPU, AWQ/GPTQ para GPU) para rodar modelos maiores em hardware limitado.
3. **Precisa de PT-BR Fluente?** Priorize modelos com bom suporte a PT-BR (Llama-3, Qwen1.5, Gemma, Mistral-7B-Instruct, Whisper-large para áudio).
4. **Licença é Permissiva?** (Apache-2.0, MIT, Llama Community são geralmente OK para demos).
5. **Quão Rápido Precisa Ser?** Modelos menores (<10B) geralmente têm latência menor.

**Dicas Adicionais:**

* **Prompt Engineering Primeiro:** Antes de pensar em fine-tuning, explore diferentes prompts. Muitas vezes, um bom prompt resolve o problema.
* **Cache Local de Modelos:** Configure `HF_HOME` para um diretório local (`export HF_HOME=/path/to/my/hf_cache`) para evitar re-downloads e economizar tempo/internet no evento.
* **Plano B com APIs Comerciais:** Tenha chaves de API e scripts básicos prontos para OpenAI, Claude ou Gemini como fallback caso os modelos locais não funcionem como esperado ou o tempo fique apertado.
* **Ambientes Virtuais:** Use `conda` ou `venv` para isolar dependências de cada projeto/modelo.

## Exemplo de Snippet de Uso Genérico (Hugging Face Transformers)

Este snippet demonstra como carregar e usar um modelo de linguagem causal do Hugging Face, com opções para rodar em CPU, GPU e com quantização em 8-bit para economizar VRAM.

```python
from transformers import AutoTokenizer, AutoModelForCausalLM, pipeline
import torch

# --- Configurações ---
# Substitua pelo ID do modelo desejado do Hugging Face Hub
MODEL_ID = "mistralai/Mistral-7B-Instruct-v0.3" # Exemplo: Mistral 7B
# MODEL_ID = "meta-llama/Llama-2-7b-chat-hf" # Exemplo: Llama 2 7B (requer acesso)
# MODEL_ID = "google/gemma-2b-it" # Exemplo: Gemma 2B

# --- Detecção de Hardware e Configuração do Device ---
if torch.cuda.is_available():
    DEVICE = "cuda"
    # Para GPUs com menos VRAM, carregar em 8-bit pode ajudar
    # No entanto, nem todos os modelos suportam bem ou podem ter perda de performance.
    # Teste se load_in_8bit=True funciona para seu modelo e hardware.
    LOAD_IN_8BIT = True # Mude para False se não quiser 8-bit ou der erro
    if LOAD_IN_8BIT:
        print(f"CUDA disponível. Tentando carregar {MODEL_ID} em 8-bit no device: {DEVICE}")
    else:
        print(f"CUDA disponível. Carregando {MODEL_ID} em precisão padrão no device: {DEVICE}")
else:
    DEVICE = "cpu"
    LOAD_IN_8BIT = False # 8-bit não é tão relevante/otimizado para CPU com transformers da mesma forma
    print(f"CUDA não disponível. Carregando {MODEL_ID} no device: {DEVICE}")

# --- Carregar Tokenizador e Modelo ---
try:
    tokenizer = AutoTokenizer.from_pretrained(MODEL_ID, trust_remote_code=True)
    
    if DEVICE == "cuda" and LOAD_IN_8BIT:
        model = AutoModelForCausalLM.from_pretrained(
            MODEL_ID,
            torch_dtype=torch.float16, # Usar float16 com 8-bit
            load_in_8bit=True,
            device_map="auto", # Distribui o modelo automaticamente se múltiplas GPUs
            trust_remote_code=True
        )
    else:
        model = AutoModelForCausalLM.from_pretrained(
            MODEL_ID,
            torch_dtype=torch.float16 if DEVICE == "cuda" else torch.float32, # float32 para CPU
            device_map=DEVICE, # Mapeia para o dispositivo único (CPU ou uma GPU)
            trust_remote_code=True
        )
    
    # Se device_map="auto" ou load_in_8bit=True não for usado, pode ser necessário mover o modelo para o device:
    # if not (DEVICE == "cuda" and LOAD_IN_8BIT): # Se não usou device_map ou 8bit que já mapeia
    # model.to(DEVICE)

except Exception as e:
    print(f"Erro ao carregar o modelo ou tokenizador: {e}")
    print("Verifique o MODEL_ID, sua conexão com a internet, e se você tem permissão para acessar o modelo (para modelos restritos como Llama).")
    exit()

# --- Criar Pipeline de Geração de Texto ---
# Para modelos "instruct" ou "chat", o prompt geralmente precisa ser formatado.
# Verifique a "model card" do modelo no Hugging Face para o formato de prompt correto.

# Exemplo de formatação para Mistral Instruct (pode variar para outros)
# messages = [
# {"role": "user", "content": "Explique o que é FinOps em duas frases."}
# ]
# prompt_formatted = tokenizer.apply_chat_template(messages, tokenize=False, add_generation_prompt=True)

# Exemplo de prompt simples (ajuste conforme o modelo)
raw_prompt = "Explique o que é um Hackathon em 3 frases."

# Se o modelo for do tipo chat e tiver um template de chat, use-o:
if hasattr(tokenizer, 'apply_chat_template') and callable(tokenizer.apply_chat_template):
    try:
        chat_messages = [{"role": "user", "content": raw_prompt}]
        formatted_prompt = tokenizer.apply_chat_template(chat_messages, tokenize=False, add_generation_prompt=True)
        print(f"\nUsando template de chat. Prompt formatado: {formatted_prompt}")
    except Exception as e:
        print(f"Erro ao aplicar template de chat, usando prompt raw: {e}")
        formatted_prompt = raw_prompt
else:
    formatted_prompt = raw_prompt
    print(f"\nModelo não parece ter um template de chat. Usando prompt raw: {formatted_prompt}")


text_generator = pipeline(
    "text-generation",
    model=model,
    tokenizer=tokenizer,
    device=model.device # Garante que o pipeline usa o mesmo device do modelo
)

# --- Gerar Texto ---
print("\nGerando texto...")
try:
    generated_outputs = text_generator(
        formatted_prompt,
        max_new_tokens=100,       # Número máximo de novos tokens a serem gerados
        do_sample=True,           # Ativa a amostragem (mais criativo, menos determinístico)
        temperature=0.7,          # Controla a aleatoriedade (valores menores = mais determinístico)
        top_k=50,                 # Considera os k tokens mais prováveis em cada passo
        top_p=0.95,               # Considera o menor conjunto de tokens cuja probabilidade acumulada excede p
        num_return_sequences=1    # Número de sequências a serem retornadas
    )

    for i, output in enumerate(generated_outputs):
        print(f"\n--- Resposta {i+1} ---")
        # O texto gerado pode incluir o prompt, dependendo do modelo e do pipeline.
        # Muitas vezes, você precisa extrair apenas a parte nova.
        # A forma de fazer isso pode variar.
        full_text = output['generated_text']
        # Simples tentativa de remover o prompt (pode precisar de ajuste)
        if formatted_prompt in full_text:
            generated_part = full_text.split(formatted_prompt, 1)[-1].strip()
        else: # Se o prompt não estiver exatamente no início, pode ser mais complexo
            # Para modelos instruct, o prompt pode ser diferente do texto gerado.
            # Vamos apenas mostrar o texto completo neste caso genérico.
            print("(Nota: O prompt pode estar incluído abaixo ou a separação falhou)")
            generated_part = full_text.strip()
        
        print(generated_part)

except Exception as e:
    print(f"Erro durante a geração de texto: {e}")

```

Este catálogo visa ser um ponto de partida dinâmico. A área de IA evolui rapidamente, então sempre verifique o Hugging Face Hub e outras fontes para os modelos mais recentes e relevantes antes do hackathon.
