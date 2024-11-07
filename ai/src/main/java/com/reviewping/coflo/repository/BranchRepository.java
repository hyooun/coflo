package com.reviewping.coflo.repository;

import com.reviewping.coflo.service.dto.BranchInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BranchRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BranchInfo findByBranchId(Long branchId) {
        String sql = "SELECT * FROM branch WHERE branch_id = :branchId";

        MapSqlParameterSource params = new MapSqlParameterSource().addValue("branchId", branchId);

        return namedParameterJdbcTemplate.queryForObject(
                sql,
                params,
                (rs, rowNum) ->
                        new BranchInfo(
                                rs.getLong("id"),
                                rs.getString("lastCommitHash"),
                                rs.getLong("projectId"),
                                rs.getLong("branchId")));
    }

    public void updateLastCommitHash(Long id, String commitHash) {
        String sql = "UPDATE branch SET last_commit_hash = :commitHash WHERE id = :id";

        MapSqlParameterSource params =
                new MapSqlParameterSource().addValue("id", id).addValue("commitHash", commitHash);

        namedParameterJdbcTemplate.update(sql, params);
    }
}
