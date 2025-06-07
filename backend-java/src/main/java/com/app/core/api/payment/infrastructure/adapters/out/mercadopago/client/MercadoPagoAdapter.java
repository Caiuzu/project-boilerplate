package com.app.core.api.payment.infrastructure.adapters.out.mercadopago.client;

import com.app.core.api.payment.application.dto.request.GenerateQrCodeRequest;
import com.app.core.api.payment.application.dto.response.GenerateQrCodeResponse;
import com.app.core.api.payment.application.dto.response.AcquirerOrderResponse;
import com.app.core.api.payment.application.dto.response.AcquirerPaymentsResponse;
import com.app.core.api.payment.application.ports.out.AcquirerPort;
import com.app.core.api.payment.infrastructure.adapters.out.mercadopago.config.MercadoPagoProperties;
import com.app.core.api.payment.infrastructure.adapters.out.mercadopago.exceptions.MercadoPagoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import retrofit2.Response;

/**
 * Implementação da porta de saída referente a API do mercado pago
 */
@Component
@Slf4j
public class MercadoPagoAdapter implements AcquirerPort {

    private final MercadoPagoClient client;
    private final MercadoPagoProperties properties;

    public MercadoPagoAdapter(
            MercadoPagoClient client,
            MercadoPagoProperties mercadoPagoProperties
    ) {
        this.client = client;
        this.properties = mercadoPagoProperties;
    }

    @Override
    public GenerateQrCodeResponse generateQrCode(GenerateQrCodeRequest request) {
        try {
            request.setNotification_url(properties.getNotificationUrl());
            Response<GenerateQrCodeResponse> response = client.generateQrCode(
                    properties.getUserId(),
                    properties.getPosId(),
                    request
            ).execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                log.warn("Erro ao contatar API do mercado pago para gerar QrCode");
                String errorBody = response.errorBody() != null ? response.errorBody().string() : "Erro desconhecido";
                throw new MercadoPagoException(
                        "Erro na API Mercado Pago: " + errorBody + " | Status code: " + response.code(),
                        null,
                        response.code()
                );
            }
        } catch (MercadoPagoException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao contatar API do mercado pago para gerar QrCode");
            throw new MercadoPagoException("Erro inesperado ao chamar API Mercado Pago", e, 500);
        }
    }

    @Override
    public AcquirerPaymentsResponse getAcquirerPayments(String id) {
        try {
            var response = client.getMercadoPagoPayments(id).execute();

            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                log.warn("Erro ao contatar API do mercado pago para obter dados do pagamento");

                String errorBody = response.errorBody() != null ? response.errorBody().string() : "Erro desconhecido";

                throw new MercadoPagoException(
                        "Erro na API Mercado Pago: " + errorBody + " | Status code: " + response.code(),
                        null,
                        response.code()
                );
            }
        } catch (MercadoPagoException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao contatar API do mercado pago obter dados do pagamento");
            throw new MercadoPagoException("Erro inesperado ao chamar API Mercado Pago", e, 500);
        }

    }

    @Override
    public AcquirerOrderResponse getAcquirerOrder(Long orderId) {
        try {
            var response = client.getMercadoPagoOrder(orderId).execute();

            if (response.isSuccessful() && response.body() != null) {
                return response.body();
            } else {
                log.warn("Erro ao contatar API do mercado pago para obter dados do pedido");

                String errorBody = response.errorBody() != null ? response.errorBody().string() : "Erro desconhecido";

                throw new MercadoPagoException(
                        "Erro na API Mercado Pago: " + errorBody + " | Status code: " + response.code(),
                        null,
                        response.code()
                );
            }
        } catch (MercadoPagoException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao contatar API do mercado pago para obter dados do pedido");
            throw new MercadoPagoException("Erro inesperado ao chamar API Mercado Pago", e, 500);
        }

    }
}