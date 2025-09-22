package com.rhacp.dip_app.services.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;

public interface JdbcTemplateProviderService {

    public void init(String source);

    JdbcTemplate getJdbcTemplate();

    void updateJdbcTemplateSource(String current, String source);
}
