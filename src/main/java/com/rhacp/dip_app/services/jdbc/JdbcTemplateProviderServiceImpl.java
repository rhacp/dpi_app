package com.rhacp.dip_app.services.jdbc;

import com.rhacp.dip_app.services.yaml.YamlWorkerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.sqlite.SQLiteDataSource;

import java.nio.file.Path;

@Slf4j
@Service
public class JdbcTemplateProviderServiceImpl implements JdbcTemplateProviderService {

    private JdbcTemplate jdbcTemplate;

    @Override
    public void init(String source) {
        Path dbPath = Path.of(source).toAbsolutePath().normalize();
        SQLiteDataSource ds = new SQLiteDataSource();
        ds.setUrl("jdbc:sqlite:" + dbPath.toString().replace("\\", "/"));
        jdbcTemplate = new JdbcTemplate(ds);
        log.info("Database initialized successful: {}. Method: updateJdbcTemplateSource", dbPath);
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public void updateJdbcTemplateSource(String current, String source) {
        if (source.equals(current)) {
            return;
        }

        Path dbPath = Path.of(source).toAbsolutePath().normalize();
        SQLiteDataSource ds = new SQLiteDataSource();
        ds.setUrl("jdbc:sqlite:" + dbPath.toString().replace("\\", "/"));
        jdbcTemplate = new JdbcTemplate(ds);
        log.info("Database path updated successfully: {}. Method: updateJdbcTemplateSource", dbPath);
    }
}
