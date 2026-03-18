package com.example.dailyreport.specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

import com.example.dailyreport.entity.*;

public class ReportSpecification {

    public static Specification<Report> search(String keyword, LocalDateTime startDateTime, LocalDateTime endDateTime, String fromTo) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null) {
                predicates.add(cb.like(root.get("task"), "%" + keyword + "%"));
            }

            if ("FROM".equals(fromTo)) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("startTime"), startDateTime));
                predicates.add(cb.lessThanOrEqualTo(root.get("startTime"), endDateTime));
            }

            if ("TO".equals(fromTo)) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("endTime"), startDateTime));
                predicates.add(cb.lessThanOrEqualTo(root.get("endTime"), endDateTime));
            }

            query.orderBy(cb.desc(root.get("startTime")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
