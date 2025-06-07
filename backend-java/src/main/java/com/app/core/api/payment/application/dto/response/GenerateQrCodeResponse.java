package com.app.core.api.payment.application.dto.response;

import lombok.Data;

/**
 * Entidade que representa o response body de geração de um QrCode
 */
@Data
public class GenerateQrCodeResponse {
    private String in_store_order_id;
    private String qr_data;
}
