from dotenv import load_dotenv
load_dotenv()
import logging
from fastapi.middleware.cors import CORSMiddleware

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(message)s"
)
from fastapi import FastAPI, HTTPException, UploadFile, File, Form
from fastapi.responses import JSONResponse
from pydantic import BaseModel
import uvicorn
import os
import boto3
import json
from github import Github
import requests
import re
from datetime import date, timedelta

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

# Configuração de CORS para permitir requisições do frontend
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # ou especifique ["http://localhost:3000"] para mais seguro
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"]
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

class FinOpsRequest(BaseModel):
    chat_id: str
    content: str

class AIResponse(BaseModel):
    content: str
    type: str = "default"  # 'cost', 'alert', 'insight', 'dashboard', ou 'default'

# Defina o modelId como constante
BEDROCK_MODEL_ID = "anthropic.claude-3-5-sonnet-20241022-v2:0"

# Função auxiliar para invocar o modelo Bedrock
def invoke_bedrock_model(prompt, region, access_key, secret_key):
    logging.info(f"Invocando Bedrock modelId={BEDROCK_MODEL_ID}")
    bedrock = boto3.client(
        service_name="bedrock-runtime",
        region_name=region,
        aws_access_key_id=access_key,
        aws_secret_access_key=secret_key
    )
    body = {
        "messages": [
            {"role": "user", "content": prompt}
        ],
        "max_tokens": 4096,
        "temperature": 0.5,
        "top_p": 1,
        "anthropic_version": "bedrock-2023-05-31"
    }
    response = bedrock.invoke_model(
        modelId=BEDROCK_MODEL_ID,
        body=json.dumps(body),
        accept="application/json",
        contentType="application/json"
    )
    result = json.loads(response["body"].read())
    logging.info(f"Resposta bruta do Bedrock: {result}")
    return result["content"][0]["text"] if "content" in result and result["content"] else result.get("completion", "[ERRO] Não foi possível obter resposta da LLM Bedrock.")

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
        logging.info(f"Diff da PR obtido com sucesso para {data.repo} PR #{data.pr_number}")
    except Exception as e:
        logging.error(f"Erro ao buscar diff da PR: {e}")
        raise HTTPException(status_code=500, detail=f"Erro ao buscar diff da PR: {str(e)}")
    # Montar prompt focado em logs
    diff_preview = diff_content[:2000]
    prompt = (
        "Você é um especialista em observabilidade e FinOps para ambientes multicloud em grandes bancos. "
        "Analise apenas as alterações de LOGS no diff abaixo. Identifique desperdício, ruído, duplicidade, excesso de logs, spans inúteis e anti-padrões que aumentam custos ou dificultam o diagnóstico. "
        "Sugira correções, padronizações e formas de reduzir custos de observabilidade. Seja objetivo, cite exemplos do diff e responda em PT-BR.\n\n"
        f"DIFF DE LOGS:\n{diff_preview}\n\nResumo e recomendações:"
    )
    try:
        review = invoke_bedrock_model(prompt, region, access_key, secret_key)
    except Exception as e:
        logging.error(f"Erro ao acessar Bedrock: {e}")
        raise HTTPException(status_code=500, detail=f"Erro ao acessar Bedrock: {str(e)}")
    # Palavras-chave para identificar anti-padrão
    anti_padrao_keywords = [
        "anti-padrão", "desperdício", "duplicidade", "excesso", "span inútil", "problema", "ruído", "logs desnecessários"
    ]
    if any(x in review.lower() for x in anti_padrao_keywords):
        return JSONResponse(status_code=409, content={"review": review})
    return {"review": review}

