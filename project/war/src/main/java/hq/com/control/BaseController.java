package hq.com.control;

import hq.com.aop.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
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

    public abstract Object decodeRequest(HttpServletRequest request);

    public abstract Object decodeRequest(Map<String, Map<String, Object>> map);

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
}
