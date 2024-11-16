package com.finance.repository;

import com.finance.model.KeyInformationPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface KeyInformationPermissionRepository extends JpaRepository<KeyInformationPermission, Long>, JpaSpecificationExecutor<KeyInformationPermission> {
    Optional<KeyInformationPermission> findFirstByAccountIdAndKeyInformationId(Long accountId, Long keyInformationId);
    Optional<KeyInformationPermission> findFirstByAccountIdAndKeyInformationGroupId(Long accountId, Long keyInformationGroupId);
}

