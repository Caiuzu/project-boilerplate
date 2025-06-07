package middleware

import (
	"bytes"
	"fmt"
	"log"
	"time"

	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"
)

// Configuração do CORS
func CorsConfig() gin.HandlerFunc {
	corsConfig := cors.Config{
		AllowOrigins:     []string{"*"},
		AllowMethods:     []string{"GET", "POST", "PUT", "PATCH", "DELETE", "HEAD", "OPTIONS"},
		AllowHeaders:     []string{"Origin", "Content-Length", "Content-Type", "Authorization", "X-Request-ID"},
		ExposeHeaders:    []string{"Content-Length"},
		AllowCredentials: true,
		MaxAge:           12 * time.Hour,
	}
	return cors.New(corsConfig)
}

// Logger middleware usando zap
func Logger(logger *zap.Logger) gin.HandlerFunc {
	return func(c *gin.Context) {
		start := time.Now()
		path := c.Request.URL.Path
		query := c.Request.URL.RawQuery

		// Processa a requisição
		c.Next()

		// Coleta métricas após a requisição
		latency := time.Since(start)
		statusCode := c.Writer.Status()
		clientIP := c.ClientIP()
		method := c.Request.Method
		requestID := c.GetHeader("X-Request-ID")

		// Log estruturado
		logger.Info("Request completed",
			zap.String("request_id", requestID),
			zap.String("method", method),
			zap.String("path", path),
			zap.String("query", query),
			zap.Int("status", statusCode),
			zap.String("ip", clientIP),
			zap.Duration("latency", latency),
		)
	}
}

// Recovery middleware com logging de erros
func Recovery(logger *zap.Logger) gin.HandlerFunc {
	return func(c *gin.Context) {
		defer func() {
			if err := recover(); err != nil {
				// Log do erro
				logger.Error("Panic recovered",
					zap.Any("error", err),
					zap.String("path", c.Request.URL.Path),
					zap.String("method", c.Request.Method),
					zap.String("request_id", c.GetHeader("X-Request-ID")),
				)

				// Resposta de erro
				c.AbortWithStatusJSON(500, gin.H{
					"error": "Internal Server Error",
					"request_id": c.GetHeader("X-Request-ID"),
				})
			}
		}()
		c.Next()
	}
}

// Cache middleware usando Redis
// O parâmetro ttl é em segundos
func Cache(redisClient *redis.Client, ttl time.Duration) gin.HandlerFunc {
	return func(c *gin.Context) {
		// Só aplica cache para GET requests
		if c.Request.Method != "GET" {
			c.Next()
			return
		}

		// Gera chave do cache
		cacheKey := fmt.Sprintf("cache:%s:%s", c.Request.Method, c.Request.URL.Path)

		// Tenta obter do cache
		val, err := redisClient.Get(c.Request.Context(), cacheKey).Result()
		if err == nil {
			// Cache hit
			c.Header("X-Cache", "HIT")
			c.Header("Content-Type", "application/json")
			c.String(200, val)
			c.Abort()
			return
		}

		// Cache miss - continua com o handler normal
		c.Header("X-Cache", "MISS")
		
		// Captura a resposta
		writer := &responseWriter{ResponseWriter: c.Writer}
		c.Writer = writer
		c.Next()

		// Se a resposta foi bem sucedida, salva no cache
		if c.Writer.Status() == 200 && writer.body != nil {
			// Salva no Redis com o TTL correto
			err = redisClient.Set(
				c.Request.Context(), 
				cacheKey, 
				writer.body.String(), 
				ttl*time.Second, // Multiplicamos por Second para garantir que o Redis use segundos
			).Err()
			
			if err != nil {
				log.Printf("Erro ao salvar resposta no cache: %v", err)
			}
		}
	}
}

// responseWriter é um wrapper para gin.ResponseWriter que armazena o corpo da resposta
type responseWriter struct {
	gin.ResponseWriter
	body *bytes.Buffer
}

// Write implementa a interface io.Writer
func (w *responseWriter) Write(b []byte) (int, error) {
	if w.body == nil {
		w.body = &bytes.Buffer{}
	}
	w.body.Write(b)
	return w.ResponseWriter.Write(b)
} 