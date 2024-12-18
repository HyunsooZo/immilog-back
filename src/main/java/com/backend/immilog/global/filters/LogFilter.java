package com.backend.immilog.global.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LogFilter implements Filter {

    private static final Logger logger =
            LoggerFactory.getLogger(LogFilter.class);
    private static final ObjectMapper objectMapper =
            new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_GREEN = "\u001B[32m";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    )
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 멀티파트 요청인지 확인
        if (httpRequest.getContentType() != null && httpRequest.getContentType().startsWith("multipart/form-data")) {
            chain.doFilter(request, response);
            return;
        }

        // 요청 파라미터 로깅
        String params = httpRequest.getParameterMap().entrySet().stream()
                .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
                .collect(Collectors.joining(", "));

        // 요청 로깅
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("method", httpRequest.getMethod());
        requestMap.put("uri", httpRequest.getRequestURI() + (params.isEmpty() ? "" : "?" + params));
        requestMap.put("headers", getHeadersInfo(httpRequest));

        // 요청 바디를 읽기 위한 래퍼
        MultiReadHttpServletRequest multiReadHttpServletRequest = new MultiReadHttpServletRequest(httpRequest);
        requestMap.put("body", multiReadHttpServletRequest.getRequestBody());

        String requestLog = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestMap);
        logger.info(ANSI_YELLOW + "Incoming request: " + requestLog + ANSI_RESET);

        // 응답 래퍼
        CharResponseWrapper responseWrapper = new CharResponseWrapper(httpResponse);

        // 다음 필터나 서블릿 실행
        chain.doFilter(multiReadHttpServletRequest, responseWrapper);

        // 응답 로깅
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", responseWrapper.getStatus());
        responseMap.put("headers", getHeadersInfo(httpResponse));

        // 응답 바디를 포맷팅
        String responseBody = responseWrapper.getCaptureAsString();
        try {
            if (!responseBody.isEmpty()) {
                // JSON 문자열을 다시 JSON 객체로 변환 후 포맷팅
                Object json = objectMapper.readValue(responseBody, Object.class);
                responseMap.put("body", json); // JSON 객체로 저장하여 문자열로 직렬화되지 않도록 함
            } else {
                responseMap.put("body", responseBody);
            }
        } catch (IOException e) {
            // JSON 파싱 실패 시 원래 문자열 유지
            responseMap.put("body", responseBody);
        }

        String responseLog = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseMap);
        logger.info(ANSI_GREEN + "Outgoing response: " + responseLog + ANSI_RESET);

        // 실제 응답 출력
        ServletOutputStream out = httpResponse.getOutputStream();
        out.write(responseWrapper.getCaptureAsBytes());
        out.flush();
    }

    @Override
    public void destroy() {
    }

    private Map<String, String> getHeadersInfo(
            HttpServletRequest request
    ) {
        Map<String, String> map = new HashMap<>();
        for (String headerName : new java.util.ArrayList<>(java.util.Collections.list(request.getHeaderNames()))) {
            map.put(headerName, request.getHeader(headerName));
        }
        return map;
    }

    private Map<String, String> getHeadersInfo(
            HttpServletResponse response
    ) {
        Map<String, String> map = new HashMap<>();
        for (String headerName : response.getHeaderNames()) {
            map.put(headerName, response.getHeader(headerName));
        }
        return map;
    }

    @Getter
    private static class MultiReadHttpServletRequest extends HttpServletRequestWrapper {

        private final String requestBody;

        public MultiReadHttpServletRequest(
                HttpServletRequest request
        ) throws IOException {
            super(request);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader bufferedReader = request.getReader()) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }
            requestBody = stringBuilder.toString();
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBody.getBytes());
            return new ServletInputStream() {
                @Override
                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }

                @Override
                public boolean isFinished() {
                    return byteArrayInputStream.available() == 0;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                }
            };
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }
    }

    private static class CharResponseWrapper extends HttpServletResponseWrapper {
        private final CharArrayWriter charArrayWriter = new CharArrayWriter();
        private final PrintWriter writer = new PrintWriter(charArrayWriter);
        private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        private final ServletOutputStream outputStream = new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
            }

            @Override
            public void write(int b) throws IOException {
                charArrayWriter.write(b);
                byteArrayOutputStream.write(b);
            }
        };
        private int httpStatus = SC_OK;

        public CharResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public PrintWriter getWriter() {
            return writer;
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return outputStream;
        }

        public int getStatus() {
            return this.httpStatus;
        }

        @Override
        public void setStatus(int sc) {
            super.setStatus(sc);
            this.httpStatus = sc;
        }

        public String getCaptureAsString() {
            writer.flush();
            return charArrayWriter.toString();
        }

        @SneakyThrows
        public byte[] getCaptureAsBytes() {
            outputStream.flush();
            return byteArrayOutputStream.toByteArray();
        }
    }
}
