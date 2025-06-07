package com.app.core.api.payment.application.mapper.request;

import com.app.core.api.order.domain.events.OrderCreatedEvent;
import com.app.core.api.payment.application.dto.request.GenerateQrCodeRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper responsável por converter objetos {@link OrderCreatedEvent} em {@link GenerateQrCodeRequest},
 * que representam o corpo da requisição para geração de um QR Code de pagamento.
 *
 */
@Mapper(componentModel = "spring", uses = GenerateQrCodeItemRequestMapper.class)
public interface GenerateQrCodeRequestMapper {

    @Mapping(target = "external_reference", source = "id")
    @Mapping(target = "title", expression = "java(\"Pedido #\" + event.getOrderNumber())")
    @Mapping(target = "description", source = "statusDescription")
    @Mapping(target = "notification_url", ignore = true)
    @Mapping(target = "total_amount", source = "totalAmount")
    @Mapping(target = "items", source = "items")
    @Mapping(target = "expiration_date", ignore = true)
    GenerateQrCodeRequest toRequest(OrderCreatedEvent event);
}
