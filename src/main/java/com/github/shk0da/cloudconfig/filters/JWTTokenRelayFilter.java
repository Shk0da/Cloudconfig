package com.github.shk0da.cloudconfig.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import com.github.shk0da.cloudconfig.config.CloudConfigConstants;

import java.util.Set;

@Component
@Profile("!" + CloudConfigConstants.PROFILE_OAUTH2)
public class JWTTokenRelayFilter extends ZuulFilter {

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Set<String> headers = (Set<String>) ctx.get("ignoredHeaders");
        // JWT tokens should be relayed to the resource servers
        headers.remove("authorization");
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
