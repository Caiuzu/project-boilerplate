"""
Exemplos de uso de modelos e bibliotecas de IA citados no playbook e no requirements.txt.
Cada função pode ser adaptada para endpoints FastAPI ou notebooks.

Resumo das libs:
- transformers: LLMs, NLP, visão, áudio (HuggingFace)
- sentence-transformers: embeddings de texto
- openai: API OpenAI (GPT, embeddings)
- groq: API Groq (LLMs open-source ultra-rápidos)
- tabpfn: classificação tabular zero-shot
- tsai: séries temporais
- whisper: transcrição de áudio
- PIL: visão computacional
- pandas: manipulação de dados tabulares
- numpy: computação numérica
- scikit-learn: ML tradicional (regressão, clustering, etc)
- tensorflow: deep learning (alternativa ao torch)
- langchain: pipelines de LLMs, agentes, RAG
- faiss-cpu: busca vetorial/semântica
"""

# 1. HuggingFace Transformers - LLM
from transformers import AutoTokenizer, AutoModelForCausalLM, pipeline
import torch

def example_hf_llm():
    """Geração de texto com LLM open-source (HuggingFace)."""
    model_id = "mistralai/Mistral-7B-Instruct-v0.3"
    tokenizer = AutoTokenizer.from_pretrained(model_id, trust_remote_code=True)
    model = AutoModelForCausalLM.from_pretrained(model_id, torch_dtype=torch.float16 if torch.cuda.is_available() else torch.float32, device_map="auto", trust_remote_code=True)
    pipe = pipeline("text-generation", model=model, tokenizer=tokenizer, device=model.device)
    prompt = "Explique o que é um Hackathon em 3 frases."
    return pipe(prompt, max_length=100)[0]["generated_text"]

# 2. sentence-transformers - Embeddings
from sentence_transformers import SentenceTransformer

def example_embeddings():
    """Geração de embeddings de texto para busca/vetorização."""
    model = SentenceTransformer('all-MiniLM-L6-v2')
    sentences = ["O Hackathon é um evento de inovação.", "Maratona de programação."]
    embeddings = model.encode(sentences)
    return embeddings

# 3. OpenAI API
import os
from openai import OpenAI

def example_openai():
    """Geração de texto com GPT-3.5/4 via API OpenAI."""
    client = OpenAI(api_key=os.getenv("OPENAI_API_KEY"))
    response = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=[{"role": "user", "content": "Explique o que é um Hackathon."}]
    )
    return response.choices[0].message.content

# 4. Groq API
from groq import Groq

def example_groq():
    """Geração de texto com LLM open-source via API Groq (ultra-rápido)."""
    client = Groq(api_key=os.getenv("GROQ_API_KEY"))
    response = client.chat.completions.create(
        model="llama3-8b-8192",
        messages=[{"role": "user", "content": "Explique o que é um Hackathon."}]
    )
    return response.choices[0].message.content

# 5. TabPFN - Classificação tabular zero-shot
from tabpfn import TabPFNClassifier
import numpy as np

def example_tabpfn():
    """Classificação tabular zero-shot (sem treino tradicional)."""
    X = np.random.rand(10, 5)
    y = np.random.randint(0, 2, 10)
    clf = TabPFNClassifier(device="cpu")
    clf.fit(X, y)
    return clf.predict(X)

# 6. tsai - Séries temporais
from tsai.all import get_UCR_data, InceptionTime, Learner

def example_tsai():
    """Classificação de séries temporais com deep learning."""
    X_train, y_train, X_valid, y_valid = get_UCR_data("OliveOil", split_data=True)
    model = InceptionTime(X_train.shape[1], len(set(y_train)))
    learn = Learner(dls=[(X_train, y_train), (X_valid, y_valid)], model=model, loss_func="cross_entropy")
    # learn.fit_one_cycle(1)  # Descomente para treinar
    return model

# 7. Whisper - Transcrição de áudio
import librosa
import soundfile as sf
from transformers import pipeline as hf_pipeline

