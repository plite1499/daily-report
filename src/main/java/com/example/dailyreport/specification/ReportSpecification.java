package com.example.dailyreport.specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

import com.example.dailyreport.entity.*;

public class ReportSpecification {

    public static Specification<Report> search(String keyword, LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null) {
                predicates.add(cb.like(root.get("task"), "%" + keyword + "%"));
            }

            if (from != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("startTime"), from));
            }

            if (to != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("startTime"), to));
            }

            query.orderBy(cb.desc(root.get("startTime")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
