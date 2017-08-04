package hq.com.http.client;

import hq.com.http.base.HttpClientObjectParam;
import hq.com.http.base.ResponseResult;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.*;

/**
 * @Describle: http请求处理客户端
 * @Author: YinHq
 * @Date: Created By 上午 9:25 2017/6/10 0010
 * @Modified By
 */
public class HttpClient {
    public static Logger log = LoggerFactory.getLogger(HttpClient.class);

    /*连接超时时间*/
    private static int CONNECT_TIMEOUT = 120000;
    /*连接请求超时时间*/
    private static int CONNECT_REQUEST_TIMEOUT = 60000;
    /*socket超时时间*/
    private static int SOCKET_TIMEOUT = 120000;
    /*最大重连次数*/
    private static int MAX_REDIRECTS = 6;
    /*是否允许重连*/
    private static boolean CIRCULAR_REDIRECTS_ALLOWED = true;
    /* 默认编码格式*/
    private final static String DEFAULT_CHARSET = "UTF-8";
    public final static String DEFAULT_CONTENT_TYPE = "application/json";
    public final static String X_WWW_FORM_URL_ENCODED = "application/x-www-form-urlencoded";

    /*连接池*/
    private PoolingHttpClientConnectionManager cm;
    private RequestConfig config;
    private DefaultHttpRequestRetryHandler requestRetryHandler;

