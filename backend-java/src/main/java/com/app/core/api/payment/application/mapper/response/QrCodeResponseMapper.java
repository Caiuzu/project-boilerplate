package com.app.core.api.payment.application.mapper.response;

import com.app.core.api.payment.application.dto.response.QrCodeResponse;
import com.app.core.api.payment.domain.model.Payment;
import com.app.core.api.shared.mapper.CycleAvoidingMappingContext;
import com.app.core.api.shared.mapper.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

/**
 * Mapper para resposta de obtenção do qr code de um pedido
 */
@Mapper(componentModel = "spring")
public interface QrCodeResponseMapper {

    QrCodeResponse toResponse(Payment payment, @Context CycleAvoidingMappingContext context);

    @DoIgnore
    default QrCodeResponse toResponse(Payment payment) {
        return toResponse(payment, new CycleAvoidingMappingContext());
    }
}
