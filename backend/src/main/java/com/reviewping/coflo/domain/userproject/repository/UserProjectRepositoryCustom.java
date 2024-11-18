package com.reviewping.coflo.domain.userproject.repository;

import com.reviewping.coflo.domain.userproject.entity.UserProject;
import java.util.List;

public interface UserProjectRepositoryCustom {

    List<UserProject> getUserProjectsOrderByModifiedDateDesc(Long gitlabAccountId);
}
