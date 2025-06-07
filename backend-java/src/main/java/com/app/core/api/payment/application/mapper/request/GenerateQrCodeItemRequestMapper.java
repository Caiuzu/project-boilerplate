package com.app.core.api.payment.application.mapper.request;

import com.app.core.api.order.domain.events.OrderItemCreatedEvent;
import com.app.core.api.payment.application.dto.request.GenerateQrCodeItemRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper responsável por converter objetos {@link OrderItemCreatedEvent} em {@link GenerateQrCodeItemRequest},
 * utilizados na geração de QR Codes de pagamento.
 *
 */
@Mapper(componentModel = "spring")
public interface GenerateQrCodeItemRequestMapper {

    @Mapping(target = "sku_number", source = "productId", qualifiedByName = "toString")
    @Mapping(target = "category", constant = "App")
    @Mapping(target = "title", source = "name")
    @Mapping(target = "description", source = "observations")
    @Mapping(target = "unit_price", source = "unitPrice")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "unit_measure", constant = "unit")
    @Mapping(target = "total_amount", source = "subtotal")
    GenerateQrCodeItemRequest toRequest(OrderItemCreatedEvent item);

    @Named("toString")
    static String longToString(Long id) {
        return id != null ? id.toString() : null;
    }
}
