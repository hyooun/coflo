package com.reviewping.coflo.domain.userproject.service;

import com.reviewping.coflo.domain.mergerequest.entity.MrInfo;
import com.reviewping.coflo.domain.project.entity.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserProjectScoreService {

    @Transactional
    public void saveUserProjectScores(String username, MrInfo mrInfo) {
        Project project = mrInfo.getProject();
    }
}
