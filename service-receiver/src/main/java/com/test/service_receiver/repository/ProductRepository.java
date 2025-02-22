package com.test.service_receiver.repository;

import com.test.service_receiver.dto.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

/**
 * Репозиторий для работы с продуктами в базе данных.
 */
@Repository
public class ProductRepository {

    private static final Logger logger = LoggerFactory.getLogger(ProductRepository.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Сохраняет продукт в базе данных.
     * Если продукт с таким ID уже существует, выбрасывает исключение.
     *
     * @param productDTO Объект продукта для сохранения.
     * @throws SQLException Если продукт с таким ID уже существует.
     */
    public void saveProduct(ProductDTO productDTO) throws SQLException {
        if (existsById(productDTO.getId())) {
            logger.error("Продукт с id = {} уже существует в базе данных", productDTO.getId());
            throw new SQLException("Продукт с таким id уже существует");
        }

        String sql = "INSERT INTO products (id, name, price, timestamp) VALUES (?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, productDTO.getId(), productDTO.getName(), productDTO.getPrice(), productDTO.getTimestamp());
            logger.info("Продукт успешно сохранён: id = {}, name = {}, price = {}, timestamp = {}",
                    productDTO.getId(), productDTO.getName(), productDTO.getPrice(), productDTO.getTimestamp());
        } catch (DataAccessException e) {
            logger.error("Ошибка при выполнении SQL-запроса на сохранение продукта: id = {}, name = {}, price = {}, timestamp = {}. Ошибка: {}",
                    productDTO.getId(), productDTO.getName(), productDTO.getPrice(), productDTO.getTimestamp(), e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Проверяет, существует ли продукт с указанным ID в базе данных.
     *
     * @param id ID продукта для проверки.
     */
    public boolean existsById(int id) {
        String sql = "SELECT COUNT(*) FROM products WHERE id = ?";
        try {
            Integer count = jdbcTemplate.queryForObject(sql, new Object[]{id}, Integer.class);
            return count != null && count > 0;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
