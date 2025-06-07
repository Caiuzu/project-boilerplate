# Templates, Boilerplates e Exemplos de Código

Este documento serve como um repositório central de links para templates de projetos, boilerplates, exemplos de código e ferramentas que podem acelerar significativamente o desenvolvimento durante o Hackathon Itaú. O objetivo é permitir que as equipes foquem na lógica de negócio e na inovação, em vez de gastar tempo excessivo com configurações iniciais.

## Princípios para Usar Templates

* **Entenda Antes de Usar:** Não use um boilerplate cegamente. Gaste alguns minutos para entender sua estrutura e como ele funciona.
* **Adapte, Não Adote CegaMente:** Use o template como ponto de partida e adapte-o às necessidades específicas do seu projeto.
* **Mantenha a Simplicidade:** Escolha templates que sejam o mais próximo possível do que você precisa, evitando complexidade desnecessária.

## Boilerplates e Templates Recomendados

### Frontend (Next.js com TypeScript e Tailwind CSS)

* **Recurso:** [Create Next App (Documentação Oficial)](https://nextjs.org/docs/getting-started)
* **Comando:**

    ```bash
    npx create-next-app@latest my-nextjs-app --typescript --tailwind --eslint
    ```

* **Por que usar?**
  * **Next.js:** Framework React robusto com renderização no lado do servidor (SSR), geração de sites estáticos (SSG), e excelente performance.
  * **TypeScript:** Adiciona tipagem estática ao JavaScript, ajudando a pegar erros em tempo de desenvolvimento e melhorando a manutenibilidade.
  * **Tailwind CSS:** Framework CSS "utility-first" que permite construir interfaces customizadas rapidamente sem escrever CSS tradicional.
  * **ESLint:** Para padronização e qualidade do código.
* **Dica:** Explore os templates oficiais do Next.js que vêm com `create-next-app` ou procure por starters no GitHub com mais funcionalidades pré-configuradas se precisar (ex: integração com bibliotecas de UI, autenticação básica).

### Backend

A escolha do backend depende muito da preferência da equipe e da familiaridade com a linguagem/framework.

* **Python (FastAPI):
  * **Gerador Sugerido (Cookiecutter):** [https://github.com/tiangolo/full-stack-fastapi-postgresql](https://github.com/tiangolo/full-stack-fastapi-postgresql) (Este é bem completo, pode ser simplificado para o hackathon).
  * **Por que usar FastAPI?** Alta performance, fácil de aprender, validação de dados automática com Pydantic, geração de documentação de API (Swagger UI, ReDoc) automática.
  * **Dica:** Para um MVP simples, você pode começar com um único arquivo `main.py` e expandir conforme necessário. Não precisa de um gerador complexo inicialmente.

* **Node.js (Express com TypeScript):
  * **Recurso:** Muitos starters no GitHub. Pesquise por "express typescript boilerplate" ou "node typescript starter".
  * **Exemplo (simples, para inspiração):** [https://github.com/jsynowiec/node-typescript-boilerplate](https://github.com/jsynowiec/node-typescript-boilerplate)
  * **Por que usar Express?** Framework minimalista e flexível para Node.js, amplamente utilizado e com uma grande comunidade.
  * **Dica:** Configure o `nodemon` para reiniciar automaticamente o servidor durante o desenvolvimento e o `ts-node` para rodar TypeScript diretamente.

* **Java (Spring Boot):
  * **Recurso:** [Spring Initializr](https://start.spring.io/)
  * **Por que usar Spring Boot?** Ecossistema maduro, robusto, facilita a criação de aplicações Java standalone e prontas para produção. Ideal se a equipe tem experiência com Java.
  * **Dica:** Selecione apenas as dependências essenciais para o MVP (ex: Spring Web, Spring Data JPA se precisar de banco de dados).

* **Go (Gin Gonic):
  * **Exemplo de Estrutura de Projeto:** [https://github.com/golang-standards/project-layout](https://github.com/golang-standards/project-layout) (Pode ser um overkill para hackathon, mas bom para referência de organização).
  * **Por que usar Gin?** Framework HTTP de alta performance para Go, com uma API semelhante ao Martini, mas mais rápido.
  * **Dica:** Go é ótimo para CLIs e APIs de alta performance. Sua compilação para um binário único simplifica o deploy.

### Multi-Cloud e Infraestrutura como Código (IaC)

* **Docker e Docker Compose:**
  * **Recurso:** [Docker Compose - Getting Started](https://docs.docker.com/compose/gettingstarted/)
  * **Exemplo `docker-compose.yml` (Frontend + Backend + DB):

        ```yaml
        version: '3.8'
        services:
          frontend:
            build: ./frontend # Assumindo que o Dockerfile está na pasta frontend
            ports:
              - "3000:3000"
            depends_on:
              - backend
          backend:
            build: ./backend # Assumindo que o Dockerfile está na pasta backend
            ports:
              - "8000:8000"
            environment:
              DATABASE_URL: postgresql://user:password@db:5432/mydb
            depends_on:
              - db
          db:
            image: postgres:13
            environment:
              POSTGRES_USER: user
              POSTGRES_PASSWORD: password
              POSTGRES_DB: mydb
            ports:
              - "5432:5432"
            volumes:
              - postgres_data:/var/lib/postgresql/data

        volumes:
          postgres_data:
        ```

  * **Dica:** Crie `Dockerfile`s otimizados para cada serviço (frontend, backend, IA service). Use multi-stage builds para manter as imagens pequenas.

* **Terraform (para provisionar recursos na Cloud):
  * **Recurso:** [Terraform - Getting Started (AWS, GCP, Azure)](https://developer.hashicorp.com/terraform/tutorials/aws-get-started)
  * **Exemplos Oficiais:** A HashiCorp e os provedores de cloud geralmente têm repositórios de exemplos no GitHub.
    * **AWS:** [https://github.com/aws-samples/terraform-aws-modules](https://github.com/aws-samples/terraform-aws-modules)
    * **GCP:** [https://github.com/GoogleCloudPlatform/terraform-google-examples](https://github.com/GoogleCloudPlatform/terraform-google-examples)
    * **Azure:** [https://github.com/Azure/terraform](https://github.com/Azure/terraform)
  * **Dica:** Comece com um recurso simples (ex: um bucket S3 na AWS, uma instância de VM no GCP) para entender o fluxo do Terraform. Mantenha seus arquivos de configuração (`.tf`) modulares.

### Modelos de IA e Exemplos

* **Hugging Face Transformers:**
  * **Recurso:** [Hugging Face Tasks](https://huggingface.co/tasks) - Encontre modelos pré-treinados para diversas tarefas (NLP, CV, Audio).
  * **Exemplo (Sumarização de Texto com Pipeline em Python):

```python
        from transformers import pipeline

        # Carrega o pipeline de sumarização
        summarizer = pipeline("summarization", model="facebook/bart-large-cnn")

        ARTICLE = """
        Insira aqui um texto longo para ser sumarizado. 
        Quanto maior o texto, melhor o resultado da sumarização, dentro dos limites do modelo.
        Este é apenas um exemplo de como usar o pipeline.
        """
        summary = summarizer(ARTICLE, max_length=150, min_length=30, do_sample=False)
        print(summary[0]['summary_text'])
```

* **Dica:** Muitos modelos podem ser usados com poucas linhas de código. Explore os pipelines para tarefas comuns.

* **OpenAI API (GPT-3.5, GPT-4)**:
  * **Recurso:** [OpenAI API Documentation](https://platform.openai.com/docs/api-reference)
  * **Exemplo (Chat Completion com Python):

        ```python
        # import openai # Lembre-se de instalar: pip install openai
        # from openai import OpenAI # Nova sintaxe
        # client = OpenAI(api_key="SUA_CHAVE_API_AQUI")

        # Exemplo (simulado, pois não podemos executar chamadas de API aqui)
        def get_openai_response(prompt):
            # Em um cenário real, aqui ocorreria a chamada para a API da OpenAI
            # Exemplo de como seria a chamada:
            # response = client.chat.completions.create(
            # model="gpt-3.5-turbo",
            # messages=[
            # {"role": "system", "content": "Você é um assistente prestativo."},
            # {"role": "user", "content": prompt}
            # ]
            # )
            # return response.choices[0].message.content
            print(f"Simulando resposta da OpenAI para o prompt: {prompt}")
            if "capital da França" in prompt.lower():
                return "Paris."
            return "Desculpe, não posso processar este pedido no momento."

        user_prompt = "Qual é a capital da França?"
        response_text = get_openai_response(user_prompt)
        print(f"Resposta: {response_text}")
        ```

  * **Dica:** Use variáveis de ambiente para suas chaves de API. Comece com os modelos mais baratos (como GPT-3.5-turbo) para testar.

* **Google AI (Gemini):
  * **Recurso:** [Google AI Studio](https://aistudio.google.com/) e [Documentação do Gemini API](https://ai.google.dev/docs/gemini_api_overview)
  * **Dica:** Similar ao OpenAI, o Google AI Studio oferece uma interface para experimentar com o Gemini e exportar o código (Python, Node.js, etc.).

* **Scikit-learn (para Machine Learning Clássico):
  * **Recurso:** [Scikit-learn Examples](https://scikit-learn.org/stable/auto_examples/index.html)
  * **Exemplo (Classificação simples):

```python
        from sklearn.model_selection import train_test_split
        from sklearn.ensemble import RandomForestClassifier
        from sklearn.metrics import accuracy_score
        from sklearn.datasets import load_iris

        # Carregar dataset de exemplo
        iris = load_iris()
        X, y = iris.data, iris.target

        # Dividir dados em treino e teste
        X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

        # Criar e treinar o modelo
        clf = RandomForestClassifier(random_state=42)
        clf.fit(X_train, y_train)

        # Fazer previsões
        predictions = clf.predict(X_test)

        # Avaliar o modelo
        accuracy = accuracy_score(y_test, predictions)
        print(f"Acurácia do Modelo: {accuracy:.2f}")
```

* **Dica:** Scikit-learn é excelente para prototipagem rápida de modelos de ML. Use datasets de exemplo para testar seus pipelines antes de integrar dados reais (ou mocks).

## Ferramentas Adicionais Úteis

* **Postman / Insomnia:** Para testar suas APIs.
* **Ferramentas de Desenvolvedor do Navegador:** Essenciais para debugging de frontend.
* **Git e GitHub/GitLab:** Para controle de versão e colaboração.
* **VS Code com Extensões Relevantes:** (Docker, Python, Prettier, ESLint, Live Share, etc.).
