package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.*;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;

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

    public HealthLog() {}
    public HealthLog(LocalDate logDate, Double sleepHours, Integer steps, Integer moodScore, String riskLevel) {
        this.logDate = logDate;
        this.sleepHours = sleepHours;
        this.steps = steps;
        this.moodScore = moodScore;
        this.riskLevel = riskLevel;
    }

    // 🔥【關鍵修正】補上這些 Getter 和 Setter，解決找不到 symbol 的錯誤
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
    
    // 這就是編譯器一直在找的方法
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
}

interface HealthLogRepository extends org.springframework.data.jpa.repository.JpaRepository<HealthLog, Long> {}

@RestController
@RequestMapping("/health-logs")
public class HelloController {

    @Autowired
    private HealthLogRepository repository;

    // 決策樹模型：應用資訊增益 (Information Gain) 原理進行特徵切分
    private String calculateRiskLevel(double sleep, int steps, int mood) {
        if (sleep < 5.5) {
            return (mood <= 4) ? "高風險 (High)" : "中風險 (Medium)";
        } else if (sleep >= 5.5 && sleep <= 7.0) {
            return (steps < 4000) ? "中風險 (Medium)" : "低風險 (Low)";
        } 
         else {
    if (mood < 3) return "高風險 (High)"; // 增加這行最強的過濾條件
    return (mood >= 7 && steps >= 6000) ? "低風險 (Low)" : (mood < 4 ? "中風險 (Medium)" : "低風險 (Low)");
}
    }

 

    @GetMapping
    public List<HealthLog> getAllLogs() { return repository.findAll(); }

   @PostMapping
public HealthLog createLog(@RequestBody HealthLog log) {
    // 🔥 在這裡補上這行，自動抓取系統當下的日期
    log.setLogDate(LocalDate.now()); 
    
    log.setRiskLevel(calculateRiskLevel(log.getSleepHours(), log.getSteps(), log.getMoodScore()));
    return repository.save(log);
}
}
