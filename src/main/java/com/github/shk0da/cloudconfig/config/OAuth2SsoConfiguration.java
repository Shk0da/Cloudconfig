package com.github.shk0da.cloudconfig.config;

import com.github.shk0da.cloudconfig.security.AuthoritiesConstants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@EnableOAuth2Sso
@Configuration
@Profile(CloudConfigConstants.PROFILE_OAUTH2)
public class OAuth2SsoConfiguration extends WebSecurityConfigurerAdapter {

    private final RequestMatcher authorizationHeaderRequestMatcher;

    public OAuth2SsoConfiguration(@Qualifier("authorizationHeaderRequestMatcher") RequestMatcher authorizationHeaderRequestMatcher) {
        this.authorizationHeaderRequestMatcher = authorizationHeaderRequestMatcher;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
            .disable()
            .headers()
            .frameOptions()
            .disable()
        .and()
            .requestMatcher(new NegatedRequestMatcher(authorizationHeaderRequestMatcher))
            .authorizeRequests()
            .antMatchers("/services/**").authenticated()
            .antMatchers("/eureka/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/api/profile-info").permitAll()
            .antMatchers("/api/**").authenticated()
            .antMatchers("/config/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/management/health").permitAll()
            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .anyRequest().permitAll();
    }
}
