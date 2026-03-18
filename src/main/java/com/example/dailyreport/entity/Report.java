package com.example.dailyreport.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "report")
public class Report {

    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 開始時間
    @NotNull(message = "開始時間は必須です")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    // 終了時間
    @NotNull(message = "終了時間は必須です")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    // 作業時間
    @Column(name = "work_duration", nullable = false)
    private Long workDuration; // 作業時間を分単位で保持

    // 作業内容
    @NotBlank(message = "作業内容は必須です")
    @Size(max = 200, message = "200文字以内で入力してください")
    private String task;

    // 次回タスク
    @Column(name = "next_task")
    @Size(max = 200, message = "200文字以内で入力してください")
    private String nextTask;

    // メモ
    @Size(max = 200, message = "200文字以内で入力してください")
    private String memo;

    // 作成日時
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Transient 
    private String workDurationStr;

    // 日付チェック
    @AssertTrue(message = "終了時間は開始時間より後にしてください")

    public boolean isValidDateRange() {

        if (startTime == null || endTime == null) {
            return true;
        }

        return endTime.isAfter(startTime);
    }
}