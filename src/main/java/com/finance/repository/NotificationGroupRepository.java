package com.finance.repository;

import com.finance.model.NotificationGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface NotificationGroupRepository extends JpaRepository<NotificationGroup, Long>, JpaSpecificationExecutor<NotificationGroup> {
    Optional<NotificationGroup> findFirstByName(String name);
}
