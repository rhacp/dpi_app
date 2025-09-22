package com.rhacp.dip_app.repositories.logitech_repository;

import com.rhacp.dip_app.services.jdbc.JdbcTemplateProviderService;
import org.springframework.stereotype.Repository;

@Repository
public class DBImpl implements DB {

    private final JdbcTemplateProviderService jdbcTemplateProviderService;

    public DBImpl(JdbcTemplateProviderService jdbcTemplateProviderService) {
        this.jdbcTemplateProviderService = jdbcTemplateProviderService;
    }

    @Override
    public String getJsonString() {
        String query = "SELECT file FROM data";
        return jdbcTemplateProviderService.getJdbcTemplate().queryForObject(query, String.class);
    }

    @Override
    public void setJsonString(String jsonString) {
        String query = "UPDATE data SET file = ? WHERE _id = ?";
        jdbcTemplateProviderService.getJdbcTemplate().update(query, jsonString, "1");
    }
}
