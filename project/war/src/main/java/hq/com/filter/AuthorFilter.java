package hq.com.filter;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @Describle:
 * @Author: YinHq
 * @Date: Created By 下午 2:14 2017/5/29 0029
 * @Modified By
 */
@Deprecated
public class AuthorFilter implements Filter {
    public static Logger log = LoggerFactory.getLogger(AuthorFilter.class);
    private FilterConfig config;
    private String excludePage;
    private String redirectPath;
    private String tag;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        config = filterConfig;
        excludePage = config.getInitParameter("excludePage");
        redirectPath = config.getInitParameter("redirectPath");
        tag = config.getInitParameter("tag");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (!"Y".equalsIgnoreCase(tag)) {
            filterChain.doFilter(request, response);
            return;
        }
        String[] strs = excludePage.split(";");
        HttpServletRequestHandler httpRequest = null;
        //过滤页面
        log.info("请求地址：{}", request.getRequestURI());
        log.info("是否过滤:{}", isContains(request.getRequestURI(), strs));
        if (!isContains(request.getRequestURI(), strs)) {
            httpRequest = new HttpServletRequestHandler(request);
            /** 读取post请求参数 */
            BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) httpRequest.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            Map json = (Map) JSONObject.parse(sb.toString());
            System.out.println(json);
            /*处理自己的业务*/

            response.getWriter().write(JSONObject.toJSONString("json返回字符串"));
            return;
        }
        if (null == httpRequest) {
            filterChain.doFilter(request, response);
        } else {
            filterChain.doFilter(httpRequest, response);
        }
    }

    @Override
    public void destroy() {
        config = null;
    }

    private boolean isContains(String container, String[] strs) {
        for (String url : strs) {
            if (container.indexOf(url) != -1) {
                return true;
            }
        }
        return false;
    }

    private class HttpServletRequestHandler extends HttpServletRequestWrapper {

        private String _body;
        private HttpServletRequest _request;

        public HttpServletRequestHandler(HttpServletRequest request) throws IOException {
            super(request);
            _request = request;

            StringBuffer jsonStr = new StringBuffer();
            try (BufferedReader bufferedReader = request.getReader()) {
                String line;
                while ((line = bufferedReader.readLine()) != null)
                    jsonStr.append(line);
            }
            JSONObject json = JSONObject.parseObject(jsonStr.toString());
            _body = json.toJSONString();
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(_body.getBytes());
            return new ServletInputStream() {
                @Override
                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }

                @Override
                public boolean isFinished() {
                    return true;
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
            return new BufferedReader(new InputStreamReader(this.getInputStream()));
        }

    }
}
