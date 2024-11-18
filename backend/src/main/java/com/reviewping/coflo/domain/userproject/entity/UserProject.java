package com.reviewping.coflo.domain.userproject.entity;

import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProject extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gitlab_account_id")
    private GitlabAccount gitlabAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "userProject")
    private List<UserProjectScore> userProjectScores = new ArrayList<>();

    @Builder
    public UserProject(GitlabAccount gitlabAccount, Project project) {
        this.gitlabAccount = gitlabAccount;
        this.project = project;
        gitlabAccount.getUserProjects().add(this);
        project.getUserProjects().add(this);
    }
}
