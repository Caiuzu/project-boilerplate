package com.app.core.api.payment.application.dto.response;

import com.app.core.api.payment.domain.vo.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * Representa a resposta da API de consulta de um pedido no adquirente.
 */
@Data
@Schema(name = "AcquirerOrderResponse", description = "Resposta do adquirente para um pedido")
public class AcquirerOrderResponse {

    @Schema(description = "ID do pedido", example = "123456789")
    private long id;

    @Schema(description = "Status do pedido", example = "opened")
    private AcquirerOrderNotificationStatus status;

    @Schema(description = "Referência externa", example = "REF1234")
    private String external_reference;

    @Schema(description = "ID da preferência", example = "pref_abc123")
    private String preference_id;

    @Schema(description = "Lista de pagamentos relacionados ao pedido")
    private List<Payment> payments;

    @Schema(description = "Lista de remessas relacionadas ao pedido")
    private List<Shipment> shipments;

    @Schema(description = "Informações de repasses (payouts)")
    private Map<String, Object> payouts;

    @Schema(description = "Dados do coletor")
    private Collector collector;

    @Schema(description = "Marketplace", example = "MP")
    private String marketplace;

    @Schema(description = "Data de criação do pedido", example = "2024-05-30T10:15:30+00:00")
    private OffsetDateTime date_created;

    @Schema(description = "Data da última atualização do pedido", example = "2024-05-30T12:00:00+00:00")
    private OffsetDateTime last_updated;

    @Schema(description = "Custo do frete", example = "15.0")
    private double shipping_cost;

    @Schema(description = "Valor total do pedido", example = "150.5")
    private double total_amount;

    @Schema(description = "ID do site", example = "MLB")
    private String site_id;

    @Schema(description = "Valor pago", example = "150.5")
    private double paid_amount;

    @Schema(description = "Valor reembolsado", example = "0.0")
    private double refunded_amount;

    @Schema(description = "Informações do pagador")
    private Payer payer;

    @Schema(description = "Lista de itens do pedido")
    private List<Item> items;

    @Schema(description = "Indica se o pedido foi cancelado", example = "false")
    private boolean cancelled;

    @Schema(description = "Informações adicionais do pedido", example = "Entrega prevista para amanhã")
    private String additional_info;

    @Schema(description = "ID da aplicação que gerou o pedido", example = "app_1234")
    private String application_id;

    @Schema(description = "Status do pedido no sistema", example = "paid")
    private AcquirerOrderStatus order_status;

    @Data
    @Schema(name = "Payment", description = "Detalhes de um pagamento")
    public static class Payment {
        @Schema(description = "ID do pagamento", example = "987654321")
        private long id;

        @Schema(description = "Valor da transação", example = "150.0")
        private double transaction_amount;

        @Schema(description = "Valor total pago", example = "150.0")
        private double total_paid_amount;

        @Schema(description = "Custo do frete no pagamento", example = "15.0")
        private double shipping_cost;

        @Schema(description = "Código da moeda", example = "BRL")
        private String currency_id;

        @Schema(description = "Status do pagamento", example = "approved")
        private PaymentStatus status;

        @Schema(description = "Detalhes do status", example = "accredited")
        private String status_detail;

        @Schema(description = "Data de aprovação do pagamento", example = "2024-05-30T11:00:00+00:00")
        private OffsetDateTime date_approved;

        @Schema(description = "Data de criação do pagamento", example = "2024-05-30T10:30:00+00:00")
        private OffsetDateTime date_created;

        @Schema(description = "Data da última modificação", example = "2024-05-30T11:30:00+00:00")
        private OffsetDateTime last_modified;

        @Schema(description = "Valor reembolsado no pagamento", example = "0.0")
        private double amount_refunded;
    }

    @Data
    @Schema(name = "Shipment", description = "Detalhes da remessa")
    public static class Shipment {
        @Schema(description = "ID da remessa", example = "12345")
        private long id;

        @Schema(description = "Tipo de remessa", example = "shipping")
        private String shipment_type;

        @Schema(description = "Modo de envio", example = "custom")
        private String shipping_mode;

        @Schema(description = "Status da remessa", example = "ready_to_ship")
        private String status;

        @Schema(description = "Itens da remessa")
        private List<ShipmentItem> items;

        @Schema(description = "Data de criação da remessa", example = "2024-05-29T10:00:00+00:00")
        private OffsetDateTime date_created;

        @Schema(description = "Data da última modificação da remessa", example = "2024-05-29T12:00:00+00:00")
        private OffsetDateTime last_modified;

        @Schema(description = "Data da primeira impressão da remessa", example = "2024-05-29T11:00:00+00:00")
        private OffsetDateTime date_first_printed;

        @Schema(description = "ID do serviço de envio", example = "1")
        private int service_id;

        @Schema(description = "ID do remetente", example = "100")
        private long sender_id;

        @Schema(description = "ID do destinatário", example = "200")
        private long receiver_id;

        @Schema(description = "Endereço do destinatário")
        private ReceiverAddress receiver_address;

        @Schema(description = "Opções de envio")
        private ShippingOption shipping_option;

        @Data
        @Schema(name = "ShipmentItem", description = "Item dentro da remessa")
        public static class ShipmentItem {
            @Schema(description = "ID do item", example = "item_001")
            private String id;

