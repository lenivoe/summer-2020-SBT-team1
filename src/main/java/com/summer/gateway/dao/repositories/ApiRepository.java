package com.summer.gateway.dao.repositories;


import com.summer.gateway.dao.entity.Api;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ApiRepository extends JpaRepository<Api, Long> {

}
