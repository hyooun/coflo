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

    public PromptTemplate findLatestTemplate() {
        String sql = "SELECT * FROM prompt_template ORDER BY created_date DESC LIMIT 1";

        return namedParameterJdbcTemplate.queryForObject(
                sql,
                new MapSqlParameterSource(),
                (rs, rowNum) ->
                        new PromptTemplate(
                                rs.getLong("id"),
                                rs.getString("content"),
                                rs.getTimestamp("created_date").toLocalDateTime()));
    }
}
