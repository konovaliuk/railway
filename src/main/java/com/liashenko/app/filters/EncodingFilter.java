package com.liashenko.app.filters;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@WebFilter( urlPatterns = {"/*"},
        filterName = "EncodingFilter",
        description = "Filter for all requests")
public class EncodingFilter implements Filter {
    private static final Logger classLogger = LogManager.getLogger(EncodingFilter.class);
    private static final String ENCODING_UTF8 = StandardCharsets.UTF_8.name();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        encodingSetter(request, response);
//        response.setContentType("text/html;");
        filterChain.doFilter(request, response);
    }

    private static void encodingSetter(ServletRequest request, ServletResponse response) {
        try {
            if (request.getCharacterEncoding() == null || !request.getCharacterEncoding().equals(ENCODING_UTF8)) {
                request.setCharacterEncoding(ENCODING_UTF8);
            }

            if (response.getCharacterEncoding() == null || !request.getCharacterEncoding().equals(ENCODING_UTF8)) {
                response.setCharacterEncoding(ENCODING_UTF8);
            }
        } catch (UnsupportedEncodingException ex) {
            classLogger.error(ex);
        }
    }
}