package org.imedia.store.db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;


public class V2__add_stock_column_to_product_table extends BaseJavaMigration {

    @Override
    public void migrate(Context context) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(context.getConnection(), true));
        jdbcTemplate.execute("ALTER TABLE products ADD COLUMN stock_quantity INTEGER DEFAULT 0;");
        jdbcTemplate.update("UPDATE products SET stock_quantity = ? WHERE sku = ?", 100, "00001");
        jdbcTemplate.update("UPDATE products SET stock_quantity = ? WHERE sku = ?", 10, "00002");
        jdbcTemplate.update("UPDATE products SET stock_quantity = ? WHERE sku = ?", 0, "00003");
    }
}