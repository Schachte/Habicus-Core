package com.habicus.core.configuration.ServiceConfiguration;

import com.habicus.core.service.Goal.GoalService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

  @Bean
  public GoalService goalService() {
    return new GoalService();
  }
}
