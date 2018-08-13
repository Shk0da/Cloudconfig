package com.github.shk0da.cloudconfig.filters;

import com.github.shk0da.cloudconfig.filters.accesscontrol.AccessControlFilter;
import com.github.shk0da.cloudconfig.filters.responserewriting.SwaggerBasePathRewritingFilter;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.shk0da.cloudconfig.config.CloudConfigProperties;

@Configuration
public class RegistryConfiguration {

    @Configuration
    public static class SwaggerBasePathRewritingConfiguration {

        @Bean
        public SwaggerBasePathRewritingFilter swaggerBasePathRewritingFilter(){
            return new SwaggerBasePathRewritingFilter();
        }
    }

    @Configuration
    public static class AccessControlFilterConfiguration {

        @Bean
        public AccessControlFilter accessControlFilter(RouteLocator routeLocator, CloudConfigProperties cloudConfigProperties){
            return new AccessControlFilter(routeLocator, cloudConfigProperties);
        }
    }

}
