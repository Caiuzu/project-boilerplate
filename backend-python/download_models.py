"""
Script para baixar modelos do HuggingFace para uso local.
Uso: python download_models.py model1 model2 ...
Exemplo: python download_models.py mistralai/Mistral-7B-Instruct-v0.3 sentence-transformers/all-MiniLM-L6-v2
"""
import sys
import os
from transformers import AutoTokenizer, AutoModel, AutoModelForCausalLM, AutoModelForSequenceClassification

def download_model(model_id, model_type="auto"):
    print(f"[INFO] Baixando modelo: {model_id}")
    save_dir = os.path.join("models", model_id.replace("/", "--"))
    os.makedirs(save_dir, exist_ok=True)
    if model_type == "causal-lm":
        AutoModelForCausalLM.from_pretrained(model_id, cache_dir=save_dir)
    elif model_type == "seq-class":
        AutoModelForSequenceClassification.from_pretrained(model_id, cache_dir=save_dir)
    else:
        AutoModel.from_pretrained(model_id, cache_dir=save_dir)
    AutoTokenizer.from_pretrained(model_id, cache_dir=save_dir)
    print(f"[OK] Modelo salvo em: {save_dir}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python download_models.py <model_id> [<model_id> ...]")
        sys.exit(1)
    for model_id in sys.argv[1:]:
        # Heurística simples para tipo de modelo
        if "causal" in model_id.lower() or "llama" in model_id.lower() or "mistral" in model_id.lower():
            download_model(model_id, model_type="causal-lm")
        elif "miniLM" in model_id or "bert" in model_id.lower() or "seq" in model_id.lower():
            download_model(model_id, model_type="seq-class")
        else:
            download_model(model_id) 