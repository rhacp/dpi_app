package com.rhacp.dip_app.repositories.logitech_repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DBImpl implements DB {

    private final JdbcTemplate jdbcTemplate;

    public DBImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String getJsonString() {
        String query = "SELECT file FROM data";
        return jdbcTemplate.queryForObject(query, String.class);
    }

    @Override
    public void setJsonString(String jsonString) {
        String query = "UPDATE data SET file = ? WHERE _id = ?";
        jdbcTemplate.update(query, jsonString, "1");
    }
}
