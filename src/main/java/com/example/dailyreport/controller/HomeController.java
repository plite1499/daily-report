package com.example.dailyreport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home"; // src/main/resources/templates/home.html を表示
    }

    // 日報追加画面
    @GetMapping("/report/add")
    public String addReport() {
        return "addReport"; 
    }

    // 日報参照画面
    @GetMapping("/report/list")
    public String listReport() {
        return "reportList"; 
    }
}
