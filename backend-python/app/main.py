from fastapi import FastAPI, HTTPException, UploadFile, File, Form
from fastapi.responses import JSONResponse
from pydantic import BaseModel
import uvicorn
import os
import boto3
import json
from github import Github
import requests

try:
    from openai import OpenAI
except ImportError:
    OpenAI = None
try:
    import groq
except ImportError:
    groq = None
try:
    from transformers import pipeline
except ImportError:
    pipeline = None

app = FastAPI(
    title="Hackathon Python API Example",
    version="1.0.0",
    description="API de exemplo em FastAPI para pipelines de IA e outros serviços Python."
)

class HelloMessage(BaseModel):
    message: str

class Item(BaseModel):
    id: int
    name: str
    description: str | None = None

class PRRequest(BaseModel):
    repo: str  # Ex: "org/nome-repo"
    pr_number: int

@app.get("/api/hello", response_model=HelloMessage, tags=["Exemplo Python"])
async def read_root():
    """
    Retorna uma mensagem de saudação do backend Python.
    """
    return {"message": "Olá do Backend Python (FastAPI)!"}

@app.post("/api/items", response_model=Item, status_code=201, tags=["Exemplo Python"])
async def create_item(item: Item):
    """
    Endpoint de exemplo para criar um item (simulado).
    """
    # Aqui você adicionaria a lógica para salvar o item, por exemplo.
    print(f"Item recebido: {item}")
    return item

@app.post("/api/llm/openai", tags=["LLM"])
async def chat_openai(prompt: str):
    """
    Consome um modelo ChatGPT (OpenAI API).
    """
    if not OpenAI:
        raise HTTPException(status_code=500, detail="openai não instalado")
    api_key = os.getenv("OPENAI_API_KEY")
    if not api_key:
        raise HTTPException(status_code=400, detail="OPENAI_API_KEY não configurada")
    client = OpenAI(api_key=api_key)
    response = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[{"role": "user", "content": prompt}]
    )
    return {"response": response.choices[0].message.content}

@app.post("/api/llm/groq", tags=["LLM"])
async def chat_groq(prompt: str):
    """
    Consome um modelo LLM via Groq API.
    """
    if not groq:
        raise HTTPException(status_code=500, detail="groq não instalado")
    api_key = os.getenv("GROQ_API_KEY")
    if not api_key:
        raise HTTPException(status_code=400, detail="GROQ_API_KEY não configurada")
    client = groq.Groq(api_key=api_key)
    response = client.chat.completions.create(
        model="llama3-8b-8192",
        messages=[{"role": "user", "content": prompt}]
    )
    return {"response": response.choices[0].message.content}

@app.post("/api/llm/huggingface", tags=["LLM"])
async def chat_huggingface(prompt: str, model: str = "gpt2"):
    """
    Consome um modelo do HuggingFace Transformers (pipeline local ou via API, se configurado).
    """
    if not pipeline:
        raise HTTPException(status_code=500, detail="transformers não instalado")
    pipe = pipeline("text-generation", model=model)
    result = pipe(prompt, max_length=100, num_return_sequences=1)
    return {"response": result[0]["generated_text"]}

@app.post("/api/codereview", tags=["Code Review"])
async def codereview(data: PRRequest):
    """
    Recebe repo e pr_number, busca o diff da PR no GitHub, analisa com LLM (Bedrock) e retorna análise.
    Retorna 409 se detectar anti-padrão, 200 se ok.
    """
    github_token = os.getenv("GITHUB_TOKEN")
    if not github_token:
        raise HTTPException(status_code=500, detail="GITHUB_TOKEN não configurado no ambiente.")
    # Buscar diff da PR
    try:
        g = Github(github_token)
        repo = g.get_repo(data.repo)
        pr = repo.get_pull(data.pr_number)
        diff_url = pr.diff_url
        diff_response = requests.get(diff_url, headers={"Authorization": f"token {github_token}"})
        if diff_response.status_code != 200:
            raise Exception(f"Erro ao buscar diff: {diff_response.text}")
        diff_content = diff_response.text
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Erro ao buscar diff da PR: {str(e)}")
    # Montar prompt focado em logs
    diff_preview = diff_content[:2000]
    prompt = (
        "Você é um especialista em observabilidade e FinOps para ambientes multicloud em grandes bancos. "
        "Analise apenas as alterações de LOGS no diff abaixo. Identifique desperdício, ruído, duplicidade, excesso de logs, spans inúteis e anti-padrões que aumentam custos ou dificultam o diagnóstico. "
        "Sugira correções, padronizações e formas de reduzir custos de observabilidade. Seja objetivo, cite exemplos do diff e responda em PT-BR.\n\n"
        f"DIFF DE LOGS:\n{diff_preview}\n\nResumo e recomendações:"
    )
    # Chamar Bedrock
    region = os.getenv("AWS_BEDROCK_REGION")
    access_key = os.getenv("AWS_BEDROCK_ACCESS_KEY")
    secret_key = os.getenv("AWS_BEDROCK_SECRET_KEY")
    if not (region and access_key and secret_key):
        raise HTTPException(status_code=500, detail="AWS Bedrock não configurado.")
    try:
        bedrock = boto3.client(
            service_name="bedrock-runtime",
            region_name=region,
            aws_access_key_id=access_key,
            aws_secret_access_key=secret_key
        )
        body = {
            "prompt": prompt,
            "max_tokens_to_sample": 512,
            "temperature": 0.2,
            "top_k": 250,
            "top_p": 1,
            "stop_sequences": ["\n\n"]
        }
        response = bedrock.invoke_model(
            modelId="anthropic.claude-v2",
            body=json.dumps(body),
            accept="application/json",
            contentType="application/json"
        )
        result = json.loads(response["body"].read())
        review = result.get("completion", "[ERRO] Não foi possível obter resposta da LLM Bedrock.")
        # Palavras-chave para identificar anti-padrão
        anti_padrao_keywords = [
            "anti-padrão", "desperdício", "duplicidade", "excesso", "span inútil", "problema", "ruído", "logs desnecessários"
        ]
        if any(x in review.lower() for x in anti_padrao_keywords):
            return JSONResponse(status_code=409, content={"review": review})
        return {"review": review}
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Erro ao acessar Bedrock: {str(e)}")

# Adicione aqui seus endpoints para IA, por exemplo:
# @app.post("/api/v1/python/predict", tags=["IA"])
# async def predict_something(data: dict):
#     # Lógica de predição aqui
#     # import pandas as pd
#     # df = pd.DataFrame(data)
#     # prediction = my_model.predict(df)
#     return {"prediction": "resultado_da_predicao"}

if __name__ == "__main__":
    # Este bloco é útil para rodar localmente com `python app/main.py`
    # Para produção, use um servidor ASGI como Uvicorn ou Hypercorn diretamente.
    # Ex: uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload
    uvicorn.run(app, host="0.0.0.0", port=8000) 