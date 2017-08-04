package hq.com.control;

import com.alibaba.fastjson.JSONObject;
import hq.com.aop.ctx.SpringApplicationContext;
import hq.com.aop.exception.IllegalArgumentsException;
import hq.com.aop.handler.ProxyHandler;
import hq.com.aop.utils.StringUtils;
import hq.com.exception.IllegalOptionException;
import hq.com.vo.RequestRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 请求响应处理器
 * <p>
 * <b>note:</b>
 * 1. 使用application/x-www-form-urlencoded类型可用RequestParam、request等接收参数
 * 2. 使用application/json类型可用RequestBody接收参数RequestParam、request等接收不到!!!
 * </p>
 *
 * @author yinhaiquan
 * @date 2017/05/22 11:24:26.
 */
@Controller
@RequestMapping(value = "/handler")
public class FocusController extends BaseController {
    public static Logger log = LoggerFactory.getLogger(FocusController.class);

    @Resource(name = "proxyHandler")
    private ProxyHandler proxyHandler;

    /**
     * 请求响应处理器
     * <p>
     * 请求头类型application/x-www-form-urlencoded，使用ajax请求
     * <p>
     * Request/response processing
     * root:{
     * route:{
     * fk:"fuck"//路由信息 待用
     * },
     * msg:{//请求参数位置，若msg里面包含字符串和对象，则必须按照接收方法顺序传递。
     * name:"sdf",
     * userInfo:{
     * name:"hehe",
     * age:90,
     * desc:"4444444444444"
     * }
     * }
     * }
     * </p>
     *
     * @param className  全类名/beanName
     * @param methodName 方法名
     * @param request
     * @return
     * @throws IllegalArgumentsException
     */
    @RequestMapping(value = "/{className}/{methodName}",
            method = {RequestMethod.GET, RequestMethod.POST},
            headers = "Accept=application/x-www-form-urlencoded;charset=utf-8",
            consumes = "application/x-www-form-urlencoded;charset=utf-8",
            produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object handler(@PathVariable("className") String className,
                          @PathVariable("methodName") String methodName,
                          HttpServletRequest request) throws IllegalArgumentsException, IllegalOptionException {
        RequestRoot rr = (RequestRoot) decodeRequest(request);
        return doFuck(className, methodName, rr);
    }

    /**
     * 请求响应处理器
     * <p>
     * 请求头类型application/json，适用json格式传输请求类型
     * <p>
     * Request/response processing
     * root:{
     * route:{
     * fk:"fuck"//路由信息 待用
     * },
     * msg:{//请求参数位置，若msg里面包含字符串和对象，则必须按照接收方法顺序传递。
     * name:"sdf",
     * userInfo:{
     * name:"hehe",
     * age:90,
     * desc:"4444444444444"
     * }
     * }
     * }
     * </p>
     *
     * @param className  全类名/beanName
     * @param methodName 方法名
     * @param map
     * @return
     * @throws IllegalArgumentsException
     */
    @RequestMapping(value = "/{className}/{methodName}",
            method = {RequestMethod.GET, RequestMethod.POST},
            headers = "Accept=application/json;charset=utf-8",
            consumes = "application/json;charset=utf-8",
            produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object handler2(@PathVariable("className") String className,
                           @PathVariable("methodName") String methodName,
                           @RequestBody Map<String, Map<String, Object>> map) throws IllegalArgumentsException, IllegalOptionException {
        RequestRoot rr = (RequestRoot) decodeRequest(map);
        return doFuck(className, methodName, rr);
    }

    private Object doFuck(String className, String methodName, RequestRoot rr) throws IllegalArgumentsException, IllegalOptionException {
        Map<String, String[]> params = rr.getMsg();
        Map<String, String[]> route = rr.getRoute();
        //针对route路由信息处理,国际化信息也在此处理
        SpringApplicationContext.setLOCALE("zh_CN");
        //
        //route处理
        //
        return proxyHandler.handler(className, methodName, params);
    }

    public Object decodeRequest(HttpServletRequest request) {
        RequestRoot rr = new RequestRoot();
        Map<String, String[]> params = request.getParameterMap();
        Map<String, String[]> msg = new LinkedHashMap<String, String[]>();
        Map<String, String[]> route = new LinkedHashMap<String, String[]>();
        if (StringUtils.isNotEmpty(params)) {
            for (String key : params.keySet()) {
                if (isMsgOrRoute(key, REGMSG)) {
                    msg.put(getRegKey(key, MSG), params.get(key));
                    continue;
                }
                if (isMsgOrRoute(key, REGROUTE)) {
                    route.put(getRegKey(key, ROUTE), params.get(key));
                    continue;
                }
            }
        }
        rr.setMsg(msg);
        rr.setRoute(route);
        return rr;
    }

    @Override
    public Object decodeRequest(Map<String, Map<String, Object>> map) {
        RequestRoot rr = new RequestRoot();
        Map<String, String[]> msg = new LinkedHashMap<String, String[]>();
        Map<String, String[]> route = new LinkedHashMap<String, String[]>();
        Map<String, Object> msg_2 = (Map<String, Object>) map.get("root").get("msg");
        Map<String, Object> route_2 = (Map<String, Object>) map.get("root").get("route");
        if (StringUtils.isNotEmpty(msg_2)) {
            Set<String> set = msg_2.keySet();
            if (StringUtils.isNotEmpty(set)) {
                String key = set.iterator().next();
                Map<String, Object> kv = (Map<String, Object>) msg_2.get(key);
                if (StringUtils.isNotEmpty(kv)) {
                    Set<Map.Entry<String, Object>> entrySet = kv.entrySet();
                    for (Map.Entry<String, Object> entry : entrySet) {
                        StringBuffer sb = new StringBuffer(key);
                        sb.append(StringUtils.PREFIX);
                        String entry_key = sb.append(entry.getKey()).append(StringUtils.SUFFIX).toString();
                        Object entry_value = StringUtils.isNotEmpty(entry.getValue()) ? entry.getValue() : "";
                        msg.put(entry_key, new String[]{String.valueOf(entry_value)});
                    }
                }
            }
        }

        if (StringUtils.isNotEmpty(route_2)) {
            Set<Map.Entry<String, Object>> entrySet = route_2.entrySet();
            for (Map.Entry<String, Object> entry : entrySet) {
                String entry_key = entry.getKey();
                Object entry_value = StringUtils.isNotEmpty(entry.getValue()) ? entry.getValue() : "";
                route.put(entry_key, new String[]{String.valueOf(entry_value)});
            }
        }
        rr.setMsg(msg);
        rr.setRoute(route);
        return rr;
    }

    public String encodeResponse(Object obj) {
        return null;
    }
}
