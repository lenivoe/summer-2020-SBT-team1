package com.summer.gateway.dao.repositories;

import com.summer.gateway.dao.entity.Instance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstanceRepository extends JpaRepository<Instance, Long> {
    Instance findByUuid(String uuid);
}
