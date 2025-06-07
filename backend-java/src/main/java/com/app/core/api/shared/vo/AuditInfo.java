package com.app.core.api.shared.vo;

import com.app.core.api.shared.exception.BusinessException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Objeto de valor que representa informações de auditoria
 * - createdAt: data e hora de criação
 * - updatedAt: data e hora da última atualização
 */
@Getter
public class AuditInfo {
    private final LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    /**
     * Autualiza o campo updatedAt
     * 
     * @param updatedAt LocalDateTime a ser atualizado
     * @throws NullPointerException  se o updatedAt for nulo
     * @throws BusinessException se o updatedAt for menor ou igual a createdAt
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        validate(updatedAt);
        this.updatedAt = updatedAt;
    }

    /**
     * Valida o campo updatedAt
     * 
     * @param updatedAt data e hora da última atualização
     * @throws NullPointerException  se o updatedAt for nulo
     * @throws BusinessException se o updatedAt for menor ou igual a createdAt
     */
    private void validate(LocalDateTime updatedAt) {
        Objects.requireNonNull(updatedAt, "UpdatedAt não pode ser nulo");

        if (updatedAt.isBefore(createdAt)) {
            throw new BusinessException("UpdatedAt não pode ser menor ou igual a CreatedAt");
        }
        else if (updatedAt.isEqual(createdAt)) {
            updatedAt.plusNanos(1);
        }
    }
}