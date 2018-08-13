package com.github.shk0da.cloudconfig.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Properties specific to CloudConfig.
 *
 * <p>
 * Properties are configured in the application.yml file.
 * </p>
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    @Bean("cloudConfigProperties")
    public CloudConfigProperties cloudConfigProperties() {
        return new CloudConfigProperties();
    }

    @Bean("loginSecurityConfiguration")
    public LoginSecurityConfiguration loginSecurityConfiguration() {
        return new LoginSecurityConfiguration();
    }
}
