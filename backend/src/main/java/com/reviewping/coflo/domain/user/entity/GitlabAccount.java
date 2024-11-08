package com.reviewping.coflo.domain.user.entity;

import com.reviewping.coflo.domain.userproject.entity.UserProject;
import com.reviewping.coflo.global.common.entity.BaseTimeEntity;
import com.reviewping.coflo.global.converter.CryptoConverter;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GitlabAccount extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String domain;

    @Convert(converter = CryptoConverter.class)
    @Column(nullable = false, unique = true)
    private String userToken;

    @OneToMany(mappedBy = "gitlabAccount")
    private List<UserProject> userProjects = new ArrayList<>();

    private Long recentProjectId;

    @Builder
    public GitlabAccount(User user, String domain, String userToken) {
        this.user = user;
        this.domain = domain;
        this.userToken = userToken;
        user.getGitlabAccounts().add(this);
    }

    public void updateRecentProjectId(Long recentProjectId) {
        this.recentProjectId = recentProjectId;
    }
}
