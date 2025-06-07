package com.app.core.api.order.infrastructure.adapters.out.persistence.mapper.shared;

import com.app.core.api.order.domain.vo.OrderNumber;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderNumberMapper {

    default OrderNumber toOrderNumber(String orderNumberString) {
        if (orderNumberString == null) return null;

        // Espera o formato "ORD-YYYY-NNNNN"
        String[] parts = orderNumberString.split("-");
        if (parts.length != 3 || !parts[0].equals("ORD")) {
            throw new IllegalArgumentException("Formato inválido para OrderNumber: " + orderNumberString);
        }

        int year = Integer.parseInt(parts[1]);
        int sequential = Integer.parseInt(parts[2]);

        return new OrderNumber(year, sequential);
    }

    default String toString(OrderNumber orderNumber) {
        if (orderNumber == null) return null;
        return orderNumber.getFormatted();
    }
}

