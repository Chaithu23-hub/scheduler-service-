package com.example.scheduler_service.repository;


import com.example.scheduler_service.entity.ActiveRegistrationAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActiveRegistrationAccountRepository
        extends JpaRepository<ActiveRegistrationAccount, Long> {

}
