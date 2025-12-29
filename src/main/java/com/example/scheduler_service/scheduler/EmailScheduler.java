package com.example.scheduler_service.scheduler;

import com.example.scheduler_service.entity.ActiveRegistrationAccount;
import com.example.scheduler_service.repository.ActiveRegistrationAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EmailScheduler {

    private final RestTemplate restTemplate;
    private final ActiveRegistrationAccountRepository repository;

    @Value("${user-service.base-url}")
    private String userServiceBaseUrl;

    @Value("${user-service.send-email-path}")
    private String sendEmailPath;

    @Value("${user-service.confirm-path}")
    private String confirmPath;

    @Value("${scheduler.job.name}")
    private String jobName;

    @Value("${scheduler.fixed-rate-ms}")
    private long fixedRateMs;

    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void triggerPendingUserEmails() {

        String url =
                userServiceBaseUrl
                        + sendEmailPath
                        + "?confirmationBaseUrl="
                        + userServiceBaseUrl
                        + confirmPath;
        System.out.println(url);

        ResponseEntity<String> response =
                restTemplate.postForEntity(url, null, String.class);


        LocalDateTime now = LocalDateTime.now();

        LocalDateTime nextRunTime =
                now.plus(Duration.ofMillis(fixedRateMs));

        ActiveRegistrationAccount account =
                new ActiveRegistrationAccount();


        account.setJobName(jobName);
        account.setCalledAt(LocalDateTime.now());
        account.setResponse(response.getBody());
        account.setNextScheduledAt(nextRunTime);

        repository.save(account);
    }
}
