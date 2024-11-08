package com.reviewping.coflo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.reviewping.coflo.git.GitUtil;
import com.reviewping.coflo.json.JsonUtil;
import com.reviewping.coflo.repository.BranchRepository;
import com.reviewping.coflo.repository.ChunkedCodeRepository;
import com.reviewping.coflo.service.dto.BranchInfo;
import com.reviewping.coflo.service.dto.GitFileInfo;
import com.reviewping.coflo.service.dto.request.UpdateRequestMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.diff.DiffEntry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ProjectUpdateService {

    private final String gitCloneDirectory;
    private final JsonUtil jsonUtil;
    private final GitUtil gitUtil;
    private final ProjectHelper projectHelper;
    private final BranchRepository branchRepository;
    private final ChunkedCodeRepository chunkedCodeRepository;

    public ProjectUpdateService(
            @Value("${git-clone-directory}") String gitCloneDirectory,
            JsonUtil jsonUtil,
            GitUtil gitUtil,
            ProjectHelper projectHelper,
            BranchRepository branchRepository,
            ChunkedCodeRepository chunkedCodeRepository) {
        this.gitCloneDirectory = gitCloneDirectory;
        this.jsonUtil = jsonUtil;
        this.gitUtil = gitUtil;
        this.projectHelper = projectHelper;
        this.branchRepository = branchRepository;
        this.chunkedCodeRepository = chunkedCodeRepository;
    }

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
        // 1. 최신 commit_hash와 last_commit_hash 비교 (없으면 분기해서 init)
        BranchInfo branchInfo = branchRepository.findByBranchId(updateRequest.branchId());
        if (branchInfo == null) {
            initializeKnowledgeBase(
                    updateRequest.projectId(),
                    updateRequest.branchId(),
                    updateRequest.gitUrl(),
                    updateRequest.branch(),
                    updateRequest.token());
            return;
        }
        String localPath =
                gitCloneDirectory + updateRequest.projectId() + "/" + updateRequest.branch();
        String newCommitHash =
                gitUtil.shallowCloneOrPull(
                        updateRequest.gitUrl(),
                        updateRequest.branch(),
                        updateRequest.token(),
                        Path.of(localPath));
        if (newCommitHash.equals(branchInfo.lastCommitHash())) {
            log.info(
                    "리포지토리가 최신입니다. - Project ID: {}, Branch ID: {}, Git URL: {}, Branch: {}, Commit"
                            + " Hash: {}",
                    updateRequest.projectId(),
                    updateRequest.branchId(),
                    updateRequest.gitUrl(),
                    updateRequest.branch(),
                    newCommitHash);
            return;
        }
        log.info(
                "지식베이스 업데이트 시작 - Project ID: {}, Branch ID: {}, Git URL: {}, Branch: {}, Old Commit"
                        + " Hash: {}, New Commit Hash: {}",
                updateRequest.projectId(),
                updateRequest.branchId(),
                updateRequest.gitUrl(),
                updateRequest.branch(),
                branchInfo.lastCommitHash(),
                newCommitHash);
        // 2. git diff --name-only로 변경파일들(수정, 삭제, 생성)
        List<GitFileInfo> fileInfos =
                gitUtil.getUpdatedFileInfos(localPath, branchInfo.lastCommitHash(), newCommitHash);
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
        projectHelper.preprocessAndSave(branchInfo.id(), pathStream);
        // 3. last_commit_hash 업데이트
        branchRepository.updateLastCommitHash(branchInfo.id(), newCommitHash);
        log.info(
                "지식베이스 업데이트 완료 - Project ID: {}, Branch ID: {}, Git URL: {}, Branch: {}, Old Commit"
                        + " Hash: {}, New Commit Hash: {}",
                updateRequest.projectId(),
                updateRequest.branchId(),
                updateRequest.gitUrl(),
                updateRequest.branch(),
                branchInfo.lastCommitHash(),
                newCommitHash);
    }

    private void initializeKnowledgeBase(
            Long projectId, Long branchId, String gitUrl, String branch, String token) {
        log.info(
                "지식베이스 초기화 시작 - Project ID: {}, Branch ID: {}, Git URL: {}, Branch: {}",
                projectId,
                branchId,
                gitUrl,
                branch);
        // 1. git clone
        Path localPath = Path.of(gitCloneDirectory + projectId + "/" + branch);
        String lastCommitHash = gitUtil.shallowCloneOrPull(gitUrl, branch, token, localPath);
        // 2. 전처리: 메소드 단위 청킹 + 메타데이터 추가
        // 3. 벡터DB에 저장
        branchRepository.save(projectId, branchId, lastCommitHash);
        BranchInfo branchInfo = branchRepository.findByBranchId(branchId);
        try (Stream<Path> filePathStream = Files.walk(localPath)) {
            projectHelper.preprocessAndSave(branchInfo.id(), filePathStream);
        } catch (IOException e) {
            throw new PreprocessException("파일 전처리 중 오류가 발생했습니다.", e);
        }

        log.info(
                "지식베이스 초기화 완료 - Project ID: {}, Branch ID: {}, Git URL: {}, Branch: {}, Last Commit"
                        + " Hash: {}",
                projectId,
                branchId,
                gitUrl,
                branch,
                lastCommitHash);
    }
}
