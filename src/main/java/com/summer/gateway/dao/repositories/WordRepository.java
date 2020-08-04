package com.summer.gateway.dao.repositories;

import com.summer.gateway.dao.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Word, Long> {
}
