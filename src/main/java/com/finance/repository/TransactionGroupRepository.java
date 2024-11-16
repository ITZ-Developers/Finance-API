package com.finance.repository;

import com.finance.model.TransactionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TransactionGroupRepository extends JpaRepository<TransactionGroup, Long>, JpaSpecificationExecutor<TransactionGroup> {
    Optional<TransactionGroup> findFirstByName(String name);
}
