package com.reviewping.coflo.domain.project.service;

import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;

    private Project saveProject(Long gitlabProjectId, String botToken, String projectName) {

        Project project =
                Project.builder()
                        .gitlabProjectId(gitlabProjectId)
                        .botToken(botToken)
                        .name(projectName)
                        .build();
        return projectRepository.save(project);
    }
}
