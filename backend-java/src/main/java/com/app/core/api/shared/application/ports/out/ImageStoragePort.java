package com.app.core.api.shared.application.ports.out;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStoragePort {

    /**
     * Realiza o upload de uma imagem para um determinado caminho no serviço de armazenamento.
     *
     * @param path o caminho (dentro do container) onde a imagem será armazenada, por exemplo: "catalogs/1/categories/5"
     * @param file o arquivo de imagem a ser enviado
     * @return a URL pública da imagem armazenada
     * @throws RuntimeException se ocorrer um erro durante o upload
     */
    String uploadImage(String path, MultipartFile file);

    /**
     * Exclui uma imagem do serviço de armazenamento com base em sua URL pública.
     *
     * @param imageUrl a URL completa da imagem no Azure Blob Storage
     * @throws IllegalArgumentException se a URL não pertencer ao container configurado
     * @throws RuntimeException se o blob não for encontrado ou ocorrer erro durante a exclusão
     */
    void deleteImage(String imageUrl);
}
