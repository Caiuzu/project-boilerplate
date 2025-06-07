package com.app.core.api.payment.application.mapper.response;

import com.app.core.api.payment.application.dto.response.PaymentStatusResponse;
import com.app.core.api.payment.domain.model.Payment;
import com.app.core.api.shared.mapper.CycleAvoidingMappingContext;
import com.app.core.api.shared.mapper.DoIgnore;
import org.mapstruct.Context;
import org.mapstruct.Mapper;

/**
 * Mapper para resposta de checagem de pagamento
 */
@Mapper(componentModel = "spring")
public interface PaymentStatusResponseMapper {

    PaymentStatusResponse toResponse(Payment payment, @Context CycleAvoidingMappingContext context);

    @DoIgnore
    default PaymentStatusResponse toResponse(Payment payment) {
        return toResponse(payment, new CycleAvoidingMappingContext());
    }
}
