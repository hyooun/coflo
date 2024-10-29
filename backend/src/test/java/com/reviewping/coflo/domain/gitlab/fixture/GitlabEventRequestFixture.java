package com.reviewping.coflo.domain.gitlab.fixture;

import com.reviewping.coflo.domain.gitlab.dto.request.GitlabEventRequest;

public class GitlabEventRequestFixture {

    public static GitlabEventRequest createOpenActionRequest() {
        return new GitlabEventRequest(
                "merge_request",
                "merge_request",
                new GitlabEventRequest.Project(1L, "https://gitlab.example.com"),
                new GitlabEventRequest.ObjectAttributes(
                        99L,
                        1L,
                        "Sample MR",
                        "2023-10-10T10:00:00Z",
                        "2023-10-10T12:00:00Z",
                        "2023-10-10T13:00:00Z",
                        false,
                        "Description of MR",
                        "open"));
    }

    public static GitlabEventRequest createUnsupportedActionRequest() {
        return new GitlabEventRequest(
                "merge_request",
                "merge_request",
                new GitlabEventRequest.Project(1L, "https://gitlab.example.com"),
                new GitlabEventRequest.ObjectAttributes(
                        99L,
                        1L,
                        "Sample MR",
                        "2023-10-10T10:00:00Z",
                        "2023-10-10T12:00:00Z",
                        "2023-10-10T13:00:00Z",
                        false,
                        "Description of MR",
                        "unsupported_action"));
    }

    public static GitlabEventRequest createInvalidUrlRequest() {
        return new GitlabEventRequest(
                "merge_request",
                "merge_request",
                new GitlabEventRequest.Project(1L, "invalid-url"),
                new GitlabEventRequest.ObjectAttributes(
                        99L,
                        1L,
                        "Sample MR",
                        "2023-10-10T10:00:00Z",
                        "2023-10-10T12:00:00Z",
                        "2023-10-10T13:00:00Z",
                        false,
                        "Description of MR",
                        "open"));
    }
}
