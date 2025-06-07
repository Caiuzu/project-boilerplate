package com.app.core.api.payment.application.dto.response;


import com.app.core.api.payment.domain.vo.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Representa a resposta da API de consulta de um pagamento no adquirente.
 */
@Data
public class AcquirerPaymentsResponse {

    private Long id;
    private PaymentStatus status;
    private String status_detail;
    private String description;
    private BigDecimal transaction_amount;
    private BigDecimal transaction_amount_refunded;
    private BigDecimal shipping_amount;
    private BigDecimal coupon_amount;
    private String currency_id;
    private Integer installments;
    private Boolean captured;
    private Boolean binary_mode;
    private Boolean live_mode;
    private String operation_type;
    private String notification_url;
    private String external_reference;
    private Date date_approved;
    private Date date_created;
    private Date date_last_updated;
    private Date money_release_date;
    private String money_release_status;
    private Long collector_id;
    private String issuer_id;
    private String payment_method_id;
    private String payment_type_id;
    private String processing_mode;
    private String merchant_account_id;
    private String pos_id;
    private String store_id;

    private AdditionalInfo additional_info;
    private Order order;
    private Payer payer;
    private PaymentMethod payment_method;
    private PointOfInteraction point_of_interaction;
    private ChargesExecutionInfo charges_execution_info;
    private TransactionDetails transaction_details;

    private Map<String, Object> metadata;
    private List<Object> acquirer_reconciliation;
    private List<Object> charges_details;
    private List<Object> fee_details;
    private List<Object> refunds;

    public com.app.core.api.payment.domain.vo.PaymentMethod getPaymentType() {
        return this.payment_method.getType();
    }


    @Data
    public static class AdditionalInfo {
        private String tracking_id;
    }

    @Data
    public static class Order {
        private String id;
        private String type;
    }

    @Data
    public static class Payer {
        private String id;
    }

    @Data
    public static class PaymentMethod {
        private String id;
        private String issuer_id;
        private com.app.core.api.payment.domain.vo.PaymentMethod type;
    }

    @Data
    public static class ChargesExecutionInfo {
        private InternalExecution internal_execution;

        @Data
        public static class InternalExecution {
            private Date date;
            private String execution_id;
        }
    }

    @Data
    public static class PointOfInteraction {
        private ApplicationData application_data;
        private BusinessInfo business_info;
        private ForwardData forward_data;
        private Location location;
        private TransactionData transaction_data;
        private String type;
        private String sub_type;

        @Data
        public static class ApplicationData {
            private String name;
            private String operating_system;
            private String version;
        }

        @Data
        public static class BusinessInfo {
            private String unit;
            private String branch;
            private String sub_unit;
        }

        @Data
        public static class ForwardData {
            private String destination_user_id;
            private String input_type;
            private String marketplace;
            private String original_payment_id;
            private TaxExemption tax_exemption;
            private String user_role;

            @Data
            public static class TaxExemption {
                private Boolean eligible;
                private String product;
                private String reason;
            }
        }

        @Data
        public static class Location {
            private String source;
            private String state_id;
        }

        @Data
        public static class TransactionData {
            private BankInfo bank_info;
            private String bank_transfer_id;
            private String e2e_id;
            private String financial_institution;
            private InfringementNotification infringement_notification;
            private String qr_code;
            private String ticket_url;
            private String transaction_id;

            @Data
            public static class BankInfo {
                private BankAccount collector;
                private BankAccount payer;
                private Boolean is_same_bank_account_owner;
                private String origin_bank_id;
                private String origin_wallet_id;

                @Data
                public static class BankAccount {
                    private String account_id;
                    private String account_holder_name;
                    private String long_name;
                    private String transfer_account_id;
                    private String branch;
                    private String external_account_id;
                    private String id;
                    private Identification identification;
                    private Boolean is_end_consumer;

                    @Data
                    public static class Identification {
                        private Map<String, Object> data; // pode conter campos diversos
                    }
                }
            }

            @Data
            public static class InfringementNotification {
                private String status;
                private String type;
            }
        }
    }

    @Data
    public static class TransactionDetails {
        private String acquirer_reference;
        private String bank_transfer_id;
        private String external_resource_url;
        private String financial_institution;
        private BigDecimal installment_amount;
        private BigDecimal net_received_amount;
        private BigDecimal overpaid_amount;
        private BigDecimal total_paid_amount;
        private String payment_method_reference_id;
        private String payable_deferral_period;
        private String transaction_id;
    }
}

