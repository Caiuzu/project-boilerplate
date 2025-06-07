# Infraestrutura como Código (IaC) e Ambiente de Simulação Cloud - Hackathon

Este diretório (`infra/`) contém toda a configuração de Infraestrutura como Código (IaC) e as ferramentas para simular um ambiente multi-cloud localmente para o projeto Hackathon.

## 1. Visão Geral

A infraestrutura deste projeto visa demonstrar a capacidade de deploy e gerenciamento em múltiplos provedores de nuvem (AWS, GCP, Azure) utilizando Terraform. Para desenvolvimento e testes locais, o LocalStack é utilizado para simular os serviços AWS.

Os principais componentes da infraestrutura são:

* **Terraform:** Para provisionar e gerenciar recursos na nuvem (AWS, GCP, Azure) de forma declarativa.
* **Helm:** Para gerenciar e implantar aplicações em clusters Kubernetes (se aplicável no escopo do hackathon).
* **LocalStack:** Para simular serviços AWS localmente, permitindo desenvolvimento e testes offline de funcionalidades que dependem da AWS.
* **Docker Compose:** Utilizado para orquestrar o LocalStack e potencialmente outros serviços de infraestrutura local.

## 2. Diagrama da Arquitetura de Infraestrutura (Conceitual)

```mermaid
graph TD
    A[Desenvolvedor] -- Interage com --> B(Makefile / CLI);

    subgraph "Ambiente Local"
        B -- Docker Compose --> C[LocalStack Container];
        C -- Simula Serviços AWS --> D{Aplicações Backend (rodando localmente)};
    end

    subgraph "Ambiente Cloud (Multi-Cloud)"
        B -- Terraform Apply --> E[AWS Cloud];
        B -- Terraform Apply --> F[GCP Cloud];
        B -- Terraform Apply --> G[Azure Cloud];

        E -- Provisiona --> EA(Recursos AWS: S3, RDS, EC2, etc.);
        F -- Provisiona --> FA(Recursos GCP: GCS, SQL, GKE, etc.);
        G -- Provisiona --> GA(Recursos Azure: Blob, SQL DB, AKS, etc.);

        H[Aplicações Backend (Deployadas)] --> EA;
        H --> FA;
        H --> GA;

        B -- Helm Deploy (Opcional) --> I[Kubernetes Cluster (AWS EKS, GCP GKE, Azure AKS)];
        I -- Orquestra --> H;
    end

    style A fill:#fff,stroke:#333,stroke-width:2px
    style B fill:#ddd,stroke:#333,stroke-width:2px
```

*(Este é um diagrama conceitual e pode ser adaptado conforme os recursos específicos utilizados.)*

## 3. Estrutura de Pastas (Sugerida)

```
infra/
├── terraform/                    # Código Terraform
│   ├── aws/                      # Configurações específicas da AWS
│   │   ├── main.tf
│   │   ├── variables.tf
│   │   └── outputs.tf
│   ├── gcp/                      # Configurações específicas da GCP
│   │   ├── main.tf
│   │   └── ...
│   ├── azure/                    # Configurações específicas do Azure
│   │   ├── main.tf
│   │   └── ...
│   └── modules/                  # Módulos Terraform reutilizáveis (opcional)
├── helm/                         # Helm Charts (se Kubernetes for utilizado)
│   └── my-application/
│       ├── Chart.yaml
│       ├── values.yaml
│       └── templates/
├── localstack/                   # Configurações e scripts para LocalStack
│   ├── docker-compose.localstack.yml # (Opcional) Se LocalStack for gerenciado separadamente
│   └── init-aws.sh             # Script para inicializar recursos AWS no LocalStack (ex: criar buckets S3)
└── README.md                     # Este arquivo
```

* **`terraform/`**: Contém todo o código Terraform.
  * **`aws/`, `gcp/`, `azure/`**: Subdiretórios para organizar o código Terraform por provedor de nuvem. Cada um pode conter seus arquivos `main.tf`, `variables.tf`, `outputs.tf`, etc.
  * **`modules/`**: (Opcional) Para módulos Terraform customizados e reutilizáveis dentro do projeto.
* **`helm/`**: (Opcional) Se o Kubernetes for usado, este diretório conteria os Helm Charts para as aplicações.
  * **`my-application/`**: Exemplo de um chart para uma aplicação.
* **`localstack/`**: Contém arquivos relacionados à configuração e inicialização do LocalStack.
  * **`docker-compose.localstack.yml`**: (Opcional) Pode ser usado se você quiser gerenciar o LocalStack separadamente do `docker-compose.yml` principal. Normalmente, o LocalStack é incluído no `docker-compose.yml` raiz do projeto.
  * **`init-aws.sh`**: Script executado na inicialização do LocalStack para criar recursos AWS mockados (ex: buckets S3, filas SQS, tabelas DynamoDB) que as aplicações podem precisar.

