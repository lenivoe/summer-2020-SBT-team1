package com.summer.gateway.dao.repositories;

import com.summer.gateway.dao.entity.InstanceService;
import com.summer.gateway.dao.entity.RemoteService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RemoteServiceRepository extends JpaRepository<RemoteService, Long> {
    RemoteService findByNameService(String nameService);
    RemoteService findByInstancesEquals(InstanceService instance);
}