def example_whisper():
    """Transcrição de áudio para texto (ASR) com Whisper."""
    audio_path = "audio_exemplo.wav"
    # y, sr = librosa.load(audio_path, sr=16000)
    # sf.write("audio16k.wav", y, sr)
    pipe = hf_pipeline("automatic-speech-recognition", model="openai/whisper-large-v3")
    return pipe(audio_path)

# 8. Visão Computacional - LLaVA/Moondream2
from transformers import AutoProcessor, AutoModelForVision2Seq
from PIL import Image

def example_vision():
    """Descrição de imagem com modelo multimodal (LLM+Visão)."""
    model_id = "llava-hf/llava-1.6-mistral-7b-hf"
    processor = AutoProcessor.from_pretrained(model_id)
    model = AutoModelForVision2Seq.from_pretrained(model_id)
    image = Image.open("exemplo.jpg")
    prompt = "Descreva a imagem."
    inputs = processor(prompt, image, return_tensors="pt")
    output = model.generate(**inputs)
    return processor.decode(output[0], skip_special_tokens=True)

# 9. pandas - Manipulação de dados tabulares
def example_pandas():
    """Manipulação e análise de dados tabulares."""
    import pandas as pd
    df = pd.DataFrame({"nome": ["Ana", "Beto"], "idade": [23, 31]})
    df["maior_de_idade"] = df["idade"] >= 18
    return df

# 10. numpy - Computação numérica/vetorial
def example_numpy():
    """Operações matemáticas e vetoriais rápidas."""
    import numpy as np
    arr = np.array([1, 2, 3])
    return arr * 2

# 11. scikit-learn - ML tradicional
def example_sklearn():
    """Classificação simples com RandomForest."""
    from sklearn.ensemble import RandomForestClassifier
    from sklearn.datasets import load_iris
    X, y = load_iris(return_X_y=True)
    clf = RandomForestClassifier().fit(X, y)
    return clf.predict([X[0]])

# 12. tensorflow - Deep learning (alternativa ao torch)
def example_tensorflow():
    """Rede neural simples com TensorFlow/Keras."""
    import tensorflow as tf
    model = tf.keras.Sequential([
        tf.keras.layers.Dense(10, activation='relu', input_shape=(4,)),
        tf.keras.layers.Dense(3, activation='softmax')
    ])
    model.compile(optimizer='adam', loss='categorical_crossentropy')
    return model.summary()

# 13. langchain - Pipelines de LLMs, agentes, RAG
def example_langchain():
    """Pipeline de LLM com LangChain (OpenAI)."""
    from langchain_openai import ChatOpenAI
    from langchain_core.messages import HumanMessage
    llm = ChatOpenAI(openai_api_key=os.getenv("OPENAI_API_KEY"))
    return llm([HumanMessage(content="Explique o que é um Hackathon.")])

# 14. faiss-cpu - Busca vetorial/semântica
def example_faiss():
    """Busca vetorial rápida para embeddings."""
    import faiss
    import numpy as np
    d = 128
    xb = np.random.random((100, d)).astype('float32')
    xq = np.random.random((5, d)).astype('float32')
    index = faiss.IndexFlatL2(d)
    index.add(xb)
    D, I = index.search(xq, k=3)
    return I

# 15. huggingface_hub - Inference API (cloud Hugging Face)
from huggingface_hub import InferenceClient

def example_hf_inference_api():
    """Geração de texto via Inference API da Hugging Face (nuvem, sem infra local)."""
    client = InferenceClient(token="your_hf_token")  # Substitua pelo seu token HF
    response = client.text_generation("Qual o propósito da vida?", model="tiiuae/falcon-7b-instruct")
    return response

# 16. transformers pipeline direto da nuvem (sem download local)
from transformers import pipeline as hf_pipeline2

def example_hf_pipeline_cloud():
    """Uso rápido de modelo via pipeline (pode rodar local ou na nuvem se disponível)."""
    pipe = hf_pipeline2("text-classification", model="distilbert-base-uncased-finetuned-sst-2-english")
    return pipe("I love this!") 