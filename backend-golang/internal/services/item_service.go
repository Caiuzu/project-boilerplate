package services

import (
	"errors"
	"hackathon-kit/backend-golang/api"
	"hackathon-kit/backend-golang/internal/models"
	"hackathon-kit/backend-golang/internal/repository"
	// "time" // Removido pois não estava sendo usado diretamente

	"gorm.io/gorm"
)

// ItemService define a interface para a lógica de negócios de Item.
type ItemService interface {
	CreateItem(dto api.CreateItemDTO) (*models.Item, error)
	GetAllItems() ([]models.Item, error)
	GetItemByID(id uint) (*models.Item, error)
	UpdateItem(id uint, dto api.UpdateItemDTO) (*models.Item, error)
	DeleteItem(id uint) error
}

// itemServiceImpl é a implementação de ItemService.
type itemServiceImpl struct {
	repo repository.ItemRepository
}

// NewItemService cria uma nova instância de ItemService.
func NewItemService(repo repository.ItemRepository) ItemService {
	return &itemServiceImpl{repo: repo}
}

func (s *itemServiceImpl) CreateItem(dto api.CreateItemDTO) (*models.Item, error) {
	item := &models.Item{
		Name:        dto.Name,
		Description: dto.Description,
		// CreatedAt e UpdatedAt são gerenciados pelo GORM/DB
	}
	err := s.repo.Create(item)
	if err != nil {
		return nil, err
	}
	return item, nil
}

func (s *itemServiceImpl) GetAllItems() ([]models.Item, error) {
	return s.repo.GetAll()
}

func (s *itemServiceImpl) GetItemByID(id uint) (*models.Item, error) {
	item, err := s.repo.GetByID(id)
	if err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			return nil, nil // Retorna nil, nil se não encontrado para o handler tratar como 404
		}
		return nil, err
	}
	return item, nil
}

func (s *itemServiceImpl) UpdateItem(id uint, dto api.UpdateItemDTO) (*models.Item, error) {
	existingItem, err := s.repo.GetByID(id)
	if err != nil {
		if errors.Is(err, gorm.ErrRecordNotFound) {
			return nil, nil // Não encontrado
		}
		return nil, err
	}
	if existingItem == nil {
	    return nil, nil // Não encontrado
	}

	updated := false
	if dto.Name != nil && *dto.Name != "" {
		existingItem.Name = *dto.Name
		updated = true
	}
	if dto.Description != nil {
		existingItem.Description = *dto.Description
		updated = true
	}

	if !updated {
		// Nenhum campo para atualizar foi fornecido ou os valores são os mesmos
		// Pode-se optar por retornar o item existente ou um erro/mensagem específica.
		// Por simplicidade, retornaremos o item existente.
		return existingItem, nil
	}
    
    // GORM atualiza o UpdatedAt automaticamente se o campo existir e for time.Time ou *time.Time
    // Se o trigger do banco não estiver funcionando ou para controle explícito:
    // existingItem.UpdatedAt = time.Now()

	err = s.repo.Update(existingItem)
	if err != nil {
		return nil, err
	}
	return existingItem, nil
}

func (s *itemServiceImpl) DeleteItem(id uint) error {
    // Verifica se o item existe antes de deletar para poder retornar 404 se não existir.
    _, err := s.repo.GetByID(id)
    if err != nil {
        if errors.Is(err, gorm.ErrRecordNotFound) {
            return gorm.ErrRecordNotFound // Propaga o erro para o handler tratar como 404
        }
        return err
    }
	return s.repo.Delete(id)
} 