            @Schema(description = "Descrição do item", example = "Camisa Polo")
            private String description;

            @Schema(description = "Quantidade", example = "2")
            private int quantity;

            @Schema(description = "Dimensões do item", example = "10x20x5 cm")
            private String dimensions;
        }

        @Data
        @Schema(name = "ReceiverAddress", description = "Endereço do destinatário")
        public static class ReceiverAddress {
            @Schema(description = "ID do endereço", example = "555")
            private long id;

            @Schema(description = "Linha de endereço", example = "Rua das Flores, 123")
            private String address_line;

            @Schema(description = "Cidade")
            private City city;

            @Schema(description = "Estado")
            private State state;

            @Schema(description = "País")
            private Country country;

            @Schema(description = "Latitude do endereço", example = "-23.550520")
            private double latitude;

            @Schema(description = "Longitude do endereço", example = "-46.633308")
            private double longitude;

            @Schema(description = "Comentários adicionais", example = "Próximo ao supermercado")
            private String comment;

            @Schema(description = "Contato", example = "Maria")
            private String contact;

            @Schema(description = "Telefone", example = "11987654321")
            private long phone;

            @Schema(description = "CEP", example = "01234-567")
            private int zip_code;

            @Schema(description = "Nome da rua")
            private StreetName street_name;

            @Schema(description = "Número da rua", example = "123")
            private int street_number;

            @Data
            @Schema(name = "City", description = "Cidade do endereço")
            public static class City {
                @Schema(description = "Nome da cidade", example = "São Paulo")
                private String name;
            }

            @Data
            @Schema(name = "State", description = "Estado do endereço")
            public static class State {
                @Schema(description = "ID do estado", example = "SP")
                private String id;

                @Schema(description = "Nome do estado", example = "São Paulo")
                private String name;
            }

            @Data
            @Schema(name = "Country", description = "País do endereço")
            public static class Country {
                @Schema(description = "ID do país", example = "BR")
                private String id;

                @Schema(description = "Nome do país", example = "Brasil")
                private String name;
            }

            @Data
            @Schema(name = "StreetName", description = "Nome da rua em diferentes idiomas")
            public static class StreetName {
                @Schema(description = "Nome em inglês", example = "Flowers Street")
                private String en;

                @Schema(description = "Nome em português", example = "Rua das Flores")
                private String pt;

                @Schema(description = "Nome em espanhol", example = "Calle de las Flores")
                private String es;
            }
        }

        @Data
        @Schema(name = "ShippingOption", description = "Opções de envio da remessa")
        public static class ShippingOption {
            @Schema(description = "ID da opção de envio", example = "1")
            private long id;

            @Schema(description = "Custo do frete", example = "10.0")
            private double cost;

            @Schema(description = "Código da moeda", example = "BRL")
            private String currency_id;

            @Schema(description = "ID do método de envio", example = "101")
            private long shipping_method_id;

            @Schema(description = "Estimativa de entrega")
            private EstimatedDelivery estimated_delivery;

            @Schema(description = "Nome da opção de envio", example = "Entrega rápida")
            private String name;

            @Schema(description = "Custo da lista", example = "15.0")
            private double list_cost;

            @Schema(description = "Velocidade de entrega")
            private Speed speed;

            @Data
            @Schema(name = "EstimatedDelivery", description = "Data estimada para entrega")
            public static class EstimatedDelivery {
                @Schema(description = "Data estimada", example = "2024-06-01T15:00:00+00:00")
                private OffsetDateTime date;
            }

            @Data
            @Schema(name = "Speed", description = "Tempo de manuseio e envio")
            public static class Speed {
                @Schema(description = "Tempo de manuseio (horas)", example = "24")
                private int handling;

                @Schema(description = "Tempo de envio (horas)", example = "48")
                private int shipping;
            }
        }
    }

    @Data
    @Schema(name = "Collector", description = "Informações do coletor do pedido")
    public static class Collector {
        @Schema(description = "ID do coletor", example = "1001")
        private long id;

        @Schema(description = "Email do coletor", example = "collector@example.com")
        private String email;

        @Schema(description = "Apelido do coletor", example = "Collector123")
        private String nickname;
    }

    @Data
    @Schema(name = "Payer", description = "Informações do pagador")
    public static class Payer {
        @Schema(description = "ID do pagador", example = "5001")
        private long id;
    }

    @Data
    @Schema(name = "Item", description = "Detalhes do item no pedido")
    public static class Item {
        @Schema(description = "ID do item", example = "item123")
        private String id;

        @Schema(description = "ID da categoria do item", example = "cat001")
        private String category_id;

        @Schema(description = "Código da moeda", example = "BRL")
        private String currency_id;

        @Schema(description = "Descrição do item", example = "Camisa Polo azul")
        private String description;

        @Schema(description = "URL da imagem do item", example = "http://example.com/image.jpg")
        private String picture_url;

        @Schema(description = "Título do item", example = "Camisa Polo")
        private String title;

        @Schema(description = "Quantidade do item", example = "2")
        private int quantity;

        @Schema(description = "Preço unitário do item", example = "50.0")
        private double unit_price;
    }
}