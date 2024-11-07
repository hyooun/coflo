package com.reviewping.coflo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.reviewping.coflo.git.GitUtil;
import com.reviewping.coflo.json.JsonUtil;
import com.reviewping.coflo.repository.BranchRepository;
import com.reviewping.coflo.repository.ChunkedCodeRepository;
import com.reviewping.coflo.service.dto.BranchInfo;
import com.reviewping.coflo.service.dto.GitFileInfo;
import com.reviewping.coflo.service.dto.request.InitRequestMessage;
import com.reviewping.coflo.service.dto.request.UpdateRequestMessage;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.diff.DiffEntry;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectUpdateService {

    private final JsonUtil jsonUtil;
    private final ProjectInitializeService projectInitializeService;
    private final GitUtil gitUtil;
    private final ProjectHelper projectHelper;
    private final BranchRepository branchRepository;
    private final ChunkedCodeRepository chunkedCodeRepository;

    /*
    1. 최신 commit_hash와 last_commit_hash 비교 (없으면 분기해서 init)
     2. git diff --name-only로 변경파일들(수정, 삭제, 생성)
        수정 -> db삭제 -> 다시 청킹 저장
        삭제 -> db삭제
        생성 -> 청킹 저장
    3. last_commit_hash 업데이트
     */
    @ServiceActivator(inputChannel = "updateChannel")
    @Transactional
    public void updateKnowledgeBase(String updateRequestMessage) {
        UpdateRequestMessage updateRequest =
                jsonUtil.fromJson(updateRequestMessage, new TypeReference<>() {});
        log.info(
                "지식베이스 업데이트 시작 - Project ID: {}, Branch ID: {}, Git URL: {}, Branch: {}, Commit"
                        + " Hash: {}",
                updateRequest.projectId(),
                updateRequest.branchId(),
                updateRequest.gitUrl(),
                updateRequest.branch(),
                updateRequest.commitHash());

        //        1. 최신 commit_hash와 last_commit_hash 비교 (없으면 분기해서 init)
        BranchInfo branchInfo = branchRepository.findByBranchId(updateRequest.branchId());
        if (branchInfo == null) {
            InitRequestMessage initRequestMessage =
                    new InitRequestMessage(
                            updateRequest.projectId(),
                            updateRequest.branchId(),
                            updateRequest.gitUrl(),
                            updateRequest.branch(),
                            updateRequest.token());
            projectInitializeService.initializeKnowledgeBase(jsonUtil.toJson(initRequestMessage));
            return;
        }
        if (updateRequest.commitHash().equals(branchInfo.lastCommitHash())) {
            return;
        }

        // 2. git diff --name-only로 변경파일들(수정, 삭제, 생성)
        List<GitFileInfo> fileInfos =
                gitUtil.getUpdatedFileInfos(
                        "", branchInfo.lastCommitHash(), updateRequest.commitHash());
        // 수정, 삭제된 파일 정보 삭제
        fileInfos.stream()
                .filter(gitFileInfo -> gitFileInfo.changeType() != DiffEntry.ChangeType.ADD)
                .forEach(
                        gitFileInfo ->
                                chunkedCodeRepository.removeAllByFilePath(gitFileInfo.filePath()));
        // 수정, 생성된 파일 정보 저장
        Stream<Path> pathStream =
                fileInfos.stream()
                        .filter(
                                gitFileInfo ->
                                        gitFileInfo.changeType() != DiffEntry.ChangeType.DELETE)
                        .map(gitFileInfo -> Paths.get(gitFileInfo.filePath()));
        projectHelper.preprocessAndSave(
                updateRequest.projectId(), updateRequest.branchId(), pathStream);
        // 3. last_commit_hash 업데이트
        branchRepository.updateLastCommitHash(branchInfo.id(), updateRequest.commitHash());
        log.info(
                "지식베이스 업데이트 완료 - Project ID: {}, Branch ID: {}, Git URL: {}, Branch: {}, Commit"
                        + " Hash: {}",
                updateRequest.projectId(),
                updateRequest.branchId(),
                updateRequest.gitUrl(),
                updateRequest.branch(),
                updateRequest.commitHash());
    }
}
