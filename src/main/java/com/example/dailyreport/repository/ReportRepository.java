package com.example.dailyreport.repository;

import com.example.dailyreport.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportRepository extends JpaRepository<Report, Integer>, JpaSpecificationExecutor<Report> {
    List<Report> findAllByOrderByStartTimeDesc();

    Page<Report> findByStartTimeBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);

    Page<Report> findByTaskContaining(String keyword, Pageable pageable);

    Page<Report> findByTaskContainingAndStartTimeBetween(String keyword, LocalDateTime from, LocalDateTime to,
            Pageable pageable);

}