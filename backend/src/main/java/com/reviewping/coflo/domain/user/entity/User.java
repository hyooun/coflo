package com.reviewping.coflo.domain.user.entity;

import com.reviewping.coflo.domain.user.enums.Provider;
import com.reviewping.coflo.domain.user.enums.Role;
import com.reviewping.coflo.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "\"user\"")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String profileImageUrl;

    @Column(nullable = false, unique = true)
    private String oauth2Id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<GitlabAccount> gitlabAccounts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserBadge> userBadges = new ArrayList<>();

    @Builder
    public User(
            String username,
            String profileImageUrl,
            String oauth2Id,
            Provider provider,
            Role role) {
        this.username = username;
        this.profileImageUrl = profileImageUrl;
        this.oauth2Id = oauth2Id;
        this.provider = provider;
        this.role = role;
    }

    public void updateUserInfo(String username, String profileImageUrl) {
        this.username = username;
        this.profileImageUrl = profileImageUrl;
    }
}
