package com.test.service_receiver.service;

import com.test.service_receiver.dto.ProductDTO;
import com.test.service_receiver.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с продуктами.
 * Взаимодействие с репозиторием.
 */
@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Добавляет новый продукт в базу данных.
     * Логирует процесс сохранения и обрабатывает возможные ошибки.
     *
     * @param productDTO Объект продукта для добавления.
     */
    public void addNewProduct(ProductDTO productDTO) {
        try{
            logger.info("Сохранение продукта: id = {}, name = {}, price = {}, timestamp = {}",
                    productDTO.getId(), productDTO.getName(), productDTO.getPrice(), productDTO.getTimestamp());

            productRepository.saveProduct(productDTO);
        } catch (Exception e) {
            logger.error("Ошибка при сохранении продукта: id = {}, name = {}, price = {}, timestamp = {}. Ошибка: {}",
                    productDTO.getId(), productDTO.getName(), productDTO.getPrice(), productDTO.getTimestamp(), e.getMessage(), e);
        }
    }
}
