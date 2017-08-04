package hq.com.vo;

import java.util.Map;

/**
 * @title :
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/5/24 11:15 星期三
 */
public class RequestRoot {
    Map<String, String[]> msg;
    Map<String, String[]> route;

    public RequestRoot() {
    }

    public RequestRoot(Map<String, String[]> msg, Map<String, String[]> route) {
        this.msg = msg;
        this.route = route;
    }

    public Map<String, String[]> getMsg() {
        return msg;
    }

    public void setMsg(Map<String, String[]> msg) {
        this.msg = msg;
    }

    public Map<String, String[]> getRoute() {
        return route;
    }

    public void setRoute(Map<String, String[]> route) {
        this.route = route;
    }
}
