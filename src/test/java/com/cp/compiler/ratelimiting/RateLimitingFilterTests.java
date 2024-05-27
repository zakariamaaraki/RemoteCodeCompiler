package com.cp.compiler.ratelimiting;

import com.cp.compiler.api.filters.RateLimitingFilter;
import com.cp.compiler.consts.WellKnownParams;
import com.cp.compiler.services.platform.ratelimiting.LeakyBucket;
import com.cp.compiler.services.platform.ratelimiting.UserBucketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RateLimitingFilterTests {

    @Mock
    private UserBucketService userBucketService;

    @Mock
    private LeakyBucket leakyBucket;

    @Mock
    private FilterChain filterChain;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @InjectMocks
    private RateLimitingFilter rateLimitingFilter;

    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws IOException {
        responseWriter = new StringWriter();
        lenient().when(httpServletResponse.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    void testDoFilter_UserIdAbsent() throws IOException, ServletException {

        // Given
        when(httpServletRequest.getHeader(WellKnownParams.USER_ID)).thenReturn(null);
        when(userBucketService.getBucket("defaultUserId")).thenReturn(leakyBucket);
        when(leakyBucket.allowRequest()).thenReturn(true);

        // When
        rateLimitingFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        // Then
        verify(userBucketService).getBucket("defaultUserId");
        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
    }

    @Test
    void testDoFilter_UserIdPresent() throws IOException, ServletException {

        // Given
        when(httpServletRequest.getHeader(WellKnownParams.USER_ID)).thenReturn("user123");
        when(userBucketService.getBucket("user123")).thenReturn(leakyBucket);
        when(leakyBucket.allowRequest()).thenReturn(true);

        // When
        rateLimitingFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        // Then
        verify(userBucketService).getBucket("user123");
        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
    }

    @Test
    void testDoFilter_UserThrottled() throws IOException, ServletException {

        // Given
        when(httpServletRequest.getHeader(WellKnownParams.USER_ID)).thenReturn("user123");
        when(userBucketService.getBucket("user123")).thenReturn(leakyBucket);
        when(leakyBucket.allowRequest()).thenReturn(false);

        // When
        rateLimitingFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        // Then
        verify(userBucketService).getBucket("user123");
        verify(httpServletResponse).setStatus(429);
        verifyNoMoreInteractions(filterChain);
    }
}
