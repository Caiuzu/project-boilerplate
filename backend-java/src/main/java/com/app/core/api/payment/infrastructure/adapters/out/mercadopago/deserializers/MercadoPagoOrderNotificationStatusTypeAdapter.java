package com.app.core.api.payment.infrastructure.adapters.out.mercadopago.deserializers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.app.core.api.payment.application.dto.response.AcquirerOrderNotificationStatus;

import java.io.IOException;

/**
 * TypeAdapter customizado para MercadoPagoOrderNotificationStatus (enum) para uso com GSON.
 */
public class MercadoPagoOrderNotificationStatusTypeAdapter extends TypeAdapter<AcquirerOrderNotificationStatus> {
    @Override
    public void write(JsonWriter out, AcquirerOrderNotificationStatus value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.name());
        }
    }

    @Override
    public AcquirerOrderNotificationStatus read(JsonReader in) throws IOException {
        String value = in.nextString();
        return AcquirerOrderNotificationStatus.fromValue(value);
    }
}
