package com.summer.gateway.dao.repositories;

import com.summer.gateway.dao.entity.InstanceService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstanceServiceRepository extends JpaRepository<InstanceService, Integer> {
    InstanceService findByUid(String uid);
}
