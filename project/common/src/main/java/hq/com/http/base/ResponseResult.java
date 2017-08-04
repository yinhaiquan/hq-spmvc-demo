package hq.com.http.base;

import java.io.Serializable;

/**
 * @Describle: http请求返回数据实体类
 * <p>
 * <b>note:</b>
 * 正常情况 执行成功 返回200状态码
 * 请求地址不存在 返回404
 * 服务器内部报错 返回500
 * 频繁的采集数据，则返回403 拒绝你请求
 * 返回null,则请求服务异常
 * <p>
 * </p>
 * @Author: YinHq
 * @Date: Created By 下午 3:19 2017/6/10 0010
 * @Modified By
 */
public class ResponseResult implements Serializable {
    private static final long serialVersionUID = 2286588404364386239L;
    /*http请求返回码*/
    private int HttpCode;
    /*http请求返回数据*/
    private String content;

    public ResponseResult() {
    }

    public int getHttpCode() {
        return HttpCode;
    }

    public void setHttpCode(int httpCode) {
        HttpCode = httpCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "HttpCode=" + HttpCode +
                ", content='" + content + '\'' +
                '}';
    }
}
