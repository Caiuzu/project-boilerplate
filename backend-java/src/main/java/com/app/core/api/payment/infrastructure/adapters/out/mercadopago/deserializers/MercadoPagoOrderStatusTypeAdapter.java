package com.app.core.api.payment.infrastructure.adapters.out.mercadopago.deserializers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.app.core.api.payment.application.dto.response.AcquirerOrderStatus;

import java.io.IOException;

/**
 * TypeAdapter customizado para MercadoPagoOrderStatus (enum) para uso com GSON.
 */
public class MercadoPagoOrderStatusTypeAdapter extends TypeAdapter<AcquirerOrderStatus> {
    @Override
    public void write(JsonWriter out, AcquirerOrderStatus value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.name());
        }
    }

    @Override
    public AcquirerOrderStatus read(JsonReader in) throws IOException {
        String value = in.nextString();
        return AcquirerOrderStatus.fromValue(value);
    }
}
