package com.cp.compiler.api.filters;

import com.cp.compiler.services.platform.ratelimiting.LeakyBucket;
import com.cp.compiler.services.platform.ratelimiting.UserBucketService;
import com.cp.compiler.consts.WellKnownParams;
import com.cp.compiler.exceptions.CompilerThrottlingException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class RateLimitingFilter implements Filter {

    private final UserBucketService userBucketService;

    public RateLimitingFilter(UserBucketService userBucketService) {
        this.userBucketService = userBucketService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization necessary
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String userId = httpRequest.getHeader(WellKnownParams.USER_ID);

        if (userId == null || userId.isEmpty()) {
            log.warn("{} is absent", WellKnownParams.USER_ID);
            userId = "defaultUserId";
        }

        LeakyBucket bucket = userBucketService.getBucket(userId);

        if (bucket.allowRequest()) {
            log.info("user {} has been allowed", userId);
            chain.doFilter(request, response);
        } else {
            log.warn("user {} has been throttled", userId);
            ((HttpServletResponse) response).setStatus(429);
            response.getWriter().write("Too many requests. Please try again later.");

        }
    }

    @Override
    public void destroy() {
        // No cleanup necessary
    }
}
