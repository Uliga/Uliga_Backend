package com.uliga.uliga_backend.domain.Record.dao;

import com.uliga.uliga_backend.domain.Record.model.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {
}
