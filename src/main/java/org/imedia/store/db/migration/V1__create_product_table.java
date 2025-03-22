package org.imedia.store.db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class V1__create_product_table extends BaseJavaMigration {

    @Override
    public void migrate(Context context) {
        new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true))
                .execute("CREATE TABLE products (" +
                        "sku VARCHAR(50) PRIMARY KEY, " +
                        "name VARCHAR(255) NOT NULL, " +
                        "description TEXT, " +
                        "price DECIMAL(10, 2) NOT NULL" +
                        ")");

        // Insert some initial data for testing
        JdbcTemplate jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true));

        jdbcTemplate.update(
                "INSERT INTO products (sku, name, description, price) VALUES (?, ?, ?, ?)",
                "00001", "Car Wipers", "Premium long lasting high quality wipers", 19.99
        );

        jdbcTemplate.update(
                "INSERT INTO products (sku, name, description, price) VALUES (?, ?, ?, ?)",
                "00002", "LEDS", "Day by Night lights", 89.99
        );

        jdbcTemplate.update(
                "INSERT INTO products (sku, name, description, price) VALUES (?, ?, ?, ?)",
                "00003", "Baby car seats", "Safety and confort for your child", 129.99
        );
    }
}