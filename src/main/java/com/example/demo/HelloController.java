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
    // Getters and Setters 省略，請保留你原本的 (若沒寫請補上)
    public Long getId() { return id; }
    public LocalDate getLogDate() { return logDate; }
    public Double getSleepHours() { return sleepHours; }
    public Integer getSteps() { return steps; }
    public Integer getMoodScore() { return moodScore; }
    public String getRiskLevel() { return riskLevel; }
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
        } else {
            return (mood >= 7 && steps >= 6000) ? "低風險 (Low)" : (mood < 4 ? "中風險 (Medium)" : "低風險 (Low)");
        }
    }

    @PostConstruct
    public void initSeedData() {
        if (repository.count() > 0) return;
        LocalDate startDate = LocalDate.now().minusDays(90);
        for (int i = 0; i < 90; i++) {
            double s = (i < 25) ? 4.5 : ((i < 65) ? 6.5 : 8.0);
            int st = (i < 25) ? 2000 : ((i < 65) ? 5000 : 8000);
            int m = (i < 25) ? 3 : ((i < 65) ? 6 : 9);
            repository.save(new HealthLog(startDate.plusDays(i), s, st, m, calculateRiskLevel(s, st, m)));
        }
    }

    @GetMapping
    public List<HealthLog> getAllLogs() { return repository.findAll(); }

    @PostMapping
    public HealthLog createLog(@RequestBody HealthLog log) {
        log.setRiskLevel(calculateRiskLevel(log.getSleepHours(), log.getSteps(), log.getMoodScore()));
        return repository.save(log);
    }
}
