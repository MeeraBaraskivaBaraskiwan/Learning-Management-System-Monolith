// project/src/main/java/com/example/project/config/SseConfig.java
package com.example.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

import com.example.project.Assessments.Assessments_Assessment.AssessmentDTO;
import com.example.project.CourseContents.CourseContentDTO;
import com.example.project.Notifications.Notification.NotificationDTO;

@Configuration
public class SseConfig {
    /**
     * A multicast sink that will replay the last item to new subscribers.
     */
    @Bean
    public Sinks.Many<NotificationDTO> notificationSink() {
        return Sinks.many().replay().latest();
    }

    @Bean
    public Sinks.Many<CourseContentDTO> courseContentSink() {
        return Sinks.many().replay().latest();
    }

    @Bean
    public Sinks.Many<AssessmentDTO> assessmentSink() {
        return Sinks.many().replay().latest();
    }
}
