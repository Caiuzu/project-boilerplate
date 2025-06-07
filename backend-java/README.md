# Java Spring Boot Boilerplate

Este projeto é um **boilerplate genérico para backend Java** usando Spring Boot, pronto para ser usado como base para qualquer API moderna. Ele traz exemplos mínimos, arquitetura limpa, comandos automatizados via Makefile e integração pronta com Docker, banco de dados e ferramentas de qualidade.

---

## 1. Visão Geral

- **Stack:** Java 21+, Spring Boot 3+, Gradle, Docker, Liquibase, PostgreSQL
- **Arquitetura:** Modular, inspirada em DDD e Hexagonal (Ports & Adapters)
- **Pronto para produção:** Build automatizado, migrations, testes, lint, CI/CD
- **Fácil de estender:** Estrutura de pacotes clara para novos domínios/módulos

---

## 2. Estrutura do Projeto

```
app-core-api/
├── Dockerfile                # Imagem Docker da aplicação
├── docker/
│   └── docker-compose.yml    # Orquestração de infraestrutura
├── Makefile                  # Comandos automatizados (build, run, infra, db, etc)
├── build.gradle              # Configuração do Gradle
├── settings.gradle           # Configuração de módulos
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/core/
│       │       ├── api/      # Exemplo de controller REST
│       │       ├── domain/   # Exemplo de entidade e serviço
│       │       └── ...       # Adicione seus módulos/domínios aqui
│       └── resources/
│           └── db/changelog/ # Migrations Liquibase
└── README.md                 # Este arquivo
```

---

## 3. Comandos Rápidos (Makefile)

```sh
make build        # Build do projeto
make run          # Sobe a aplicação localmente
make test         # Roda os testes
make up           # Sobe a infraestrutura (DB, etc)
make down         # Derruba a infraestrutura
make migrate      # Aplica migrations do banco
make reset-db     # Reseta o banco de dados (DANGER!)
make lint         # Lint/checkstyle (se configurado)
make clean        # Limpa builds temporários
```

---

## 4. Exemplo de Controller Genérico

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    @GetMapping
    public List<User> listUsers() {
        // Exemplo simplista
        return List.of(new User(1L, "Alice"), new User(2L, "Bob"));
    }
}
```

---

## 5. Como Estender

- Crie novos pacotes em `core/` para cada domínio (ex: `order`, `product`, `payment`)
- Siga o padrão de separar `api` (controllers), `domain` (entidades, serviços), `infrastructure` (adapters)
- Adicione migrations em `resources/db/changelog/`
- Use o Makefile para build, testes e infraestrutura

---

## 6. Links Úteis

- [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Liquibase](https://docs.liquibase.com/)
- [Gradle](https://docs.gradle.org/current/userguide/userguide.html)
- [Docker](https://docs.docker.com/)
- [Checkstyle](https://checkstyle.sourceforge.io/)

---

> Este boilerplate é um ponto de partida. Remova, adapte e evolua conforme as necessidades do seu produto!
