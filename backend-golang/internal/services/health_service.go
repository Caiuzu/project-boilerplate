package services

import (
	"time"

	"hackathon-kit/backend-golang/internal/models"
)

// HealthService define a interface para o serviço de healthcheck.
type HealthService interface {
	GetHealthStatus() models.HealthStatus
}

// healthServiceImpl é a implementação de HealthService.
type healthServiceImpl struct {
	// Aqui poderiam entrar dependências como conexões de banco de dados, etc.
}

// NewHealthService cria uma nova instância de HealthService.
func NewHealthService() HealthService {
	return &healthServiceImpl{}
}

// GetHealthStatus retorna o status atual da aplicação.
// Por enquanto, simplesmente retorna "ok".
// No futuro, pode ser expandido para verificar conexões com banco de dados, serviços externos, etc.
func (s *healthServiceImpl) GetHealthStatus() models.HealthStatus {
	return models.HealthStatus{
		Status:    "ok",
		Timestamp: time.Now().UTC(),
	}
}
