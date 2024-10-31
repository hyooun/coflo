package com.reviewping.coflo.git;

import java.io.File;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Component;

@Component
public class GitUtil {

    /**
     * Git 저장소의 특정 브랜치의 최신상태만을 클론
     * @param gitUrl Git 저장소 URL
     * @param token Git 접근 토큰
     * @param branch Git 클론 할 브랜치
     * @param localPath 저장할 Git 로컬 저장소 위치
     * @throws GitUtilException Git 클론 중 발생할 수 있는 예외
     */
    public void shallowCloneBranch(String gitUrl, String branch, String token, String localPath) {

        try {
            // Git 클론 설정
            CloneCommand cloneCommand =
                    Git.cloneRepository()
                            .setURI(gitUrl)
                            .setBranch(branch)
                            .setDirectory(new File(localPath))
                            .setDepth(1)
                            .setCredentialsProvider(
                                    new UsernamePasswordCredentialsProvider("token", token));
            // Git 클론 실행
            Git git = cloneCommand.call();
            git.close();

        } catch (GitAPIException e) {
            throw new GitUtilException("Git clone failed: " + e.getMessage());
        }
    }
}
