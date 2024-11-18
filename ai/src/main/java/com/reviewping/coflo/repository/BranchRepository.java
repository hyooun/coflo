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
        String sql = "SELECT * FROM branch_info WHERE branch_id = :branchId";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("branchId", branchId);

        return namedParameterJdbcTemplate
                .query(
                        sql,
                        params,
                        (rs, rowNum) ->
                                new BranchInfo(
                                        rs.getLong("id"),
                                        rs.getString("last_commit_hash"),
                                        rs.getLong("project_id"),
                                        rs.getLong("branch_id")))
                .stream()
                .findFirst()
                .orElse(null);
    }

    public void updateLastCommitHash(Long id, String commitHash) {
        String sql = "UPDATE branch_info SET last_commit_hash = :commitHash WHERE id = :id";

        MapSqlParameterSource params =
                new MapSqlParameterSource().addValue("id", id).addValue("commitHash", commitHash);

        namedParameterJdbcTemplate.update(sql, params);
    }

    public void save(Long projectId, Long branchId, String lastCommitHash) {
        String sql =
                "INSERT INTO branch_info (project_id, branch_id, last_commit_hash) VALUES"
                        + " (:projectId, :branchId, :lastCommitHash)";

        MapSqlParameterSource params =
                new MapSqlParameterSource()
                        .addValue("projectId", projectId)
                        .addValue("branchId", branchId)
                        .addValue("lastCommitHash", lastCommitHash);

        namedParameterJdbcTemplate.update(sql, params);
    }
}
