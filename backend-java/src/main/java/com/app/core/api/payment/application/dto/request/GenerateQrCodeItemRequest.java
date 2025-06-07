package com.app.core.api.payment.application.dto.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Entidade que representa os itens do pedido no body de geração de um QrCode
 */
@Data
public class GenerateQrCodeItemRequest {
    private String sku_number;
    private String category;
    private String title;
    private String description;
    private BigDecimal unit_price;
    private int quantity;
    private String unit_measure;
    private BigDecimal total_amount;
}