## 4. Tecnologias Utilizadas

* **Terraform**: Para provisionamento de infraestrutura multi-cloud.
  * Docs: [https://www.terraform.io/](https://www.terraform.io/)
  * Providers: [AWS](https://registry.terraform.io/providers/hashicorp/aws/latest/docs), [GCP](https://registry.terraform.io/providers/hashicorp/google/latest/docs), [Azure](https://registry.terraform.io/providers/hashicorp/azurerm/latest/docs)
* **Helm**: (Opcional) Para gerenciamento de aplicações Kubernetes.
  * Docs: [https://helm.sh/](https://helm.sh/)
* **LocalStack**: Para simulação de serviços AWS localmente.
  * Docs: [https://localstack.cloud/](https://localstack.cloud/)
  * GitHub: [https://github.com/localstack/localstack](https://github.com/localstack/localstack)
* **Docker & Docker Compose**: Para executar o LocalStack e outras dependências de infraestrutura.

## 5. Como Usar

### 5.1. LocalStack (Simulação AWS Local)

O LocalStack é geralmente iniciado através do `docker-compose.yml` na raiz do projeto.

1. **Verifique a configuração do LocalStack no `docker-compose.yml` principal.**
    * Certifique-se de que os serviços AWS que você precisa simular estão listados na variável de ambiente `SERVICES` (ex: `S3,SQS,DynamoDB`).
    * O script `infra/localstack/init-aws.sh` (se existir e estiver configurado no volume do Docker Compose) será executado para inicializar recursos.

2. **Suba os serviços com Docker Compose (a partir da raiz do projeto):**

    ```bash
    make up # Ou docker-compose up -d
    ```

3. **Interaja com os serviços AWS simulados:**
    * Use o AWS CLI apontando para o endpoint do LocalStack (geralmente `http://localhost:4566`). Exemplo:

        ```bash
        aws --endpoint-url=http://localhost:4566 s3 ls
        aws --endpoint-url=http://localhost:4566 sqs list-queues
        ```

    * Suas aplicações backend, quando rodando localmente e configuradas para usar o endpoint do LocalStack, interagirão com os serviços simulados.

### 5.2. Terraform (Provisionamento em Nuvem Real)

**Pré-requisitos:**

* Terraform CLI instalado.
* Credenciais de acesso para os provedores de nuvem desejados (AWS, GCP, Azure) configuradas no seu ambiente.

**Passos (exemplo para AWS):**

1. **Navegue até o diretório do provedor desejado:**

    ```bash
    cd infra/terraform/aws
    ```

2. **Inicialize o Terraform:**

    ```bash
    terraform init
    ```

3. **Planeje as mudanças na infraestrutura:**

    ```bash
    terraform plan
    ```

    Revise o plano para garantir que ele fará o que você espera.

4. **Aplique as mudanças na infraestrutura:**

    ```bash
    terraform apply
    ```

    Confirme a aplicação quando solicitado.

5. **Para destruir a infraestrutura (CUIDADO):**

    ```bash
    terraform destroy
    ```

Repita passos similares para os diretórios `gcp/` e `azure/` conforme necessário.

### 5.3. Helm (Deploy em Kubernetes - Opcional)

**Pré-requisitos:**

* Helm CLI instalado.
* `kubectl` configurado para apontar para um cluster Kubernetes.

**Passos (exemplo):**

1. **Navegue até o diretório do chart:**

    ```bash
    cd infra/helm/my-application
    ```

2. **Faça o "lint" do chart (verificar sintaxe):**

    ```bash
    helm lint .
    ```

3. **Instale ou atualize o release:**

    ```bash
    # Para instalar um novo release
    helm install my-release-name . -n my-namespace --create-namespace

    # Para atualizar um release existente
    helm upgrade my-release-name . -n my-namespace
    ```

4. **Para desinstalar um release:**

    ```bash
    helm uninstall my-release-name -n my-namespace
    ```

## 6. Principais Decisões Técnicas

* **Terraform para Multi-Cloud:** Permite um gerenciamento unificado da infraestrutura em diferentes provedores, facilitando a portabilidade e a experimentação.
* **LocalStack para Desenvolvimento Local:** Acelera o ciclo de desenvolvimento ao simular serviços AWS, reduzindo custos e dependência de ambientes de nuvem reais durante a codificação e testes iniciais.
* **Organização Modular:** A estrutura de pastas visa separar as preocupações por provedor de nuvem (Terraform) e por aplicação (Helm), promovendo a clareza.

## 7. Links Úteis

* [Terraform Documentation](https://www.terraform.io/docs)
* [LocalStack Documentation](https://docs.localstack.cloud/)
* [Helm Documentation](https://helm.sh/docs/)
* [AWS CLI with LocalStack](https://docs.localstack.cloud/user-guide/integrations/aws-cli/)
