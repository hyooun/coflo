package com.reviewping.coflo.repository;

import com.pgvector.PGvector;
import com.reviewping.coflo.service.dto.ChunkedCode;
import java.sql.Array;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChunkedCodeRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private MapSqlParameterSource createParameterSource(Long branchInfoId, ChunkedCode chunkedCode)
            throws SQLException {
        PGvector embeddingVector = new PGvector(chunkedCode.getEmbedding());
        return new MapSqlParameterSource()
                .addValue("branchInfoId", branchInfoId)
                .addValue("language", chunkedCode.getLanguage())
                .addValue("content", chunkedCode.getContent())
                .addValue("fileName", chunkedCode.getFileName())
                .addValue("filePath", chunkedCode.getFilePath())
                .addValue("embedding", embeddingVector);
    }

    public void saveAllChunkedCodes(Long branchInfoId, List<ChunkedCode> chunkedCodes) {
        String sql =
                "INSERT INTO chunked_code (branch_info_id, language, content, file_name,"
                        + " file_path, embedding) VALUES (:branchInfoId, :language, :content,"
                        + " :fileName, :filePath, :embedding)";
        MapSqlParameterSource[] batchValues = new MapSqlParameterSource[chunkedCodes.size()];

        try {
            for (int i = 0; i < chunkedCodes.size(); i++) {
                batchValues[i] = createParameterSource(branchInfoId, chunkedCodes.get(i));
            }
            int[] ints = namedParameterJdbcTemplate.batchUpdate(sql, batchValues);
        } catch (SQLException e) {
            throw new PersistenceException("Failed to batch save chunked codes with embeddings", e);
        }
    }

    public List<ChunkedCode> retrieveRelevantData(
            Long projectId, Long branchId, int count, float[] queryEmbedding) {
        String sql =
                "SELECT cc.content, cc.file_name, cc.file_path, cc.embedding, cc.language "
                        + "FROM chunked_code cc "
                        + "INNER JOIN branch_info bi ON cc.branch_info_id = bi.id "
                        + "WHERE bi.project_id = :projectId AND bi.branch_id = :branchId "
                        + "ORDER BY cc.embedding <=> :embedding::vector "
                        + "LIMIT :limit";
        PGvector embeddingVector = new PGvector(queryEmbedding);

        MapSqlParameterSource params =
                new MapSqlParameterSource()
                        .addValue("projectId", projectId)
                        .addValue("branchId", branchId)
                        .addValue("embedding", embeddingVector)
                        .addValue("limit", count);

        return namedParameterJdbcTemplate.query(
                sql,
                params,
                (rs, rowNum) -> {
                    ChunkedCode chunkedCode =
                            new ChunkedCode(
                                    rs.getString("content"),
                                    rs.getString("file_name"),
                                    rs.getString("file_path"),
                                    rs.getString("language"));
                    Array sqlArray = rs.getArray("embedding");
                    if (sqlArray != null) {
                        chunkedCode.addEmbedding(new PGvector(String.valueOf(sqlArray)).toArray());
                    }
                    return chunkedCode;
                });
    }

    public void removeAllByFilePath(String filePath) {
        String sql = "DELETE FROM chunked_code WHERE file_path = :filePath";

        MapSqlParameterSource params = new MapSqlParameterSource().addValue("filePath", filePath);

        namedParameterJdbcTemplate.update(sql, params);
    }
}
