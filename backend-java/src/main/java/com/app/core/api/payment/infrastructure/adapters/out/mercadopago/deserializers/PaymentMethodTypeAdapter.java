package com.app.core.api.payment.infrastructure.adapters.out.mercadopago.deserializers;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.app.core.api.payment.domain.vo.PaymentMethod;

import java.io.IOException;

/**
 * TypeAdapter customizado para PaymentMethod (enum) para uso com GSON.
 */
public class PaymentMethodTypeAdapter extends TypeAdapter<PaymentMethod> {
    @Override
    public void write(JsonWriter out, PaymentMethod value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.name());
        }
    }

    @Override
    public PaymentMethod read(JsonReader in) throws IOException {
        String value = in.nextString();
        return PaymentMethod.fromValue(value);
    }
}