    private HttpClient() {
        if (null == config) {
            config = RequestConfig.custom()
                    .setExpectContinueEnabled(true)
                    .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                    .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).
                            setConnectTimeout(CONNECT_TIMEOUT).
                            setConnectionRequestTimeout(CONNECT_REQUEST_TIMEOUT).
                            setSocketTimeout(SOCKET_TIMEOUT).
                            setMaxRedirects(MAX_REDIRECTS).
                            setCircularRedirectsAllowed(CIRCULAR_REDIRECTS_ALLOWED).build();
        }
        if (null == cm) {
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().
                    register("http", PlainConnectionSocketFactory.getSocketFactory()).
                    register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
            cm = new PoolingHttpClientConnectionManager(registry);
            /*最大连接数*/
            cm.setMaxTotal(200);
            /*每个路由最大连接数*/
            cm.setDefaultMaxPerRoute(20);
        }
        if (null == requestRetryHandler) {
            requestRetryHandler = new DefaultHttpRequestRetryHandler(MAX_REDIRECTS, true);
        }
    }

    public static HttpClient newInstance() {
        return new HttpClient();
    }


    /**
     * 发送 get请求
     *
     * @param url
     * @return
     */
    public ResponseResult sendHttpGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        return sendHttpGet(httpGet);
    }

    /**
     * 发送Get请求Https
     *
     * @param url
     * @return
     */
    public ResponseResult sendHttpsGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        return sendHttpsGet(httpGet);
    }

    /**
     * 发送 post请求
     *
     * @param httpUrl     请求url
     * @param obj         请求参数对象
     * @param objectParam 参数对象格式化工具
     * @return
     * @description: <p>
     * <b>note:</b>
     * eg: T为MAP类型数据或者其他对象数据
     * HttpClientObjectParam.formatObject(T t){
     * for (String key : maps.keySet()) {
     * nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
     * }
     * }
     * </p>
     */
    public ResponseResult sendHttpPostByObject(String httpUrl, Object obj, HttpClientObjectParam objectParam) {
        log.info("==>调用sendHttpPostByObject(String httpUrl,Object obj, HttpClientObjectParam objectParam)方法");
        HttpPost httpPost = new HttpPost(httpUrl);
        // 创建参数队列
        List<NameValuePair> nameValuePairs = objectParam.formatObject(obj);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, DEFAULT_CHARSET));
        } catch (UnsupportedEncodingException e) {
            log.info("调用httpclient post请求抛出异常:" + e.getMessage());
        }
        return sendHttpPost(httpPost);
    }


    /**
     * 发送 post请求
     *
     * @param url        请求地址
     * @param param      参数
     * @param contenType 请求体参数类型 传null类型默认json格式数据
     * @param headers    请求头信息
     * @return
     * @description: <p>
     * <b>note:</b>
     * contenType为null时，默认application/json 参数param即为json格式参数
     * contenType为application/x-www-form-urlencoded时，参数param即为格式:key1=value1&key2=value2参数
     * .
     * .
     * .
     * <p>
     * </p>
     */
    public ResponseResult sendHttpPost(String url, String param, String contenType, Map<String, String> headers) {
        log.info("==>调用sendHttpPost(String url,String param,String contenType,Map<String, String> headers)方法");
        HttpPost httpPost = new HttpPost(url);
        if (null != param) {
            try {
                httpPost.setHeader("Content-Type", null != contenType ? contenType : DEFAULT_CONTENT_TYPE);
                if (headers != null) {
                    Set<String> keys = headers.keySet();
                    for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
                        String key = (String) i.next();
                        httpPost.addHeader(key, headers.get(key));
                    }
                }
                StringEntity stringEntity = new StringEntity(param, DEFAULT_CHARSET);
                stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, null != contenType ? contenType : DEFAULT_CONTENT_TYPE));
                httpPost.setEntity(stringEntity);
            } catch (Exception e) {
                log.info("调用httpclient post请求抛出异常:" + e.getMessage());
            }
        }
        return sendHttpPost(httpPost);
    }


    private ResponseResult sendHttpPost(HttpPost httpPost) {
        log.info("==>调用sendHttpPost(HttpPost httpPost)方法");
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        ResponseResult rr = null;
        try {
            /*采用连接池方式获取连接*/
            httpClient = HttpClientBuilder.create().
                    setConnectionManager(cm).
                    setRetryHandler(requestRetryHandler).build();
            /*不用连接池方式*/
            /*httpClient = HttpClients.createDefault();*/
            httpPost.setConfig(config);
            response = httpClient.execute(httpPost);
            rr = getContent(response);
        } catch (Exception e) {
            log.info("调用httpclient post请求抛出异常:" + e.getMessage());
        } finally {
            close(response, httpClient);
        }
        return rr;
    }

    public ResponseResult sendHttpGet(HttpGet httpGet) {
        log.info("==>调用sendHttpGet(HttpGet httpGet)方法");
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        ResponseResult rr = null;
        try {
            /*采用连接池方式获取连接*/
            httpClient = HttpClients.custom()
                    .setConnectionManager(cm)
                    .setRetryHandler(requestRetryHandler).build();
            /*不用连接池方式*/
            /*httpClient = HttpClients.createDefault();*/
            httpGet.setConfig(config);
            response = httpClient.execute(httpGet);
            rr = getContent(response);
        } catch (IOException e) {
            log.info("调用httpclient get请求抛出异常:" + e.getMessage());
        } finally {
            close(response, httpClient);
        }
        return rr;
    }

    public ResponseResult sendHttpsGet(HttpGet httpGet) {
        log.info("==>调用sendHttpsGet(HttpGet httpGet)方法");
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        ResponseResult rr = null;
        try {
            PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.load(new URL(httpGet.getURI().toString()));
            DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
            /*采用连接池方式获取连接*/
            httpClient = HttpClients.custom()
                    .setConnectionManager(cm)
                    .setSSLHostnameVerifier(hostnameVerifier)
                    .setRetryHandler(requestRetryHandler).build();
            /*不用连接池方式*/
            /*httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();*/
            httpGet.setConfig(config);
            response = httpClient.execute(httpGet);
            rr = getContent(response);
        } catch (IOException e) {
            log.info("调用httpclient get请求抛出异常:" + e.getMessage());
        } finally {
            close(response, httpClient);
        }
        return rr;
    }

    private ResponseResult getContent(CloseableHttpResponse response) throws IOException {
        ResponseResult rr = new ResponseResult();
        rr.setHttpCode(response.getStatusLine().getStatusCode());
        String content = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
        rr.setContent(content);
        Header[] headers = response.getAllHeaders();
        log.info("***************客户端 get 请求头信息*******************");
        for (Header header : headers) {
            log.info(header.getName() + ":" + header.getValue());
        }
        log.info("***************客户端 get 请求响应内容*****************");
        log.info("response:" + content);
        return rr;
    }

    /**
     * 关闭连接,释放资源
     *
     * @param response
     * @param httpClient
     */
    private void close(CloseableHttpResponse response, CloseableHttpClient httpClient) {
        try {
            if (null != response) {
                response.close();
            }
            if (null != httpClient) {
                httpClient.close();
            }
        } catch (IOException e) {
            log.info("关闭资源抛出异常:" + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        String url = "http://www.baidu.com/sdfgklfs.do";
        String param = "{\"name\":\"123\",\"password\":\"123456\"}";
        long start = System.currentTimeMillis();
        System.out.println("请求中。。。");
        ResponseResult str = HttpClient.newInstance().sendHttpPost(url, param, null, null);
        System.out.println((System.currentTimeMillis() - start) / 1000);
        System.out.println(str);
    }
}
