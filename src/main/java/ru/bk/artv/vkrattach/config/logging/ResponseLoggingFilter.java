package ru.bk.artv.vkrattach.config.logging;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Логирует response:
 * INFO - время прохождения + код ответа
 * DEBUG - headers
 * DEBUG - response body. Сказывается на производительности. По умолчанию длина лога 1000.
 */
@Slf4j
public class ResponseLoggingFilter implements Filter {

    private int responseMaxPayloadLength = 1000;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        long startTime = System.currentTimeMillis();

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (log.isDebugEnabled()) {
            httpResponse = new ContentCachingResponseWrapper(httpResponse);
        }

        chain.doFilter(request, httpResponse);

        long duration = System.currentTimeMillis() - startTime;

        log.info("RESPONSE status: {} and duration : {}", httpResponse.getStatus(), duration);
        log.debug("RESPONSE headers: {}", getResponseHeaders(httpResponse));

        if (log.isDebugEnabled() && httpResponse instanceof ContentCachingResponseWrapper responseWrapper) {
            log.debug("RESPONSE Body: " + getResponseBody(responseWrapper));
            responseWrapper.copyBodyToResponse();
        }
    }

    private String getResponseHeaders(HttpServletResponse response) {
        return response.getHeaderNames()
                .stream()
                .map(headerName -> headerName + ": " + response.getHeader(headerName))
                .collect(Collectors.joining(", "));
    }

    private String getResponseBody(ContentCachingResponseWrapper responseWrapper) {
        byte[] content = responseWrapper.getContentAsByteArray();
       if (content.length > 0) {
                       String contentStr = new String(content);
            return contentStr.substring(0, Math.min(contentStr.length(), responseMaxPayloadLength));
        }
        return "NO CONTENT";
    }

    public void setResponseMaxPayloadLength(int responseMaxPayloadLength) {
        this.responseMaxPayloadLength = responseMaxPayloadLength;
    }
}
