package ru.bk.artv.vkrattach.config.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Логирует входящий request в два этапа. Первый этап до обработки контроллером:
 * INFO - principal UserName, request method, request URI, request query, remote addr
 * DEBUG - headers
 * DEBUG (после прохождения контроллера) - request body (по умолчанию 1000 char)
 */
@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {

    private int requestMaxPayloadLength = 1000;


    private boolean isIncludePayload = false;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        boolean isFirstRequest = !isAsyncDispatch(request);
        HttpServletRequest requestToUse = request;

        if (isIncludePayload && isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
            requestToUse = new ContentCachingRequestWrapper(request, requestMaxPayloadLength);

        }

        logRequestDetails(requestToUse);
        filterChain.doFilter(requestToUse, response);

        log.debug("REQUEST body: {}", getLimitedRequestBody((ContentCachingRequestWrapper) requestToUse));

    }

    private void logRequestDetails(HttpServletRequest request) throws IOException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String query = Optional.ofNullable(request.getQueryString()).orElse("");
        String auth = "";

        if (principal instanceof UserDetails userDetails) {
            auth = userDetails.getUsername();
        }

        log.info("REQUEST Method: {}, Request URI: {}{}, user : {}, remote addr is : {}", request.getMethod(), request.getRequestURI(), query, auth, request.getRemoteAddr());
        log.debug("REQUEST headers are : {}", getRequestHeaders(request));
    }

    private String getRequestHeaders(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames())
                .stream()
                .map(headerName -> headerName + ": " + request.getHeader(headerName))
                .collect(Collectors.joining(", "));
    }


    private String getLimitedRequestBody(ContentCachingRequestWrapper request) throws IOException {
        ContentCachingRequestWrapper wrapper =
                WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                int length = Math.min(buf.length, requestMaxPayloadLength);
                try {
                    return new String(buf, 0, length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException ex) {
                    return "[unknown]";
                }
            }
        }
        return "NO CONTENT";
    }

    public void setRequestMaxPayloadLength(int requestMaxPayloadLength) {
        this.requestMaxPayloadLength = requestMaxPayloadLength;
    }


    public void setIncludePayload(boolean includePayload) {
        isIncludePayload = includePayload;
    }
}
