package handlers

import (
	"hackathon-kit/backend-golang/internal/models"
	"hackathon-kit/backend-golang/internal/services"
	"net/http"

	"github.com/gin-gonic/gin"
)

// HealthHandlerGin manipula as requisições de healthcheck para Gin.
type HealthHandlerGin struct {
	healthService services.HealthService
}

// NewHealthHandlerGin cria uma nova instância de HealthHandlerGin.
func NewHealthHandlerGin(hs services.HealthService) *HealthHandlerGin {
	return &HealthHandlerGin{healthService: hs}
}

// GetHealth godoc
// @Summary Verifica a saúde da aplicação (Gin)
// @Description Retorna o status de saúde da aplicação, incluindo um timestamp.
// @Tags health
// @Produce  json
// @Success 200 {object} models.HealthStatus "Status de saúde da aplicação"
// @Router /api/v1/health [get]
func (h *HealthHandlerGin) GetHealth(c *gin.Context) {
	// Workaround para o erro "imported and not used" se ainda ocorrer com Swag.
	// Se o Swag estiver feliz sem isso após as mudanças de main.go, pode ser removido.
	_ = models.HealthStatus{}

	healthStatus := h.healthService.GetHealthStatus()
	c.JSON(http.StatusOK, healthStatus)
} 