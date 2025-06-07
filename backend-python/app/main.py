from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import uvicorn
import os

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