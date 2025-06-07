package com.app.core.api.payment.domain.vo;

import java.util.Objects;

/**
 * Value Object que representa o conteúdo de um QR Code.
 */
public record QrCode(String value) {

    /**
     * Construtor que cria uma nova instância de QrCode com os dados fornecidos.
     *
     * @throws NullPointerException se o conteúdo do QrCode for nulo
     * @throws IllegalArgumentException se a data do QrCode ultrapassar o tamanho permitido
     */
    public QrCode {
        Objects.requireNonNull(value, "QrCode não pode ser nulo");

        if (value.length() > 255) {
            throw new IllegalArgumentException("O conteúdo do QrCode não pode ultrapassar 255 caracteres");
        }
    }
}