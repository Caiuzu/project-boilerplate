package models

import "time"

// HealthStatus representa a resposta do endpoint de healthcheck.
// @Description Modelo de resposta para o status de saúde da aplicação.
type HealthStatus struct {
	Status    string    `json:"status" example:"ok"`
	Timestamp time.Time `json:"timestamp" example:"2024-05-31T10:00:00Z"`
} 