package com.reviewping.coflo.domain.project.service;

import com.reviewping.coflo.domain.project.entity.Branch;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.message.UpdateRequestMessage;
import com.reviewping.coflo.domain.project.repository.BranchRepository;
import com.reviewping.coflo.domain.userproject.controller.dto.request.ProjectLinkRequest;
import com.reviewping.coflo.global.integration.RedisGateway;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final RedisGateway redisGateway;

    public void addBranches(ProjectLinkRequest projectLinkRequest, Project project) {
        if (projectLinkRequest.branches() == null) {
            return;
        }
        List<Branch> branches = saveBranches(projectLinkRequest, project);
        setupProjectIntegration(project, branches);
    }

    private List<Branch> saveBranches(ProjectLinkRequest projectLinkRequest, Project project) {
        List<Branch> branches =
                projectLinkRequest.branches().stream()
                        .map(branchName -> new Branch(project, branchName))
                        .toList();
        return branchRepository.saveAll(branches);
    }

    private void setupProjectIntegration(Project project, List<Branch> branches) {
        branches.forEach(
                branch -> {
                    UpdateRequestMessage initRequest =
                            new UpdateRequestMessage(
                                    project.getId(),
                                    branch.getId(),
                                    project.getGitUrl(),
                                    branch.getName(),
                                    project.getBotToken(),
                                    "");
                    redisGateway.sendUpdateRequest(initRequest);
                });
    }
}
