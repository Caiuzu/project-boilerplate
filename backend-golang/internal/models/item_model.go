package models

import (
	"time"
)

// Item representa a entidade Item no banco de dados.
// @Description Modelo de dados para um item.
type Item struct {
	ID          uint      `gorm:"primaryKey" json:"id" example:"1"`
	Name        string    `gorm:"not null" json:"name" binding:"required" example:"Item de Teste"`
	Description string    `json:"description" example:"Esta é uma descrição do item."`
	CreatedAt   time.Time `json:"created_at" example:"2024-05-31T10:00:00Z"`
	UpdatedAt   time.Time `json:"updated_at" example:"2024-05-31T10:05:00Z"`
}

// TableName especifica o nome da tabela para o modelo Item.
func (Item) TableName() string {
	return "items"
} 