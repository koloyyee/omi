package co.loyyee.Omi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "openai")
public record DrafterConfigurationProperties(String apiKey ) {
}
