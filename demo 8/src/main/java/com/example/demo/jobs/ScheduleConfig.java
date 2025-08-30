package com.example.demo.jobs;




import com.example.demo.config.MonitoringProps;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScheduleConfig {

    // Expose cron from properties for @Scheduled SpEL
    @Bean(name = "monitoringScheduleCron")
    public String monitoringScheduleCron(MonitoringProps props) {
        return props.getSchedule().getCron();
    }
}

