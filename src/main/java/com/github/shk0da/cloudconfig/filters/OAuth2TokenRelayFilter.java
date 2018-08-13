package com.github.shk0da.cloudconfig.filters;

import com.github.shk0da.cloudconfig.security.oauth2.AuthorizationHeaderUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import com.github.shk0da.cloudconfig.config.CloudConfigConstants;

@Component
@Profile(CloudConfigConstants.PROFILE_OAUTH2)
public class OAuth2TokenRelayFilter extends ZuulFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        // Add specific authorization headers for OAuth2
        ctx.addZuulRequestHeader(AUTHORIZATION_HEADER,
                AuthorizationHeaderUtil.getAuthorizationHeader());

        return null;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10000;
    }
}
