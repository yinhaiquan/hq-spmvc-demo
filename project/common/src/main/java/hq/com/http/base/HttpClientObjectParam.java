package hq.com.http.base;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * @Describle: 自定义httpclient请求参数类型
 * @Author: YinHq
 * @Date: Created By 上午 10:37 2017/6/10 0010
 * @Modified By
 */
public interface HttpClientObjectParam<T> {
    public List<NameValuePair> formatObject(T t);
}
