package com.example.dailyreport.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportSearchForm {

    // 作業内容
    @Size(max = 200, message = "※200文字以内で入力してください")
    private String keyword;

    // 日付
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    // 日付（from）
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate from;

    // 日付（to）
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate to;

    // 範囲検索フラグ
    private boolean range;

    // ページ
    private int page = 0;


    @AssertTrue(message = "※終了時間は開始時間より後にしてください")
    public boolean isValidDateRange() {

        if (from == null || to == null) {
            return true;
        }

        return to.isAfter(from);
    }

}
