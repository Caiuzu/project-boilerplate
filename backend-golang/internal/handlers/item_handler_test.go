package handlers

import (
	"bytes"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/gin-gonic/gin"
	"github.com/stretchr/testify/assert"
)

type mockItemService struct{}

func (m *mockItemService) CreateItem(input interface{}) (interface{}, error) {
	return map[string]interface{}{"id": 1, "name": "Item Teste"}, nil
}
func (m *mockItemService) GetAllItems() ([]interface{}, error) {
	return []interface{}{map[string]interface{}{"id": 1, "name": "Item Teste"}}, nil
}
func (m *mockItemService) GetItemByID(id interface{}) (interface{}, error) {
	if id == "1" {
		return map[string]interface{}{"id": 1, "name": "Item Teste"}, nil
	}
	return nil, nil
}
func (m *mockItemService) UpdateItem(id interface{}, input interface{}) (interface{}, error) {
	return map[string]interface{}{"id": 1, "name": "Item Atualizado"}, nil
}
func (m *mockItemService) DeleteItem(id interface{}) error {
	return nil
}

func setupRouterWithItemHandler() *gin.Engine {
	gin.SetMode(gin.TestMode)
	r := gin.New()
	itemHandler := NewItemHandler(&mockItemService{})
	api := r.Group("/api/v1")
	itemHandler.RegisterRoutes(api)
	return r
}

func TestItemHandler_GetAllItems(t *testing.T) {
	r := setupRouterWithItemHandler()
	w := httptest.NewRecorder()
	req, _ := http.NewRequest("GET", "/api/v1/items", nil)
	r.ServeHTTP(w, req)
	assert.Equal(t, 200, w.Code)
	assert.Contains(t, w.Body.String(), "Item Teste")
}

func TestItemHandler_CreateItem(t *testing.T) {
	r := setupRouterWithItemHandler()
	w := httptest.NewRecorder()
	body := map[string]interface{}{"name": "Novo Item"}
	jsonBody, _ := json.Marshal(body)
	req, _ := http.NewRequest("POST", "/api/v1/items", bytes.NewBuffer(jsonBody))
	req.Header.Set("Content-Type", "application/json")
	r.ServeHTTP(w, req)
	assert.Equal(t, 200, w.Code)
	assert.Contains(t, w.Body.String(), "Item Teste")
}

func TestItemHandler_GetItemByID(t *testing.T) {
	r := setupRouterWithItemHandler()
	w := httptest.NewRecorder()
	req, _ := http.NewRequest("GET", "/api/v1/items/1", nil)
	r.ServeHTTP(w, req)
	assert.Equal(t, 200, w.Code)
	assert.Contains(t, w.Body.String(), "Item Teste")
}

func TestItemHandler_UpdateItem(t *testing.T) {
	r := setupRouterWithItemHandler()
	w := httptest.NewRecorder()
	body := map[string]interface{}{"name": "Item Atualizado"}
	jsonBody, _ := json.Marshal(body)
	req, _ := http.NewRequest("PUT", "/api/v1/items/1", bytes.NewBuffer(jsonBody))
	req.Header.Set("Content-Type", "application/json")
	r.ServeHTTP(w, req)
	assert.Equal(t, 200, w.Code)
	assert.Contains(t, w.Body.String(), "Item Atualizado")
}

func TestItemHandler_DeleteItem(t *testing.T) {
	r := setupRouterWithItemHandler()
	w := httptest.NewRecorder()
	req, _ := http.NewRequest("DELETE", "/api/v1/items/1", nil)
	r.ServeHTTP(w, req)
	assert.Equal(t, 200, w.Code)
} 