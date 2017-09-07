package hq.com.control;

import hq.com.aop.ctx.SpringApplicationContext;
import hq.com.aop.exception.IllegalArgumentsException;
import hq.com.aop.handler.ClassMethodHandler;
import hq.com.aop.utils.ClassObject;
import hq.com.aop.utils.RSACoderUtils;
import hq.com.aop.utils.StringUtils;
import hq.com.exception.IllegalOptionException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @title : 基础控制层
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
    /*公钥*/
    private final static String PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC3Qz0NA1KkwvAW7Hu0cKVgOTdi\n" +
            "AdFIEVkWBLvyfZmsCoqky/urTS4jv3l0aruZtJ0y1pSI8fu8R/NcKE+2Aa7Ku6RH\n" +
            "KfbMFLa6rNqZj7838JdHbtasGvXep+SoFYJGDI+E2wmGEip5veA4Od64aidbjVbt\n" +
            "tA8BsBdoA22cS9u4zQIDAQAB";

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
     *
     * @description: 1. 国际化信息处理
     *               2. 安全权限控制 a. 签名验签  b. token验证
     * @param params       数据
     * @param className    类全名/bean名称
     * @param methodName   方法名
     * @param route        路由信息
     */
    public void routeHandler(String className, String methodName, Map<String, String[]> route, Map<String, String[]> params) throws IllegalOptionException, IllegalArgumentsException {
        if (StringUtils.isNotEmpty(route)) {
            /**
             * 1. 国际化信息也在此处理
             */
            String[] lgs = route.get("lang");
            SpringApplicationContext.setLOCALE(StringUtils.isEmpty(lgs) ? "zh_CN" : lgs[0]);
            /**
             * 2. 签名处理
             *
             * @decription: a. 临时处理:通过前端传auth[sign]签名字段判断是否需要签名(此方法存在安全隐患)
             *              b. 安全处理:通过服务端配置接口权限，将权限加载至内存，通过方法名找到对应接口权限判断是否需要签名
             *                          接口权限内存存储结构：
             *                          map{
             *                              className+Method ：auth{
             *                                                     iSign:true
             *                                                     iLogin:false
             *                                                 }
             *                          }
             */
            /*---------------------安全处理-------------------------*/
            //查询内存中接口权限
            //           1. 判断是否需要签名验签
            //           2. 判断是否需要token验证
            /*---------------------安全处理-------------------------*/
//            verifySignature(route,params);
        }
    }

    /**
     * 验签
     *
     * @param route 路由信息
     * @param params   数据
     */
    private void verifySignature(Map<String, String[]> route, Map<String, String[]> params) throws IllegalArgumentsException {
        String[] routeAuths = route.get("auth[sign]");
        if (StringUtils.isNotEmpty(routeAuths)) {
            String sign = routeAuths[0];
            Map<String, Object> data = null;
            try {
                data = new HashMap<>();
                Set<Map.Entry<String, String[]>> set = params.entrySet();
                for (Map.Entry<String, String[]> entry : set) {
                    String key = entry.getKey();
                    String _key = key.substring(key.indexOf(StringUtils.PREFIX), key.indexOf(StringUtils.SUFFIX));
                    data.put(_key, entry.getValue());
                }
            } catch (Exception e) {
                throw new IllegalArgumentsException(SpringApplicationContext.getMessage("exception.auth.title"),SpringApplicationContext.getMessage("exception.auth.params"));
            }
            String sign_data = RSACoderUtils.formatParameter(data);
            try {
                boolean iSign = RSACoderUtils.verify(sign_data,PUBLICKEY,sign);
                if (!iSign){
                    throw new IllegalArgumentsException(SpringApplicationContext.getMessage("exception.auth.title"),SpringApplicationContext.getMessage("exception.auth.verify"));
                }
            } catch (Exception e) {
                throw new IllegalArgumentsException(SpringApplicationContext.getMessage("exception.auth.title"),e.getMessage());
            }
        }else{
            throw new IllegalArgumentsException(SpringApplicationContext.getMessage("exception.auth.title"),SpringApplicationContext.getMessage("exception.auth.illegal"));
        }
    }
}
