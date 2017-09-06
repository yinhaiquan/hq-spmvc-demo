package hq.com.control;

import hq.com.aop.ctx.SpringApplicationContext;
import hq.com.aop.exception.IllegalArgumentsException;
import hq.com.aop.handler.ClassMethodHandler;
import hq.com.aop.utils.ClassObject;
import hq.com.aop.utils.StringUtils;
import hq.com.exception.IllegalOptionException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @title :
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/5/24 10:13 星期三
 */
public abstract class BaseController {
    public final static String REGMSG = "root\\[msg]\\[";
    public final static String REGROUTE = "root\\[route]\\[";
    public final static String MSG = "msg";
    public final static String ROUTE = "route";
    private final static String PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWhCBkgwkpKk+VIVP8IMHM6ABp\n" +
            "CZmzfYJ+F9a//+7m+5XKrPeeisOobgR/PjSvah316YY26zZTuSDjzOvDAuf+ac2A\n" +
            "PFQyT+TU53gYdtv+aig1gLo+CgbASCZQ5X38dy4/Zjth20PDZyg4o82RcPVjOoMF\n" +
            "QOmk8hQq5kgDQ95n+QIDAQAB";

    public abstract Object decodeRequest(HttpServletRequest request) throws IllegalArgumentsException;

    public abstract Object decodeRequest(Map<String, Map<String, Object>> map) throws IllegalOptionException, IllegalArgumentsException;

    public abstract String encodeResponse(Object obj);

    public String getRegKey(String key, String regType) {
        if (StringUtils.isNotEmpty(key)) {
            switch (regType) {
                case MSG:
                    return key.replaceFirst(REGMSG, "").replaceFirst(StringUtils.SUFFIX, "");
                case ROUTE:
                    return key.replaceFirst(REGROUTE, "").replaceFirst(StringUtils.SUFFIX, "");
            }
        }
        return null;
    }

    public boolean isMsgOrRoute(String key, String reg) {
        if (StringUtils.isNotEmpty(key)) {
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(key);
            return matcher.find();
        }
        return false;
    }

    /**
     * 路由信息处理
     * @description:
     *               1. 国际化信息处理
     *               2. 签名处理
     *               3. ...
     * @param params 路由数据
     */
    public void routeHandler(Map<String, String[]> route,Map<String, String[]> params) throws IllegalOptionException, IllegalArgumentsException {
        if (StringUtils.isNotEmpty(route)){
            /*1. 国际化信息也在此处理*/
            String [] lgs = route.get("lang");
            SpringApplicationContext.setLOCALE(StringUtils.isEmpty(lgs)?"zh_CN":lgs[0]);
            /*2. 签名处理*/
            String [] routeAuths = route.get("auth[sign]");
            if (StringUtils.isNotEmpty(routeAuths)){
                String sign = routeAuths[0];
                Map<String,Object> data = new HashMap<>();
                Set<Map.Entry<String, String[]>> set = params.entrySet();
                for (Map.Entry<String, String[]> entry: set){
                    String key = entry.getKey();
                    String _key = key.substring(key.indexOf(StringUtils.PREFIX),key.indexOf(StringUtils.SUFFIX));
                    data.put(_key,entry.getValue());
                }
                System.out.println(data);
            }
        }
    }
}
