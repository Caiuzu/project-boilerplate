# Estágio de build
FROM golang:1.22-alpine AS builder

WORKDIR /app

# Copia os arquivos go.mod e go.sum para baixar as dependências primeiro
COPY go.mod go.sum ./

# Baixa as dependências
RUN go mod download
RUN go mod verify

# Copia o restante do código da aplicação
# Copia a pasta cmd, internal, pkg e qualquer outro código Go necessário
COPY cmd/ ./cmd/
COPY internal/ ./internal/
COPY pkg/ ./pkg/
# Se você tiver outros diretórios com código Go na raiz, copie-os também.
# Ex: COPY migrations/ ./migrations/

# Opcional: Gerar arquivos do Swagger DENTRO do Dockerfile (se não forem commitados)
# RUN go install github.com/swaggo/swag/cmd/swag@latest
# RUN swag init -g cmd/server/main.go --output ./docs

# Constrói o executável a partir do novo local do main.go
# O -o app especifica que o output será um arquivo chamado 'app'
# O CGO_ENABLED=0 garante um build estaticamente linkado sem dependências C
RUN CGO_ENABLED=0 go build -ldflags="-s -w" -o app cmd/server/main.go

# Estágio final - Imagem leve
FROM alpine:latest

WORKDIR /app

# Copia o executável do estágio de build
COPY --from=builder /app/app .

# Copia o diretório 'docs' gerado pelo Swag
# Se o swag init foi rodado no builder (ou localmente e os docs estão sendo copiados para o contexto do builder),
# os docs estarão em /app/docs e devem ser copiados para o diretório docs da imagem final.
COPY --from=builder /app/docs ./docs
# Se os docs não são gerados no builder mas commitados na raiz, mude para:
# COPY docs ./docs


# Expõe a porta que a aplicação Go vai usar (deve ser a mesma do main.go)
EXPOSE 8888

# Comando para rodar a aplicação
CMD ["./app"] 