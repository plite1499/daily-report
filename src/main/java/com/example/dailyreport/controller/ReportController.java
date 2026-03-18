package com.example.dailyreport.controller;

import com.example.dailyreport.dto.ReportSearchForm;
import com.example.dailyreport.entity.Report;
import com.example.dailyreport.repository.ReportRepository;
import com.example.dailyreport.service.ReportService;

import jakarta.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import com.example.dailyreport.specification.ReportSpecification;

@Controller
public class ReportController {

    private final ReportService reportService;
    private final ReportRepository reportRepository;

    public ReportController(ReportService reportService, ReportRepository reportRepository) {
        this.reportService = reportService;
        this.reportRepository = reportRepository;
    }

    // 日報作成画面
    @GetMapping("/report/add")
    public String showAddForm(Model model) {
        model.addAttribute("report", new Report());
        return "addReport";
    }

    // 日報保存
    @PostMapping("/report/save")
    public String saveReport(@Valid @ModelAttribute Report report, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "addReport";
        }

        reportService.calculateWorkDuration(report);
        reportService.save(report);
        redirectAttributes.addFlashAttribute("message", "日報を登録しました");
        return "redirect:/report/list";
    }

    // 日報一覧
    @GetMapping("/report/list")
    public String showReportList(@ModelAttribute ReportSearchForm form, Model model) {

        Pageable pageable = PageRequest.of(form.getPage(), 100, Sort.by("startTime").descending());
System.out.println("ppppppppppppppppppp" + form.getPage());
        Page<Report> reportPage = reportRepository.findAll(pageable);

        // 作業時間を時間：分に変換
        for (Report r : reportPage.getContent()) {
            if (r.getWorkDuration() != null) {
                long hours = r.getWorkDuration() / 60 - 1;
                long minutes = r.getWorkDuration() % 60;
                r.setWorkDurationStr(hours + "時間 " + minutes + "分");
            } else {
                r.setWorkDurationStr("");
            }
        }
        model.addAttribute("reportPage", reportPage);
        model.addAttribute("reports", reportPage.getContent());
        model.addAttribute("form", form); 

        return "reportList";
    }

    @GetMapping("/report/search")
    public String searchReport(@ModelAttribute ReportSearchForm form, Model model) {

        Pageable pageable = PageRequest.of(form.getPage(), 100, Sort.by("startTime").descending());

        Page<Report> reportPage;

        String keyword = form.getKeyword();
        LocalDate from = form.getFrom();
        LocalDate to = form.getTo();

        boolean hasKeyword = keyword != null && !keyword.isEmpty();
        boolean hasFrom = from != null;
        boolean hasTo = to != null;
        boolean hasRange = from != null && to != null;

        if (hasRange) {
            if (hasKeyword) {
                reportPage = reportRepository.findByTaskContainingAndStartTimeBetween(keyword, from.atStartOfDay(),
                        to.atTime(LocalTime.MAX), pageable);
            } else {
                reportPage = reportRepository.findByStartTimeBetween(from.atStartOfDay(), to.atTime(LocalTime.MAX),
                        pageable);
            }
        } else if (!hasFrom && !hasTo && !hasKeyword) {
            reportPage = reportRepository.findAll(pageable);
        } else {
            if (keyword != null && keyword.isBlank()) {
                keyword = null;
            }
            LocalDateTime fromDateTime = (from != null) ? from.atStartOfDay() : null;
            LocalDateTime toDateTime = (to != null) ? to.atTime(LocalTime.MAX) : null;

            reportPage = reportRepository.findAll(ReportSpecification.search(keyword, fromDateTime, toDateTime), pageable);
        }

        for (Report r : reportPage.getContent()) {
            if (r.getWorkDuration() != null) {
                long hours = r.getWorkDuration() / 60 - 1;
                long minutes = r.getWorkDuration() % 60;
                r.setWorkDurationStr(hours + "時間 " + minutes + "分");
            } else {
                r.setWorkDurationStr("");
            }
        }

        model.addAttribute("reportPage", reportPage);
        model.addAttribute("reports", reportPage.getContent());
        model.addAttribute("form", form);

        return "reportList";
    }

    // 日報削除
    @PostMapping("/report/delete")
    public String deleteReports(@RequestParam("ids") List<Integer> ids, RedirectAttributes redirectAttributes) {
        reportService.deleteByIds(ids);
        redirectAttributes.addFlashAttribute("message", "削除しました");
        return "redirect:/report/list";
    }

    // 詳細画面
    @GetMapping("/report/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Report report = reportService.findById(id);

        if (report.getWorkDuration() != null) {
            long hours = report.getWorkDuration() / 60 - 1;
            long minutes = report.getWorkDuration() % 60;
            report.setWorkDurationStr(hours + "時間 " + minutes + "分");
        } else {
            report.setWorkDurationStr("");
        }
        model.addAttribute("report", report);
        return "reportDetail";
    }

    // 編集画面
    @GetMapping("/report/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {

        Report report = reportRepository.findById(id).orElse(null);

        model.addAttribute("report", report);

        return "editReport";
    }

    // 更新
    @PostMapping("/report/update")
    public String updateReport(@Valid @ModelAttribute Report report, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "editReport";
        }

        reportService.calculateWorkDuration(report);
        reportService.save(report);
        redirectAttributes.addFlashAttribute("message", "日報を更新しました");

        return "redirect:/report/list";
    }
}