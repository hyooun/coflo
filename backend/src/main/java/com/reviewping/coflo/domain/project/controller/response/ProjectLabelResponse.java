package com.reviewping.coflo.domain.project.controller.response;

import com.reviewping.coflo.global.client.gitlab.response.GitlabLabelColorContent;
import java.util.List;

public record ProjectLabelResponse(List<LabelInfo> labels) {
    public static ProjectLabelResponse of(List<GitlabLabelColorContent> labels) {
        return new ProjectLabelResponse(
                labels.stream()
                        .map(label -> new LabelInfo(label.name(), label.textColor(), label.color()))
                        .toList());
    }

    private record LabelInfo(String name, String textColor, String bgColor) {}
}
