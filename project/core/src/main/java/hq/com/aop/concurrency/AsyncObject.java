package hq.com.aop.concurrency;

import java.util.Map;

/**
 * @title : 自定封装消息对象
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/7/3 15:35 星期一
 */
public class AsyncObject {
    private AsyncHandlerListener asyncHandlerListener;
    private Map<String, Object> params;

    public AsyncObject() {
    }

    public AsyncObject(AsyncHandlerListener asyncHandlerListener, Map<String, Object> params) {
        this.asyncHandlerListener = asyncHandlerListener;
        this.params = params;
    }

    public AsyncHandlerListener getAsyncHandlerListener() {
        return asyncHandlerListener;
    }

    public void setAsyncHandlerListener(AsyncHandlerListener asyncHandlerListener) {
        this.asyncHandlerListener = asyncHandlerListener;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
