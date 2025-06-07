package com.app.core.api.payment.infrastructure.adapters.out.persistence.converter;

import com.app.core.api.payment.domain.vo.QrCode;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Conversor JPA para o Value Object {@link QrCode}.
 * <p>
 * Este conversor permite que o objeto {@code QrCode} seja persistido em uma única coluna
 * do banco de dados como uma {@code String}.
 * <p>
 * Também realiza a conversão reversa da {@code String} do banco para uma instância de {@code QrCode}.
 * <p>
 * Exemplo de valor persistido: {@code "00020126360014BR.GOV.BCB.PIX0114+55119999999952040000530398654041.005802BR5913Nome do Cliente6009Sao Paulo62070503***6304ABCD"}
 *
 * <p><strong>Importante:</strong> Este conversor deve ser usado em conjunto com a anotação {@code @Convert}.
 *
 * @see QrCode
 * @see jakarta.persistence.AttributeConverter
 */
@Converter(autoApply = false)
public class QrCodeConverter implements AttributeConverter<QrCode, String> {

    /**
     * Converte o {@code QrCode} para uma {@code String} para persistência no banco.
     *
     * @param attribute o objeto {@code QrCode} a ser convertido
     * @return uma {@code String} com o conteúdo do QR code, ou {@code null} se o atributo for nulo
     */
    @Override
    public String convertToDatabaseColumn(QrCode attribute) {
        return (attribute == null) ? null : attribute.value();
    }

    /**
     * Converte a {@code String} do banco de dados de volta para um objeto {@code QrCode}.
     *
     * @param dbData a {@code String} recuperada do banco
     * @return uma instância de {@code QrCode}, ou {@code null} se a string for nula
     * @throws IllegalArgumentException se o conteúdo exceder 255 caracteres
     */
    @Override
    public QrCode convertToEntityAttribute(String dbData) {
        return (dbData == null) ? null : new QrCode(dbData);
    }
}