@app.post("/api/message", response_model=AIResponse, tags=["FinOpsGPT"])
async def finops_gpt(data: FinOpsRequest):
    """
    Endpoint FinOpsGPT: recebe uma pergunta, busca dados AWS (Cost Explorer, CloudWatch), monta prompt, chama Bedrock e retorna resposta e tipo.
    """
    region = os.getenv("AWS_BEDROCK_REGION")
    access_key = os.getenv("AWS_BEDROCK_ACCESS_KEY")
    secret_key = os.getenv("AWS_BEDROCK_SECRET_KEY")
    logging.info(f"AWS_BEDROCK_REGION: {region}")
    logging.info(f"AWS_BEDROCK_ACCESS_KEY: {'SET' if access_key else 'NOT SET'}")
    logging.info(f"AWS_BEDROCK_SECRET_KEY: {'SET' if secret_key else 'NOT SET'}")
    if not (region and access_key and secret_key):
        missing = []
        if not region:
            missing.append('AWS_BEDROCK_REGION')
        if not access_key:
            missing.append('AWS_BEDROCK_ACCESS_KEY')
        if not secret_key:
            missing.append('AWS_BEDROCK_SECRET_KEY')
        logging.error(f"Variáveis de ambiente ausentes: {', '.join(missing)}")
        raise HTTPException(status_code=500, detail="AWS Bedrock não configurado.")
    # Inicializar boto3 para Cost Explorer e CloudWatch
    ce = boto3.client(
        'ce',
        region_name=region,
        aws_access_key_id=access_key,
        aws_secret_access_key=secret_key
    )
    # 1. Custo total do mês
    try:
        today = date.today()
        start = today.replace(day=1).isoformat()
        end = today.isoformat()
        logging.info(f"Consultando Cost Explorer: {start} até {end}")
        cost_data = ce.get_cost_and_usage(
            TimePeriod={'Start': start, 'End': end},
            Granularity='MONTHLY',
            Metrics=['UnblendedCost']
        )
        total_cost = float(cost_data['ResultsByTime'][0]['Total']['UnblendedCost']['Amount'])
        total_cost_str = f"R$ {total_cost:,.2f}".replace(",", ".")
        logging.info(f"Custo total do mês: {total_cost_str}")
    except Exception as e:
        logging.error(f"Erro ao consultar Cost Explorer (custo total): {e}")
        total_cost = 0
        total_cost_str = "N/A"
    # 2. Top 3 serviços por custo
    try:
        logging.info(f"Consultando Cost Explorer (top serviços): {start} até {end}")
        cost_data_services = ce.get_cost_and_usage(
            TimePeriod={'Start': start, 'End': end},
            Granularity='MONTHLY',
            Metrics=['UnblendedCost'],
            GroupBy=[{'Type': 'DIMENSION', 'Key': 'SERVICE'}]
        )
        top_services = sorted(
            [
                (item['Keys'][0], float(item['Metrics']['UnblendedCost']['Amount']))
                for item in cost_data_services['ResultsByTime'][0]['Groups']
            ],
            key=lambda x: x[1],
            reverse=True
        )[:3]
        top_services_str = ', '.join([f'{name} (R$ {value:,.2f})' for name, value in top_services])
        logging.info(f"Top serviços: {top_services_str}")
    except Exception as e:
        logging.error(f"Erro ao consultar Cost Explorer (top serviços): {e}")
        top_services_str = "N/A"
    # 3. Alertas CloudWatch em ALARM
    try:
        logging.info("Consultando CloudWatch (alarmes em ALARM)")
        cw = boto3.client('cloudwatch', region_name=region, aws_access_key_id=access_key, aws_secret_access_key=secret_key)
        alarms = cw.describe_alarms(StateValue='ALARM')
        alertas = [a['AlarmName'] for a in alarms.get('MetricAlarms', [])]
        alertas_str = ', '.join(alertas[:5]) if alertas else 'Nenhum alerta crítico recente'
        logging.info(f"Alertas recentes: {alertas_str}")
    except Exception as e:
        logging.error(f"Erro ao consultar CloudWatch: {e}")
        alertas_str = "N/A"
    # 4. Insight: aumento de custo >30% mês a mês
    insight = None
    try:
        first_day_this_month = date.today().replace(day=1)
        last_day_last_month = first_day_this_month - timedelta(days=1)
        start_last = last_day_last_month.replace(day=1).isoformat()
        end_last = last_day_last_month.isoformat()
        logging.info(f"Consultando Cost Explorer (mês anterior): {start_last} até {end_last}")
        cost_last = ce.get_cost_and_usage(
            TimePeriod={'Start': start_last, 'End': end_last},
            Granularity='MONTHLY',
            Metrics=['UnblendedCost']
        )
        total_last = float(cost_last['ResultsByTime'][0]['Total']['UnblendedCost']['Amount'])
        if total_last > 0 and total_cost > 0:
            aumento = (total_cost - total_last) / total_last * 100
            if aumento > 30:
                insight = f"Custo total subiu {aumento:.1f}% em relação ao mês anterior."
        logging.info(f"Insight: {insight}")
    except Exception as e:
        logging.error(f"Erro ao calcular insight de aumento de custo: {e}")
    # Montar contexto
    contexto = (
        f"Custo total do mês: {total_cost_str}\n"
        f"Top serviços: {top_services_str}\n"
        f"Alertas recentes: {alertas_str}\n"
    )
    if insight:
        contexto += f"Insight: {insight}\n"
    # Montar prompt
    prompt = f"""
Você é o <b>FinOpsGPT</b>, copiloto da plataforma <u>FOG — FinOps &amp; Observability Guardian</u>, especializado em ambientes AWS multicloud.
Responda em <b>PT-BR</b>, de forma <i>clara</i>, <i>objetiva</i> e <b>sempre</b> em um <u>único bloco de HTML puro</u>, pronto para renderizar no navegador.

<!-- ======= PALETA DE CORES FOG ======= -->
<ul style="display:none;">
  <li><b>Laranja Itaú (primária):</b> #ff6900</li>
  <li><b>Azul escuro (títulos):</b> #1d3557</li>
  <li><b>Azul médio (detalhes):</b> #457b9d</li>
  <li><b>Verde (positivo):</b> #2a9d8f</li>
  <li><b>Vermelho (alerta):</b> #e63946</li>
  <li><b>Amarelo (atenção):</b> #f77f00</li>
  <!-- ESCALA DE CINZAS (usar nestings) -->
  <li><b>Cinza escuro 2:</b> #777777</li>
  <li><b>Cinza escuro 1:</b> #858585</li>
  <li><b>Cinza base (cartão raiz):</b> #929293</li>
  <li><b>Cinza claro 1:</b> #a0a0a0</li>
  <li><b>Cinza claro 2:</b> #adadad</li>
  <li><b>Texto principal:</b> #222222</li>
</ul>
<p style="display:none;">
  Use sempre o cinza base #929293 como fundo do cartão raiz.
  Para cada nível aninhado, alterne entre cinza-claro-1 (#a0a0a0) e cinza-escuro-1 (#858585) para garantir contraste.
  Nunca use branco puro (#ffffff).
  Use o laranja #ff6900 para botões, títulos principais e ícones de ação.
  Use o azul escuro #1d3557 para títulos e cabeçalhos.
  Use o azul médio #457b9d para detalhes, links e gráficos.
  Use o verde #2a9d8f para valores positivos e metas atingidas.
  Use o vermelho #e63946 para alertas, erros e avisos.
  Use o amarelo #f77f00 para alertas de atenção ou variações.
  Use o cinza escuro #222222 para textos principais.
</p>

<!-- ======= REGRAS DE FORMATAÇÃO ======= -->
<ul style="display:none;">
  <li>Use <b>apenas</b> tags HTML: div, h3, b, i, u, span, ul, li, table, tr, td, br, p, etc.</li>
  <li>Jamais utilize Markdown ou quebras "n" — use <br/> ou outras tags.</li>
  <li>Texto <b>nunca</b> deve ficar fora de tags; o marcador de tipo deve ser um comentário HTML.</li>
  <li>Utilize CSS inline simples ou classes pré-existentes (ex.: <span class='llm-alert'>).</li>
  <li>Seja visual e sucinto; use as cores da paleta FOG para alto contraste.</li>
  <li><b>Proibição absoluta</b> de #ffffff.</li>
  <li>Cartão raiz → background:#929293.<br/>
      Para cada nível aninhado, escolha cinza-claro-1 (#a0a0a0) ou cinza-escuro-1 (#858585)<br/>
      (alternar claro/escuro garante contraste interno).</li>
  <li>Alertas, badges ou ícones seguem a paleta de cor correspondente (verde/vermelho/laranja/amarelo).</li>
  <li>CSS inline simples; classes só se pré-existirem.</li>
</ul>

<!-- ======= CONTEXTO DE NEGÓCIO ======= -->
<p style="display:none;">
  Metas FOG: reduzir 30% custos de observabilidade, detectar spans inúteis < 10 min, eliminar recursos zumbis.
  Sempre ofereça recomendações práticas (ex.: lifecycle S3, migração Spot, reduzir nível de log).
</p>

<!-- ======= EXEMPLOS DE SAÍDA ======= -->
<div style="display:none;">
  <div style="background:#929293; padding:16px; border-radius:8px;">
    <p><b style="color:#e63946;">⚠️ Alerta Ativo:</b><br/>
       <span style="font-weight:600;">Gasto do serviço <span style="color:#f77f00;">S3</span> ultrapassou <u>15% da meta mensal</u>!</span>
    </p>
    <ul>
      <li><b>Atual:</b> <span style="color:#457b9d;">R$ 3.750</span></li>
      <li><b>Meta:</b> <span style="color:#2a9d8f;">R$ 3.200</span></li>
    </ul>
    <i>Recomendo revisar políticas de lifecycle e storage classes.</i>
    <!-- type: alert -->
  </div>
</div>

<div style="display:none;">
  <div style="background:#929293; padding:16px; border-radius:8px;">
    <h3 style="color:#1d3557;">Resumo de Custos</h3>
    <table style="width:100%; border-collapse:collapse; background:#858585;">
      <tr>
        <th style="padding:8px; text-align:left; color:#1d3557;">Serviço</th>
        <th style="padding:8px; text-align:right; color:#1d3557;">Custo</th>
        <th style="padding:8px; text-align:right; color:#1d3557;">% do Total</th>
      </tr>
      <tr><td><b>AWS CloudTrail</b></td><td style="text-align:right;">R$ 0,12</td><td style="text-align:right;">46,2%</td></tr>
      <tr><td><b>Amazon Bedrock</b></td><td style="text-align:right;">R$ 0,08</td><td style="text-align:right;">30,8%</td></tr>
      <tr><td><b>Amazon SageMaker</b></td><td style="text-align:right;">R$ 0,05</td><td style="text-align:right;">19,2%</td></tr>
      <tr><td><b>Total do Mês</b></td><td style="text-align:right;"><b>R$ 0,26</b></td><td style="text-align:right;">100%</td></tr>
    </table>
    <p style="margin-top:10px;"><i>Apesar do aumento percentual (563,8 %), o valor absoluto segue baixo, possivelmente por uso inicial.</i></p>
    <!-- type: cost -->
  </div>
</div>

<!-- ======= INSTRUÇÕES FINAIS ======= -->
<p style="display:none;">
  Classifique a resposta como: <b>cost</b>, <b>alert</b>, <b>insight</b> ou <b>dashboard</b>.
  Insira <b>apenas um</b> comentário HTML no final indicando o tipo — ex.: <!-- type: cost -->.
</p>

<!-- ======= ENTRADA DINÂMICA ======= -->
<div style="background:#929293; padding:16px; border-radius:8px;">
  <h3 style="color:#1d3557;">Pergunta do usuário:</h3>
  <p style="font-size:1.1em; color:#222;">{data.content}</p>
  <p style="display:none;">Contexto:<br/>{contexto}</p>
  <p><b>Responda diretamente à pergunta acima, usando os dados e contexto fornecidos. Nunca ignore a pergunta do usuário.</b></p>
</div>
"""
    try:
        llm_response = invoke_bedrock_model(prompt, region, access_key, secret_key)
    except Exception as e:
        logging.error(f"Erro ao acessar Bedrock: {e}")
        raise HTTPException(status_code=500, detail=f"Erro ao acessar Bedrock: {str(e)}")
    # Extrair type
    match = re.search(r"<!-- type: (\w+) -->", llm_response, re.IGNORECASE)
    if match:
        type_ = match.group(1).lower()
        text = llm_response[:match.start()].strip()
    else:
        type_ = "default"
        text = llm_response.strip()
    # Garante que a resposta sempre venha encapsulada em uma <div class='llm-root'>...</div>
    text = text.strip()
    # Remove qualquer wrapper <div ...> externo
    text = re.sub(r"^<div[^>]*>(.*)</div>$", r"\1", text, flags=re.DOTALL)
    # Sempre encapsula corretamente
    text = f"<div class='llm-root'>{text}</div>"
    return AIResponse(content=text, type=type_)

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
    uvicorn.run(app, host="0.0.0.0", port=8005) 