package com.app.core.api.payment.domain.model;

import com.app.core.api.payment.domain.exceptions.PaymentException;
import com.app.core.api.payment.domain.vo.PaymentMethod;
import com.app.core.api.payment.domain.vo.PaymentStatus;
import com.app.core.api.payment.domain.vo.QrCode;
import com.app.core.api.shared.exception.BusinessException;
import com.app.core.api.shared.vo.AuditInfo;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Entidade de domínio que representa um pagamento
 */
@Data
public class Payment {
    private Long id;
    private Long userId;
    private Long orderId;
    private PaymentMethod type;
    private LocalDateTime expiresIn = LocalDateTime
            .now()
            .plusMinutes(30);
    private String tid;
    private BigDecimal amount;
    private QrCode qrCode;
    private PaymentStatus status = PaymentStatus.PENDING;
    private LocalDateTime paidAt;
    private String observations = "Pagamento via Mercado Pago";
    private AuditInfo auditInfo = new AuditInfo();

    private static final DateTimeFormatter ISO_OFFSET_DATE_TIME_MILLIS_FIXED =
            new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
                    .appendLiteral('.')
                    .appendValue(ChronoField.MILLI_OF_SECOND, 3)
                    .appendOffsetId()
                    .toFormatter();

    /**
     * Construtor que cria uma nova instância de pagamento com os dados fornecidos.
     *
     * @param userId ID do usuário que realizou o pedido
     * @param orderId ID do pedido relacionado ao pagamento
     * @param amount Valor total do pagamento
     * 
     * @throws NullPointerException se type, expiresIn, tid ou amount forem nulos
     * 
     */
    public Payment(
            Long userId,
            Long orderId,
            BigDecimal amount) {

        validate(userId, orderId, amount);

        this.userId = userId;
        this.orderId = orderId;
        this.amount = amount;
    }

    /**
     * Define o qrCode do pagamento.
     *
     */
    public void setQrCode(String qrCode) {
        this.qrCode = new QrCode(qrCode);
    }

    /**
     * Define o status do pagamento.
     *
     */
    public void setStatus(PaymentStatus status) {
        if (status == PaymentStatus.APPROVED) {
            this.setPaidAt(LocalDateTime.now());
        }

        this.status = status;
        markUpdatedNow();
    }

    /**
     * Define o transaction id do pagamento.
     *
     * @throws PaymentException se o TID for maior que 255 caracteres {@code null}
     */
    public void setTid(String tid) {
        if ( tid != null && tid.length() > 255) {
            throw new PaymentException("O TID não pode ter mais de 255 caracteres");
        }

        this.tid = tid;
    }

    /**
     * Valida os campos obrigatórios para um pagamento.
     *
     * @throws NullPointerException se algum dos parâmetros for {@code null}
     */
    private void validate(
            Long userId,
            Long orderId,
            BigDecimal amount) {
        Objects.requireNonNull(userId, "O ID do cliente do pedido não pode ser nulo");
        Objects.requireNonNull(orderId, "O ID do pedido não pode ser nulo");
        Objects.requireNonNull(amount, "O valor total não pode ser nulo");
    }

    /**
     * Verifica se o pagamento está expirado.
     *
     * @return {@code true} se o pagamento estiver expirado, {@code false} caso contrário
     */
    public boolean isExpired() {
        return expiresIn.isBefore(LocalDateTime.now());
    }

    /**
     * Retorna o nome do método de pagamento.
     *
     * @return o nome do método de pagamento
     */
    public String getTypeName() {
        return this.type.name();
    }

    /**
     * Atualiza o campo updatedAt com o horário atual.
     *
     * @throws BusinessException se o horário atual for menor ou igual ao createdAt
     */
    public void markUpdatedNow() {
        this.auditInfo.setUpdatedAt(LocalDateTime.now());
    }


    /**
     * Retorna a data de expiração formatada como uma String no padrão ISO 8601
     * (yyyy-MM-dd'T'HH:mm:ssz) no fuso horário -04:00 (La Paz).
     *
     * @return String com a data de expiração formatada.
     */
    public String getLaPazISO8601ExpiresIn() {
        return this.expiresIn
                .atZone(ZoneId.of("America/Sao_Paulo"))
                .withZoneSameInstant(ZoneId.of("America/La_Paz"))
                .truncatedTo(ChronoUnit.MILLIS)
                .format(ISO_OFFSET_DATE_TIME_MILLIS_FIXED);
    }

}