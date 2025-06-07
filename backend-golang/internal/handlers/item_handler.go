package handlers

import (
	"errors"
	"hackathon-kit/backend-golang/api"
	"hackathon-kit/backend-golang/internal/services"
	"net/http"
	"strconv"

	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
)

// ItemHandler manipula as requisições HTTP para Items.
type ItemHandler struct {
	service services.ItemService
}

// NewItemHandler cria uma nova instância de ItemHandler.
func NewItemHandler(service services.ItemService) *ItemHandler {
	return &ItemHandler{service: service}
}

// RegisterRoutes registra as rotas de Item no roteador Gin.
func (h *ItemHandler) RegisterRoutes(router *gin.RouterGroup) {
	itemRoutes := router.Group("/items")
	{
		itemRoutes.POST("", h.CreateItem)
		itemRoutes.GET("", h.GetAllItems)
		itemRoutes.GET("/:id", h.GetItemByID)
		itemRoutes.PUT("/:id", h.UpdateItem)
		itemRoutes.DELETE("/:id", h.DeleteItem)
	}
}

// CreateItem godoc
// @Summary Cria um novo item
// @Description Cria um novo item com nome e descrição.
// @Tags items
// @Accept  json
// @Produce  json
// @Param   item body api.CreateItemDTO true "Payload do Item"
// @Success 201 {object} api.ItemResponseDTO "Item criado com sucesso"
// @Failure 400 {object} map[string]string "Erro de validação ou entrada inválida"
// @Failure 500 {object} map[string]string "Erro interno do servidor"
// @Router /items [post]
func (h *ItemHandler) CreateItem(c *gin.Context) {
	var dto api.CreateItemDTO
	if err := c.ShouldBindJSON(&dto); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Entrada inválida: " + err.Error()})
		return
	}

	item, err := h.service.CreateItem(dto)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Falha ao criar item: " + err.Error()})
		return
	}
	c.JSON(http.StatusCreated, item)
}

// GetAllItems godoc
// @Summary Lista todos os itens
// @Description Retorna uma lista de todos os itens.
// @Tags items
// @Produce  json
// @Success 200 {array} models.Item "Lista de itens"
// @Failure 500 {object} map[string]string "Erro interno do servidor"
// @Router /items [get]
func (h *ItemHandler) GetAllItems(c *gin.Context) {
	items, err := h.service.GetAllItems()
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Falha ao listar itens: " + err.Error()})
		return
	}
	c.JSON(http.StatusOK, items)
}

// GetItemByID godoc
// @Summary Busca um item pelo ID
// @Description Retorna um único item baseado no seu ID.
// @Tags items
// @Produce  json
// @Param   id path int true "ID do Item"
// @Success 200 {object} models.Item "Item encontrado"
// @Failure 400 {object} map[string]string "ID inválido"
// @Failure 404 {object} map[string]string "Item não encontrado"
// @Failure 500 {object} map[string]string "Erro interno do servidor"
// @Router /items/{id} [get]
func (h *ItemHandler) GetItemByID(c *gin.Context) {
	idStr := c.Param("id")
	id, err := strconv.ParseUint(idStr, 10, 32) // uint para corresponder ao ID do GORM
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "ID inválido"})
		return
	}

	item, err := h.service.GetItemByID(uint(id))
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Falha ao buscar item: " + err.Error()})
		return
	}
	if item == nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "Item não encontrado"})
		return
	}
	c.JSON(http.StatusOK, item)
}

// UpdateItem godoc
// @Summary Atualiza um item existente
// @Description Atualiza os detalhes de um item existente baseado no ID.
// @Tags items
// @Accept  json
// @Produce  json
// @Param   id   path int                true "ID do Item"
// @Param   item body api.UpdateItemDTO true "Payload de atualização do Item"
// @Success 200 {object} api.ItemResponseDTO "Item atualizado com sucesso"
// @Failure 400 {object} map[string]string "ID inválido ou entrada inválida"
// @Failure 404 {object} map[string]string "Item não encontrado"
// @Failure 500 {object} map[string]string "Erro interno do servidor"
// @Router /items/{id} [put]
func (h *ItemHandler) UpdateItem(c *gin.Context) {
	idStr := c.Param("id")
	id, err := strconv.ParseUint(idStr, 10, 32)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "ID inválido"})
		return
	}

	var dto api.UpdateItemDTO
	if err := c.ShouldBindJSON(&dto); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Entrada inválida: " + err.Error()})
		return
	}

	item, err := h.service.UpdateItem(uint(id), dto)
	if err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			c.JSON(http.StatusInternalServerError, gin.H{"error": "Falha ao atualizar item, possível erro de concorrência ou item não encontrado: " + err.Error()})
		} else {
			c.JSON(http.StatusInternalServerError, gin.H{"error": "Falha ao atualizar item: " + err.Error()})
		}
		return
	}
	if item == nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "Item não encontrado para atualização"})
		return
	}
	c.JSON(http.StatusOK, item)
}

// DeleteItem godoc
// @Summary Remove um item
// @Description Remove um item existente baseado no ID.
// @Tags items
// @Produce  json
// @Param   id path int true "ID do Item"
// @Success 204 "Item removido com sucesso"
// @Failure 400 {object} map[string]string "ID inválido"
// @Failure 404 {object} map[string]string "Item não encontrado"
// @Failure 500 {object} map[string]string "Erro interno do servidor"
// @Router /items/{id} [delete]
func (h *ItemHandler) DeleteItem(c *gin.Context) {
	idStr := c.Param("id")
	id, err := strconv.ParseUint(idStr, 10, 32)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "ID inválido"})
		return
	}

	err = h.service.DeleteItem(uint(id))
	if err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			c.JSON(http.StatusNotFound, gin.H{"error": "Item não encontrado"})
		} else {
			c.JSON(http.StatusInternalServerError, gin.H{"error": "Falha ao remover item: " + err.Error()})
		}
		return
	}
	c.Status(http.StatusNoContent)
}
