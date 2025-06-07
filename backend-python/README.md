# Backend Python - Hackathon API (FastAPI)

Este serviço é um backend em Python utilizando FastAPI, ideal para desenvolvimento rápido de APIs, incluindo aquelas que servem modelos de Inteligência Artificial. O projeto já está pronto para uso com LLMs, embeddings, séries temporais, áudio, visão computacional, tabular, e integrações com APIs comerciais e open-source.

## 1. Visão Geral

- **API pronta para produção e hackathons**: FastAPI, Swagger, Makefile, setup.sh, exemplos de uso de IA.
- **Suporte a LLMs, embeddings, áudio, visão, tabular, séries temporais** (HuggingFace, OpenAI, Groq, etc).
- **Pronto para rodar modelos localmente ou consumir via cloud (Inference API Hugging Face, OpenAI, Groq, etc).**
- **Documentação automática via Swagger UI e ReDoc.**

## 2. Estrutura de Pastas

```
backend-python/
├── Dockerfile           # Imagem Docker para o serviço
├── requirements.txt     # Dependências Python (ML, LLM, cloud, etc)
├── Makefile             # Comandos utilitários (run, lint, baixar modelos, etc)
├── setup.sh             # Script de setup automático (instala Python, venv, dependências)
├── download_models.py   # Script para baixar modelos do HuggingFace localmente
├── app/
│   ├── main.py          # FastAPI principal
│   └── model_examples.py # Exemplos de uso de todas as libs/modelos
└── README.md            # Este arquivo
```

## 3. Tecnologias e Bibliotecas

- **FastAPI**: API web moderna, rápida e com docs automáticas.
- **Uvicorn**: Servidor ASGI para FastAPI.
- **Pydantic**: Validação de dados.
- **transformers, sentence-transformers**: LLMs, embeddings, visão, áudio.
- **openai, groq**: APIs comerciais e open-source para LLMs.
- **huggingface-hub**: Inference API cloud da Hugging Face.
- **tabpfn, tsai, scikit-learn, pandas, numpy**: Tabular, séries temporais, ML tradicional.
- **langchain, faiss-cpu**: Pipelines de LLMs, busca vetorial.
- **librosa, soundfile, PIL**: Áudio e visão computacional.

## 4. Como Rodar Localmente

```bash
# Setup automático (recomendado)
./setup.sh
# Ative o ambiente virtual
source .venv/bin/activate
# Rode o servidor
make run
```

## 5. Como Baixar Modelos do HuggingFace Localmente

```bash
# Exemplo: baixar dois modelos para a pasta ./models
make download MODELS="mistralai/Mistral-7B-Instruct-v0.3 sentence-transformers/all-MiniLM-L6-v2"
```

- Os modelos ficam em `./models/<nome-do-modelo>` e podem ser usados localmente via transformers.

## 6. Documentação Interativa (Swagger)

- Swagger UI: [http://localhost:8000/docs](http://localhost:8000/docs)
- ReDoc: [http://localhost:8000/redoc](http://localhost:8000/redoc)

## 7. Exemplos de Endpoints e Uso de IA

Veja o arquivo `app/model_examples.py` para exemplos práticos de uso de todas as bibliotecas e modelos suportados.

---

<details>
<summary><strong>🚀 Como usar modelos da Hugging Face direto da nuvem (Inference API e pipeline cloud)</strong></summary>

### 1. Usando o pipeline do transformers (cloud ou local)

Ideal para PoCs rápidas. O pipeline baixa e executa o modelo automaticamente (local ou cloud, se disponível):

```python
from transformers import pipeline

pipe = pipeline("text-classification", model="distilbert-base-uncased-finetuned-sst-2-english")
result = pipe("I love this!")
print(result)
```

- Se o modelo não estiver local, será baixado automaticamente.
- Se não houver GPU/infra, pode ser mais lento.

### 2. Usando a Inference API da Hugging Face (cloud)

Ideal para desenvolvimento em locais com internet instável ou sem GPU local. Você consome o modelo direto da nuvem da Hugging Face:

```python
from huggingface_hub import InferenceClient

client = InferenceClient(token="<seu_token_hf>")
response = client.text_generation("Qual o propósito da vida?", model="tiiuae/falcon-7b-instruct")
print(response)
```

- **Vantagens:** Não precisa baixar nem rodar o modelo localmente.
- **Desvantagens:** Depende da internet e do plano da Hugging Face (há limites gratuitos e pagos).
- **Como obter o token:**
  1. Crie uma conta em <https://huggingface.co>
  2. Vá em "Settings" > "Access Tokens" > "New token" (role: "read")
  3. Use o token no código acima.
- **Dica:** Para tasks como classificação, geração de texto, embeddings, etc., basta mudar o método do `InferenceClient` (ex: `client.text_classification`, `client.feature_extraction`, etc).

</details>

---

## 8. Dicas para Hackathons e Produção

- Se a internet estiver ruim, prefira baixar os modelos antes do evento.
- Se não houver GPU, use modelos menores ou a Inference API.
- O arquivo `model_examples.py` cobre exemplos para todas as libs do requirements.txt (inclusive as comentadas).
- O Makefile e o setup.sh aceleram o setup do ambiente.

## 9. Links Úteis

- [FastAPI Documentation](https://fastapi.tiangolo.com/)
- [Uvicorn Documentation](https://www.uvicorn.org/)
- [Hugging Face Inference API](https://huggingface.co/inference-api)
- [Transformers Docs](https://huggingface.co/docs/transformers/index)
- [OpenAI API](https://platform.openai.com/docs/)
- [Groq API](https://console.groq.com/docs/openai)
- [LangChain Docs](https://python.langchain.com/docs/get_started/introduction.html)

## 10. Documentação das Bibliotecas e APIs

### APIs e Frameworks

- [FastAPI](https://fastapi.tiangolo.com/)
- [Uvicorn](https://www.uvicorn.org/)
- [Pydantic](https://docs.pydantic.dev/)
- [Docker](https://docs.docker.com/)

### LLMs, NLP, Embeddings

- [Transformers (Hugging Face)](https://huggingface.co/docs/transformers/index)
- [Hugging Face Hub](https://huggingface.co/docs/huggingface_hub/index)
- [sentence-transformers](https://www.sbert.net/)
- [OpenAI Python](https://platform.openai.com/docs/api-reference/introduction)
- [Groq Python](https://github.com/groq/groq-python)
- [LangChain](https://python.langchain.com/docs/get_started/introduction.html)

### Machine Learning, Tabular, Séries Temporais

- [scikit-learn](https://scikit-learn.org/stable/)
- [pandas](https://pandas.pydata.org/docs/)
- [numpy](https://numpy.org/doc/)
- [tabpfn](https://github.com/automl/TabPFN)
- [tsai](https://timeseriesai.github.io/tsai/)
- [torch (PyTorch)](https://pytorch.org/docs/stable/index.html)
- [tensorflow](https://www.tensorflow.org/api_docs)
- [faiss-cpu](https://github.com/facebookresearch/faiss)

### Áudio e Visão Computacional

- [librosa](https://librosa.org/doc/latest/index.html)
- [soundfile](https://pysoundfile.readthedocs.io/en/latest/)
- [Pillow (PIL)](https://pillow.readthedocs.io/en/stable/)

### Desenvolvimento e Qualidade de Código

- [black](https://black.readthedocs.io/en/stable/)
- [flake8](https://flake8.pycqa.org/en/latest/)
- [mypy](https://mypy.readthedocs.io/en/stable/)
