package hq.com.aop.concurrency;

import java.util.EventObject;
import java.util.Map;

/**
 * @title : 事件对象
 * @describle : 异步事件处理对象
 * <p>
 * Create By yinhaiquan
 * @date 2017/7/3 15:29 星期一
 */
public class AsyncEvent extends EventObject {
    private static final long serialVersionUID = -465371640104493049L;
    private Map<String, Object> params = null;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public AsyncEvent(Object source) {
        super(source);
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
