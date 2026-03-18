package com.example.dailyreport.service;

import com.example.dailyreport.dto.ReportSearchForm;
import com.example.dailyreport.entity.Report;
import com.example.dailyreport.repository.ReportRepository;
import com.example.dailyreport.specification.ReportSpecification;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    // 日報保存
    public Report save(Report report) {
        return reportRepository.save(report);
    }

    // 日報一覧取得
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    // 日報削除
    public void deleteByIds(List<Integer> ids) {
        reportRepository.deleteAllById(ids);
    }

    public Report findById(Integer id) {
        return reportRepository.findById(id).orElse(null);
    }

    public void calculateWorkDuration(Report report) {
        if (report.getStartTime() != null && report.getEndTime() != null) {
            long minutes = Duration.between(report.getStartTime(), report.getEndTime()).toMinutes();
            report.setWorkDuration(minutes);
        }
    }

    // 検索
    // public Page<Report> search(ReportSearchForm form) {

    //     Pageable pageable = PageRequest.of(form.getPage(), 100, Sort.by("startTime").descending());

    //     String keyword = form.getKeyword();
    //     LocalDate from = form.getFrom();
    //     LocalDate to = form.getTo();

    //     boolean hasKeyword = keyword != null && !keyword.isEmpty();
    //     boolean hasFrom = from != null;
    //     boolean hasTo = to != null;
    //     boolean hasRange = hasFrom && hasTo;

    //     Page<Report> reportPage;

    //     if (hasRange) {
    //         if (hasKeyword) {
    //             reportPage = reportRepository.findByTaskContainingAndStartTimeBetween(keyword, from.atStartOfDay(),
    //                     to.atTime(LocalTime.MAX), pageable);
    //         } else {
    //             reportPage = reportRepository.findByStartTimeBetween(from.atStartOfDay(), to.atTime(LocalTime.MAX),
    //                     pageable);
    //         }
    //     } else if (!hasFrom && !hasTo && !hasKeyword) {
    //         reportPage = reportRepository.findAll(pageable);
    //     } else {
    //         if (keyword != null && keyword.isBlank()) {
    //             keyword = null;
    //         }

    //         LocalDateTime fromDateTime = (from != null) ? from.atStartOfDay() : null;
    //         LocalDateTime toDateTime = (to != null) ? to.atTime(LocalTime.MAX) : null;

    //         reportPage = reportRepository.findAll(ReportSpecification.search(keyword, fromDateTime, toDateTime),
    //                 pageable);
    //     }

    //     for (Report r : reportPage.getContent()) {
    //         if (r.getWorkDuration() != null) {
    //             long hours = r.getWorkDuration() / 60 - 1;
    //             long minutes = r.getWorkDuration() % 60;
    //             r.setWorkDurationStr(hours + "時間 " + minutes + "分");
    //         } else {
    //             r.setWorkDurationStr("");
    //         }
    //     }

    //     return reportPage;
    // }
}