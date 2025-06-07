FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copiar arquivos de dependências e build
COPY gradle/ ./gradle/
COPY gradlew build.gradle settings.gradle ./
RUN chmod +x ./gradlew

# Download de dependências
RUN ./gradlew dependencies --no-daemon

# Copiar código fonte
COPY src/ ./src/

# Construir o aplicativo
RUN ./gradlew build -x test --no-daemon

# Imagem final
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Atualizar ferramentas básicas para health checks
RUN apk add --no-cache curl wget tzdata

# Definir timezone para GMT-3 (exemplo America/Sao_Paulo)
RUN cp /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime && \
    echo "America/Sao_Paulo" > /etc/timezone

# Adicionar usuário não-root para segurança
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copiar JAR construído
COPY --from=build /app/build/libs/*.jar app.jar

# Mudar para usuário não-root
USER appuser

# Porta da aplicação (modificável via docker-compose)
EXPOSE 8080

# Parâmetros para Monitoramento
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:+UseStringDeduplication -Djava.security.egd=file:/dev/./urandom"

# Verificação de saúde interna
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD wget --spider -q http://localhost:8080/actuator/health || exit 1

# Iniciar aplicação
ENTRYPOINT ["java", "-jar", "/app/app.jar"]