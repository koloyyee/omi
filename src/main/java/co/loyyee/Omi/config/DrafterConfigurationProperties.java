package co.loyyee.Omi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api")
public record DrafterConfigurationProperties(String openai) {
}
