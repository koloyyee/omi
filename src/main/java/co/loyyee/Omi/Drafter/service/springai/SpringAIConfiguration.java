package co.loyyee.Omi.Drafter.service.springai;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.memory.ChatMemory;

@Configuration
public class SpringAIConfiguration {

  @Bean
  public ChatMemory chatMemory() {
    return new InMemoryChatMemory();
  }

}
