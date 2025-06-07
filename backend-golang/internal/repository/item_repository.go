package repository

import (
	"hackathon-kit/backend-golang/internal/models"

	"gorm.io/gorm"
)

// ItemRepository define a interface para as operações de banco de dados de Item.
type ItemRepository interface {
	Create(item *models.Item) error
	GetAll() ([]models.Item, error)
	GetByID(id uint) (*models.Item, error)
	Update(item *models.Item) error
	Delete(id uint) error
}

// gormItemRepository é a implementação de ItemRepository usando GORM.
type gormItemRepository struct {
	db *gorm.DB
}

// NewGORMItemRepository cria uma nova instância de ItemRepository.
func NewGORMItemRepository(db *gorm.DB) ItemRepository {
	return &gormItemRepository{db: db}
}

func (r *gormItemRepository) Create(item *models.Item) error {
	return r.db.Create(item).Error
}

func (r *gormItemRepository) GetAll() ([]models.Item, error) {
	var items []models.Item
	err := r.db.Order("created_at desc").Find(&items).Error
	return items, err
}

func (r *gormItemRepository) GetByID(id uint) (*models.Item, error) {
	var item models.Item
	err := r.db.First(&item, id).Error
	if err != nil {
		return nil, err // GORM retorna gorm.ErrRecordNotFound se não encontrar
	}
	return &item, nil
}

func (r *gormItemRepository) Update(item *models.Item) error {
	// O GORM atualiza apenas campos não zero por padrão com Save.
	// Se você quiser permitir a atualização para valores zero (ex: limpar descrição),
	// use .Model(&item).Updates(map[string]interface{...}) ou .Select("*").Updates(item)
	return r.db.Save(item).Error
}

func (r *gormItemRepository) Delete(id uint) error {
	return r.db.Delete(&models.Item{}, id).Error
} 