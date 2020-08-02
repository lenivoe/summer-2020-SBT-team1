package com.summer.gateway.dao.repositories;

import com.summer.gateway.dao.entity.Api;
import com.summer.gateway.dao.entity.Instance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApiRepository extends JpaRepository<Api, Long> {
    List<Api> findByInstancesEquals(Instance instance);
}
