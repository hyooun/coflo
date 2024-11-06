package com.reviewping.coflo.repository;

import com.reviewping.coflo.service.dto.PromptTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PromptTemplateRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PromptTemplate findLatestTemplate(String type) {
        String sql =
                "SELECT content, type, created_date FROM prompt_template WHERE type = :type ORDER"
                        + " BY created_date DESC LIMIT 1";

        MapSqlParameterSource params = new MapSqlParameterSource().addValue("type", type);

        return namedParameterJdbcTemplate.queryForObject(
                sql,
                params,
                (rs, rowNum) ->
                        new PromptTemplate(
                                rs.getString("content"),
                                rs.getString("type"),
                                rs.getTimestamp("created_date").toLocalDateTime()));
    }
}
