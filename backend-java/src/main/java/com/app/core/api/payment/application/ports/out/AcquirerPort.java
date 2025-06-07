package com.app.core.api.payment.application.ports.out;

import com.app.core.api.payment.application.dto.request.GenerateQrCodeRequest;
import com.app.core.api.payment.application.dto.response.GenerateQrCodeResponse;
import com.app.core.api.payment.application.dto.response.AcquirerOrderResponse;
import com.app.core.api.payment.application.dto.response.AcquirerPaymentsResponse;

/**
 * Porta de saída para API do adquirente
 */
public interface AcquirerPort {
    /**
     * Gera um QR Code para pagamento com base na requisição fornecida.
     *
     * @param request Objeto contendo os dados necessários para gerar o QR Code.
     */
    GenerateQrCodeResponse generateQrCode(GenerateQrCodeRequest request);

    /**
     * Consulta os pagamentos do adquirente pelo ID informado.
     *
     * @param id Identificador do pagamento.
     */
    AcquirerPaymentsResponse getAcquirerPayments(String id);

    /**
     * Consulta um pedido (order) do adquirente pelo seu ID.
     *
     * @param orderId Identificador do pedido.
     */
    AcquirerOrderResponse getAcquirerOrder(Long orderId);
}
