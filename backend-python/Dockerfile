# Usar uma imagem Python oficial como imagem base
FROM python:3.11-slim

# Define o diretório de trabalho no contêiner
WORKDIR /app

# Copia o arquivo de dependências para o diretório de trabalho
COPY requirements.txt .

# Instala as dependências
# O --no-cache-dir reduz o tamanho da imagem, não armazenando o cache do pip
# O --upgrade pip garante que estamos usando uma versão recente do pip
RUN pip install --no-cache-dir --upgrade pip -r requirements.txt

# Copia o diretório da aplicação (app) para o diretório de trabalho no contêiner
COPY ./app /app/app

# Expõe a porta que o Uvicorn estará rodando (deve ser a mesma no comando CMD)
EXPOSE 8000

# Comando para rodar a aplicação usando Uvicorn
# O host 0.0.0.0 torna a aplicação acessível externamente (do host ou outros contêineres)
# --reload é útil para desenvolvimento, mas geralmente removido em produção.
# Para produção, você pode querer mais workers: uvicorn app.main:app --host 0.0.0.0 --port 8000 --workers 4
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"] 