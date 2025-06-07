# Etapa de build
FROM node:20-slim AS builder

ENV TZ=America/Sao_Paulo
ENV NODE_ENV=production

RUN apt-get update && apt-get install -y --no-install-recommends \
    tzdata \
    curl \
    unzip \
    && rm -rf /var/lib/apt/lists/*  

RUN npm install -g bun

WORKDIR /app

COPY package.json bun.lockb ./
RUN bun install --frozen-lockfile
RUN bun install critters

COPY . .

RUN bun run build

# Etapa de produção
FROM node:20-slim AS runner

ENV NODE_ENV=production
ENV TZ=America/Sao_Paulo

RUN npm install -g bun

WORKDIR /app

COPY --from=builder /app/.next/standalone ./
COPY --from=builder /app/.next/static ./.next/static
COPY --from=builder /app/public ./public

# Copie apenas o que for necessário para rodar a aplicação
COPY --from=builder /app/package.json ./
COPY --from=builder /app/bun.lockb ./

EXPOSE 3000

CMD ["bun", "run", "server.js"]