package com.summer.gateway.dao.repositories;

import com.summer.gateway.dao.entity.Api;
import com.summer.gateway.dao.entity.Instance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApiRepository extends JpaRepository<Api, Long> {
    List<Api> findByInstancesEquals(Instance instance);

    @Query("SELECT a from Api a where a.isActive = true")
    List<Api> findByActiveIsTrue();
}
