package com.reviewping.coflo.git;

import com.reviewping.coflo.service.dto.GitFileInfo;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.springframework.stereotype.Component;

@Component
public class GitUtil {

    /**
     * Git 저장소의 특정 브랜치의 최신상태만을 클론
     *
     * @param gitUrl    Git 저장소 URL
     * @param token     Git 접근 토큰
     * @param branch    Git 클론 할 브랜치
     * @param localPath 저장할 Git 로컬 저장소 위치
     * @throws GitUtilException Git 클론 중 발생할 수 있는 예외
     */
    public String shallowCloneOrPull(String gitUrl, String branch, String token, Path localPath) {
        File localDir = localPath.toFile();
        if (isDirectoryValid(localDir)) {
            return pullUpdates(localDir, branch, token);
        }
        return cloneRepository(gitUrl, branch, token, localDir);
    }

    private String cloneRepository(String gitUrl, String branch, String token, File localDir) {
        try (Git git = executeCloneCommand(gitUrl, branch, token, localDir)) {
            return getLastCommitHash(git);
        } catch (GitAPIException | IOException e) {
            throw new GitUtilException("Git clone이 실패했습니다.", e);
        }
    }

    private Git executeCloneCommand(String gitUrl, String branch, String token, File localDir)
            throws GitAPIException {
        return Git.cloneRepository()
                .setURI(gitUrl)
                .setBranch(branch)
                .setDirectory(localDir)
                .setDepth(1)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider("token", token))
                .call();
    }

    private String getLastCommitHash(Git git) throws IOException {
        try (RevWalk revWalk = new RevWalk(git.getRepository())) {
            RevCommit commit = revWalk.parseCommit(git.getRepository().resolve("HEAD"));
            return commit.getName();
        }
    }

    private String pullUpdates(File localDir, String branch, String token) {
        try (Git git = Git.open(localDir)) {
            PullCommand pullCommand =
                    git.pull()
                            .setCredentialsProvider(
                                    new UsernamePasswordCredentialsProvider("token", token))
                            .setRemote("origin")
                            .setRemoteBranchName(branch);
            pullCommand.call();
            return getLastCommitHash(git);
        } catch (Exception e) {
            throw new GitUtilException("Git pull이 실패했습니다.", e);
        }
    }

    public List<GitFileInfo> getUpdatedFileInfos(
            String repoPath, String oldCommitHash, String newCommitHash) {
        try (Repository repository = openRepository(repoPath);
                RevWalk revWalk = new RevWalk(repository);
                Git git = new Git(repository)) {

            RevCommit oldCommit = getCommit(repository, revWalk, oldCommitHash);
            RevCommit newCommit = getCommit(repository, revWalk, newCommitHash);

            return getDiffEntries(git, repository, oldCommit, newCommit);

        } catch (Exception e) {
            throw new GitUtilException("커밋 간 파일 변경 내역을 가져오는데 실패했습니다.", e);
        }
    }

    private Repository openRepository(String repoPath) throws IOException {
        return new FileRepositoryBuilder().setGitDir(new File(repoPath + "/.git")).build();
    }

    private RevCommit getCommit(Repository repository, RevWalk revWalk, String commitHash)
            throws IOException {
        ObjectId commitId = repository.resolve(commitHash);
        return revWalk.parseCommit(commitId);
    }

    private List<GitFileInfo> getDiffEntries(
            Git git, Repository repository, RevCommit oldCommit, RevCommit newCommit)
            throws IOException, GitAPIException {
        List<DiffEntry> diffs =
                git.diff()
                        .setOldTree(prepareTreeParser(repository, oldCommit))
                        .setNewTree(prepareTreeParser(repository, newCommit))
                        .call();

        String repoPath = repository.getDirectory().getParent();

        return diffs.stream()
                .map(
                        diffEntry ->
                                new GitFileInfo(
                                        diffEntry.getChangeType(),
                                        repoPath + "/" + diffEntry.getNewPath()))
                .toList();
    }

    private boolean isDirectoryValid(File directory) {
        return directory.exists() && directory.isDirectory() && directory.list().length > 0;
    }

    private CanonicalTreeParser prepareTreeParser(Repository repository, RevCommit commit)
            throws IOException {
        // 커밋에서 트리 객체를 가져오기
        RevTree tree = commit.getTree();

        // CanonicalTreeParser 초기화
        CanonicalTreeParser treeParser = new CanonicalTreeParser();
        try (var reader = repository.newObjectReader()) {
            // 트리 파서를 설정하여 커밋의 트리를 읽어올 수 있도록 함
            treeParser.reset(reader, tree.getId());
        }

        return treeParser;
    }
}
