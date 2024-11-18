package com.reviewping.coflo.domain.mergerequest.repository;

import com.reviewping.coflo.domain.mergerequest.entity.BestMrHistory;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BestMrHistoryRepository extends CrudRepository<BestMrHistory, Long> {
    @Query("SELECT b.userId FROM BestMrHistory b GROUP BY b.userId HAVING COUNT(b) >= :count")
    List<Long> findUsersWithAtLeastNHistory(@Param("count") long count);
}
