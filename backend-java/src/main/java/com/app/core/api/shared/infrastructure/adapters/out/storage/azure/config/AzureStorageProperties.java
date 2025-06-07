package com.app.core.api.shared.infrastructure.adapters.out.storage.azure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuração que representa as propriedades necessárias
 * para conectar ao Azure Blob Storage.
 *
 * As propriedades são carregadas a partir do arquivo `application.yaml`
 * com o prefixo {@code azure.storage}.
 *
 * Exemplo de configuração:
 * <pre>
 * azure:
 *   storage:
 *     connection-string: "DefaultEndpointsProtocol=...;AccountName=...;AccountKey=..."
 *     container-name: "nome-do-container"
 * </pre>
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "azure.storage")
public class AzureStorageProperties {

    /**
     * String de conexão completa com a conta do Azure Blob Storage.
     * Essa string pode ser copiada diretamente do portal do Azure.
     */
    private String connectionString;

    /**
     * Nome do container de blobs onde os arquivos serão armazenados.
     * O container deve existir ou será criado automaticamente, caso não exista.
     */
    private String containerName;
}

