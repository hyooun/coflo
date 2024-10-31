package com.reviewping.coflo.domain.userproject.repository;

import static com.reviewping.coflo.domain.mergerequest.entity.QMrInfo.*;
import static com.reviewping.coflo.domain.project.entity.QProject.project;
import static com.reviewping.coflo.domain.userproject.entity.QUserProject.userProject;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reviewping.coflo.domain.userproject.entity.UserProject;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserProjectRepositoryCustomImpl implements UserProjectRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserProject> getUserProjectsOrderByModifiedDateDesc(Long gitlabAccountId) {

        JPAQuery<LocalDateTime> subQuery =
                queryFactory
                        .select(mrInfo.modifiedDate.max())
                        .from(mrInfo)
                        .where(mrInfo.project.id.eq(project.id));

        return queryFactory
                .selectFrom(userProject)
                .join(userProject.project, project)
                .fetchJoin()
                .where(userProject.gitlabAccount.id.eq(gitlabAccountId))
                .orderBy(Expressions.asDateTime(subQuery).desc())
                .fetch();
    }
}
