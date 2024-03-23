package co.loyyee.Omi.Chat;

import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;
@EnabledIfEnvironmentVariable(named = "OPENAI_KEY", matches=".*")
public class FunctionCallback {

    @Configuration
    static class Config {
        @Bean
        @Description("Get weather in location")
        public Function<MockWeatherService.Request, MockWeatherService.Response> weatherFunc() {
            return new MockWeatherService();
        }
    }
}
