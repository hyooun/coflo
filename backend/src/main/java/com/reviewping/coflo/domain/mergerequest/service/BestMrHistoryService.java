package com.reviewping.coflo.domain.mergerequest.service;

import com.reviewping.coflo.domain.mergerequest.entity.BestMrHistory;
import com.reviewping.coflo.domain.mergerequest.repository.BestMrHistoryRepository;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BestMrHistoryService {

    private final MergeRequestService mergeRequestService;
    private final ProjectRepository projectRepository;
    private final BestMrHistoryRepository bestMrHistoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addBestMrHistory() {
        // Read
        List<String> usernames = new ArrayList<>();
        List<Project> projects = projectRepository.findAll();
        for (Project p : projects) {
            List<String> findUsernames = mergeRequestService.getUsernameBestMergeRequests(p);
            usernames.addAll(findUsernames);
        }

        // Process
        List<User> users = userRepository.findAllByUsernames(usernames);

        // Write
        List<BestMrHistory> histories =
                users.stream()
                        .map(user -> new BestMrHistory(user.getId(), LocalDate.now()))
                        .collect(Collectors.toList());
        bestMrHistoryRepository.saveAll(histories);
    }
}
