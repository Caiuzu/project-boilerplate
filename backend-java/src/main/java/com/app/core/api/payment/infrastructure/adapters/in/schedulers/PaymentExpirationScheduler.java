package com.app.core.api.payment.infrastructure.adapters.in.schedulers;

import com.app.core.api.payment.application.ports.in.PaymentUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Componente responsável por agendar e executar periodicamente o processamento
 * de pagamentos não aprovados expirados.
 *
 * <p>Este agendador executa a cada 31 minutos, identificando e processando
 * os pagamentos que passaram do tempo limite permitido para serem concluídos.</p>
 *
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentExpirationScheduler {

    private final PaymentUseCase paymentUseCase;

    @Scheduled(fixedRate = 31, timeUnit = TimeUnit.MINUTES)
    public void processExpiredPayments() {
        log.info("Iniciando processamento de pagamentos não aprovados expirados.");
        paymentUseCase.processExpiredPayments();
    }
}
