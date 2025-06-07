package com.app.core.api.payment.infrastructure.adapters.out.mercadopago.config;

import com.app.core.api.payment.infrastructure.adapters.out.mercadopago.deserializers.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.app.core.api.payment.application.dto.response.AcquirerOrderNotificationStatus;
import com.app.core.api.payment.application.dto.response.AcquirerOrderStatus;
import com.app.core.api.payment.domain.vo.PaymentMethod;
import com.app.core.api.payment.domain.vo.PaymentStatus;
import com.app.core.api.payment.infrastructure.adapters.out.mercadopago.client.MercadoPagoClient;
import com.app.core.api.payment.infrastructure.adapters.out.mercadopago.deserializers.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.time.OffsetDateTime;

/**
 * Classe de configuração do Retrofit.
 */
@Configuration
public class MercadoPagoRetrofitConfig {

    public static Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(PaymentStatus.class, new PaymentStatusTypeAdapter())
                .registerTypeAdapter(PaymentMethod.class, new PaymentMethodTypeAdapter())
                .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeTypeAdapter())
                .registerTypeAdapter(AcquirerOrderStatus.class, new MercadoPagoOrderStatusTypeAdapter())
                .registerTypeAdapter(AcquirerOrderNotificationStatus.class, new MercadoPagoOrderNotificationStatusTypeAdapter())
                .create();
    }

    @Bean
    public MercadoPagoClient mercadoPagoClient(MercadoPagoProperties properties) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("Authorization", "Bearer " + properties.getToken())
                            .header("Content-Type", "application/json")
                            .build();
                    return chain.proceed(request);
                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(properties.getBaseUrl())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(createGson()))
                .build();

        return retrofit.create(MercadoPagoClient.class);
    }
}
