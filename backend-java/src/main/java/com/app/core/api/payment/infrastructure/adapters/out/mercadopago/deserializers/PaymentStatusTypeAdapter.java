package com.app.core.api.payment.infrastructure.adapters.out.mercadopago.deserializers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.app.core.api.payment.domain.vo.PaymentStatus;

import java.io.IOException;

/**
 * TypeAdapter customizado para PaymentStatus para uso com GSON.
 */
public class PaymentStatusTypeAdapter extends TypeAdapter<PaymentStatus> {
    @Override
    public void write(JsonWriter out, PaymentStatus value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.name());
        }
    }

    @Override
    public PaymentStatus read(JsonReader in) throws IOException {
        String value = in.nextString();
        return PaymentStatus.fromValue(value);
    }
}
