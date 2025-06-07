package main

import (
	"context"
	"errors"
	"fmt"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"hackathon-kit/backend-golang/internal/cache"
	"hackathon-kit/backend-golang/internal/database"
	"hackathon-kit/backend-golang/internal/handlers"
	"hackathon-kit/backend-golang/internal/middleware"
	"hackathon-kit/backend-golang/internal/repository"
	"hackathon-kit/backend-golang/internal/services"

	"github.com/gin-contrib/requestid"
	"github.com/gin-gonic/gin"
	"github.com/golang-migrate/migrate/v4"
	_ "github.com/golang-migrate/migrate/v4/database/postgres"
	_ "github.com/golang-migrate/migrate/v4/source/file"
	"github.com/joho/godotenv"
	swaggerFiles "github.com/swaggo/files"
	ginSwagger "github.com/swaggo/gin-swagger"
	"go.uber.org/zap"

	_ "hackathon-kit/backend-golang/docs"
)

// @title Hackathon Go API com Gin e GORM
// @version 1.1
// @description Exemplo de API em Go para o Hackathon com Gin, GORM, PostgreSQL, Migrations e Swagger.
// @termsOfService http://swagger.io/terms/

// @contact.name API Support
// @contact.url http://www.swagger.io/support
// @contact.email support@swagger.io

// @license.name Apache 2.0
// @license.url http://www.apache.org/licenses/LICENSE-2.0.html

// @host localhost:8888
// @BasePath /api/v1
// @schemes http https

// @securityDefinitions.apikey Bearer
// @in header
// @name Authorization
// @description Type "Bearer" followed by a space and JWT token.

func main() {
	// Carregar .env (útil para desenvolvimento local, não essencial para produção se as vars já estiverem no ambiente)
	if err := godotenv.Load(); err != nil {
		log.Println("Aviso: Arquivo .env não encontrado ou erro ao carregar. Usando variáveis de ambiente existentes.")
	}

	// Conectar ao banco de dados
	database.ConnectDB()

	// Executar migrações
	if err := runMigrations(); err != nil {
		log.Fatalf("Erro ao executar migrações: %v", err)
	}

	// Inicializa o logger
	logger, _ := zap.NewProduction()
	defer logger.Sync()

	// Inicializa o Redis
	cache.InitRedis()

	// Configurar o modo do Gin (debug, release, test)
	ginMode := os.Getenv("GIN_MODE")
	if ginMode == "" {
		ginMode = gin.DebugMode
	}
	gin.SetMode(ginMode)

	router := gin.New()

	// Middlewares Padrões (Logger e Recovery)
	router.Use(gin.Logger())
	router.Use(gin.Recovery())

	// Adiciona middlewares globais
	router.Use(middleware.Logger(logger))
	router.Use(middleware.Recovery(logger))
	router.Use(middleware.CorsConfig())
	router.Use(requestid.New())

	// --- Inicialização das camadas --- 
	// Health
	healthService := services.NewHealthService()
	healthHandler := handlers.NewHealthHandlerGin(healthService)

	// Item (CRUD)
	db := database.GetDB()
	itemRepository := repository.NewGORMItemRepository(db)
	itemService := services.NewItemService(itemRepository)
	itemHandler := handlers.NewItemHandler(itemService)

	// --- Configuração das Rotas --- 
	basePath := router.Group("/api/v1")

	// Rota de Healthcheck (dentro do /api/v1)
	basePath.GET("/health", middleware.Cache(cache.GetRedisClient(), 300), healthHandler.GetHealth)

	// Rotas do Item (dentro de /api/v1)
	itemHandler.RegisterRoutes(basePath)

	// Rota do Swagger
	swaggerURL := ginSwagger.URL(fmt.Sprintf("http://localhost:%s/swagger/doc.json", os.Getenv("APP_PORT")))
	router.GET("/swagger/*any", ginSwagger.WrapHandler(swaggerFiles.Handler, swaggerURL))

	// Iniciar o servidor
	appPort := os.Getenv("APP_PORT")
	if appPort == "" {
		appPort = "8888"
	}

	srv := &http.Server{
		Addr:    fmt.Sprintf(":%s", appPort),
		Handler: router,
	}

	log.Printf("Servidor Gin iniciando na porta %s...", appPort)
	log.Printf("Documentação Swagger disponível em http://localhost:%s/swagger/index.html", appPort)
	log.Printf("Endpoint de healthcheck disponível em http://localhost:%s/api/v1/health", appPort)
	log.Printf("Endpoints de Item disponíveis em http://localhost:%s/api/v1/items", appPort)

	// Goroutine para graceful shutdown
	go func() {
		if err := srv.ListenAndServe(); err != nil && !errors.Is(err, http.ErrServerClosed) {
			log.Fatalf("Falha ao iniciar o servidor: %v", err)
		}
	}()

	// Espera por sinal de interrupção para graceful shutdown
	quit := make(chan os.Signal, 1)
	signal.Notify(quit, syscall.SIGINT, syscall.SIGTERM)
	<-quit
	log.Println("Servidor desligando...")

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	if err := srv.Shutdown(ctx); err != nil {
		log.Fatalf("Erro no desligamento do servidor: %v", err)
	}
	log.Println("Servidor desligado com sucesso.")
}

// runMigrations executa as migrações do banco de dados.
func runMigrations() error {
	databaseURL := os.Getenv("DATABASE_URL")
	if databaseURL == "" {
		return errors.New("DATABASE_URL não definida nas variáveis de ambiente")
	}

	migrationPath := "file://internal/database/migrations"

	m, err := migrate.New(migrationPath, databaseURL)
	if err != nil {
		return fmt.Errorf("falha ao criar instância de migrate: %w", err)
	}

	if err := m.Up(); err != nil && !errors.Is(err, migrate.ErrNoChange) {
		return fmt.Errorf("falha ao aplicar migrações 'up': %w", err)
	}

	log.Println("Migrações do banco de dados aplicadas com sucesso (ou nenhuma alteração necessária).")
	return nil
} 