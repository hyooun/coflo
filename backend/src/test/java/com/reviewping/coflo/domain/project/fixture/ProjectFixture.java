package com.reviewping.coflo.domain.project.fixture;

import com.reviewping.coflo.domain.project.entity.Project;

public class ProjectFixture {
    public static Project createMockProject() {
        return Project.builder().botToken("test-token").build();
    }
}
