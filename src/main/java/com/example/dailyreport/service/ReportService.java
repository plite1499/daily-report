package com.example.dailyreport.service;

import com.example.dailyreport.entity.Report;
import com.example.dailyreport.repository.ReportRepository;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.List;

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
}