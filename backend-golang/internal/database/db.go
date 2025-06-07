package database

import (
	"fmt"
	"log"
	"os"
	"time"

	"github.com/joho/godotenv"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
	"gorm.io/gorm/logger"
)

var DB *gorm.DB

// ConnectDB inicializa a conexão com o banco de dados usando GORM.
func ConnectDB() {
	// Carrega variáveis de ambiente do .env se não estiver em produção
	if os.Getenv("GIN_MODE") != "release" {
		err := godotenv.Load() // Carrega do .env na raiz do projeto
		if err != nil {
			log.Println("Aviso: Não foi possível carregar o arquivo .env. Usando variáveis de ambiente existentes ou padrões.")
		}
	}

	dbHost := os.Getenv("DB_HOST")
	dbPort := os.Getenv("DB_PORT")
	dbUser := os.Getenv("DB_USER")
	dbPassword := os.Getenv("DB_PASSWORD")
	dbName := os.Getenv("DB_NAME")
	dbSSLMode := os.Getenv("DB_SSLMODE")

	dsn := fmt.Sprintf("host=%s user=%s password=%s dbname=%s port=%s sslmode=%s TimeZone=America/Sao_Paulo",
		dbHost, dbUser, dbPassword, dbName, dbPort, dbSSLMode)

	var err error
	DB, err = gorm.Open(postgres.Open(dsn), &gorm.Config{
		Logger: logger.Default.LogMode(logger.Info), // Log GORM
	})

	if err != nil {
		log.Fatalf("Falha ao conectar ao banco de dados: %v", err)
	}

	log.Println("Conexão com o banco de dados estabelecida com sucesso.")

	// Configurações do Pool de Conexão (opcional, mas recomendado)
	sqlDB, err := DB.DB()
	if err != nil {
		log.Fatalf("Falha ao obter o objeto DB genérico: %v", err)
	}
	sqlDB.SetMaxIdleConns(10)           // Número máximo de conexões inativas
	sqlDB.SetMaxOpenConns(100)          // Número máximo de conexões abertas
	sqlDB.SetConnMaxLifetime(time.Hour) // Tempo máximo de vida de uma conexão
}

// GetDB retorna a instância de GORM DB.
// É uma boa prática ter um getter se você não quiser expor a variável DB globalmente de forma direta.
func GetDB() *gorm.DB {
	return DB
} 