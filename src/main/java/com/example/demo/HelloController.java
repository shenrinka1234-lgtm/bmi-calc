package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.*;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;

// ==========================================
// 1. 資料庫資料表定義 (Entity)
// ==========================================
@Entity
@Table(name = "health_logs")
class HealthLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate logDate;
    private Double sleepHours;
    private Integer steps;
    private Integer moodScore;
    private String riskLevel;

    // 構造函數
    public HealthLog() {}
    public HealthLog(LocalDate logDate, Double sleepHours, Integer steps, Integer moodScore, String riskLevel) {
        this.logDate = logDate;
        this.sleepHours = sleepHours;
        this.steps = steps;
        this.moodScore = moodScore;
        this.riskLevel = riskLevel;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getLogDate() { return logDate; }
    public void setLogDate(LocalDate logDate) { this.logDate = logDate; }
    public Double getSleepHours() { return sleepHours; }
    public void setSleepHours(Double sleepHours) { this.sleepHours = sleepHours; }
    public Integer getSteps() { return steps; }
    public void setSteps(Integer steps) { this.steps = steps; }
    public Integer getMoodScore() { return moodScore; }
    public void setMoodScore(Integer moodScore) { this.moodScore = moodScore; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
}

// ==========================================
// 2. 資料庫操作介面 (Repository)
// ==========================================
interface HealthLogRepository extends org.springframework.data.jpa.repository.JpaRepository<HealthLog, Long> {}

// ==========================================
// 3. 核心商務邏輯與 API 控制器 (Controller)
// ==========================================
@RestController
@RequestMapping("/health-logs")
public class HelloController {

    @Autowired
    private HealthLogRepository repository;

    // 題目核心技術：決策樹分類邏輯 (多層判斷)
    private String calculateRiskLevel(double sleep, int steps, int mood) {
        if (sleep < 5.5) {
            if (mood <= 4) {
                return "高風險 (High)";
            } else {
                return "中風險 (Medium)";
            }
        } else if (sleep >= 5.5 && sleep <= 7.0) {
            if (steps < 4000) {
                return "中風險 (Medium)";
            } else {
                return "低風險 (Low)";
            }
        } else {
            if (mood >= 7 && steps >= 6000) {
                return "低風險 (Low)";
            } else if (mood < 4) {
                return "中風險 (Medium)";
            } else {
                return "低風險 (Low)";
            }
        }
    }

    // 🔥【符合老師評分規範】自動注入 90 天有規律的種子資料
    @PostConstruct
    public void initSeedData() {
        // 如果資料庫已經有資料了，就不重複塞，避免每次重啟都重複
        if (repository.count() > 0) {
            return;
        }

        LocalDate startDate = LocalDate.now().minusDays(90);

        // 組合 ①：睡眠少、步數少、心情差 -> 佔 25 天 (高風險/中風險)
        for (int i = 0; i < 25; i++) {
            double sleep = 4.5 + (i % 2) * 0.5; // 4.5 或 5.0 小時
            int steps = 1500 + (i * 77) % 2000;  // 1500 ~ 3500 步
            int mood = 2 + (i % 3);             // 2 ~ 4 分
            String risk = calculateRiskLevel(sleep, steps, mood);
            repository.save(new HealthLog(startDate.plusDays(i), sleep, steps, mood, risk));
        }

        // 組合 ②：數值混合普通 -> 佔 40 天 (中風險/低風險)
        for (int i = 25; i < 65; i++) {
            double sleep = 6.0 + (i % 2) * 0.8; // 6.0 或 6.8 小時
            int steps = 3500 + (i * 123) % 4000; // 3500 ~ 7500 步
            int mood = 5 + (i % 3);             // 5 ~ 7 分
            String risk = calculateRiskLevel(sleep, steps, mood);
            repository.save(new HealthLog(startDate.plusDays(i), sleep, steps, mood, risk));
        }

        // 組合 ③：睡眠足、步數多、心情好 -> 佔 25 天 (低風險)
        for (int i = 65; i < 90; i++) {
            double sleep = 7.5 + (i % 2) * 1.0; // 7.5 或 8.5 小時
            int steps = 6500 + (i * 245) % 3000; // 6500 ~ 9500 步
            int mood = 8 + (i % 2);             // 8 或 9 分
            String risk = calculateRiskLevel(sleep, steps, mood);
            repository.save(new HealthLog(startDate.plusDays(i), sleep, steps, mood, risk));
        }
    }

    // API 1: 取得所有健康日誌紀錄
    @GetMapping
    public List<HealthLog> getAllLogs() {
        return repository.findAll();
    }

    // API 2: 新增一筆健康日誌
    @PostMapping
    public HealthLog createLog(@RequestBody HealthLog log) {
        if (log.getLogDate() == null) {
            log.setLogDate(LocalDate.now());
        }
        String level = calculateRiskLevel(log.getSleepHours(), log.getSteps(), log.getMoodScore());
        log.setRiskLevel(level);
        return repository.save(log);
    }

    // API 3: 依決策樹即時計算當下風險
    @GetMapping("/risk")
    public String checkInstantRisk(@RequestParam double sleep, @RequestParam int steps, @RequestParam int mood) {
        return calculateRiskLevel(sleep, steps, mood);
    }

    // API 4: 刪除指定日誌
    @DeleteMapping("/{id}")
    public String deleteLog(@PathVariable Long id) {
        repository.deleteById(id);
        return "成功刪除 ID: " + id + " 的紀錄";
    }
}
