package hq.com.aop.utils;

import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @title : 字符串工具类
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/5/22 12:00 星期一
 */
public final class StringUtils {
    public final static String PREFIX = "[";
    public final static String SUFFIX = "]";
    public final static String DOLLAR = "$";
    public final static String SLASH = "/";
    public final static String BACKSLASH = "\\";
    public final static String POINT = ".";
    public final static String RAIL = "-";
    public final static String NULL = "";

    public final static boolean isEmpty(List list) {
        if (null == list || list.size() == 0) {
            return true;
        }
        return false;
    }

    public final static boolean isNotEmpty(List list) {
        if (null != list && list.size() != 0) {
            return true;
        }
        return false;
    }

    public final static boolean isEmpty(String str) {
        if (null == str || "".equals(str)) {
            return true;
        }
        return false;
    }

    public final static boolean isNotEmpty(String str) {
        if (null != str && str.length() > 0) {
            return true;
        }
        return false;
    }

    public final static boolean isEmpty(Set set) {
        if (null == set || (null != set && set.size() == 0)) {
            return true;
        }
        return false;
    }

    public final static boolean isNotEmpty(Set set) {
        if (null != set && set.size() > 0) {
            return true;
        }
        return false;
    }

    public final static boolean isEmpty(Map map) {
        if (null == map || (null != map && map.size() == 0)) {
            return true;
        }
        return false;
    }

    public final static boolean isNotEmpty(Map map) {
        if (null != map && map.size() > 0) {
            return true;
        }
        return false;
    }

    public final static boolean isEmpty(Object obj) {
        if (null == obj) {
            return true;
        }
        return false;
    }

    public final static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public final static boolean isEmpty(Object[] obj) {
        if (null == obj || (null != obj && obj.length == 0)) {
            return true;
        }
        return false;
    }

    public final static boolean isNotEmpty(Object[] obj) {
        if (null != obj && obj.length > 0) {
            return true;
        }
        return false;
    }

    /**
     * <p>
     * str'reg started with {0} eg: {0}{1}{2}......
     * </p>
     *
     * @param str
     * @param args
     * @return
     */
    public final static String format(String str, Object... args) {
        if (isNotEmpty(str)) {
            MessageFormat mf = new MessageFormat(str);
            return mf.format(args);
        }
        return null;
    }

    /**
     * 截取分隔符之前字符
     * @param str 字符串
     * @param split 分隔符
     * @return
     */
    public final static String subStart(String str, String split) {
        if (isNotEmpty(str) && isNotEmpty(split)) {
            int i = str.indexOf(split);
            if (i>0){
                return str.substring(0, i);
            }
        }
        return str;
    }

    /**
     * 截取分隔符之后字符
     * @param str
     * @param split
     * @return
     */
    public final static String subEnd(String str, String split){
        if (isNotEmpty(str) && isNotEmpty(split)) {
            int i = str.lastIndexOf(split);
            if (i>0){
                return str.substring(i);
            }
        }
        return str;
    }

    public static void main(String[] args) {
        String str = "sdfpfd";
        System.out.println(subStart(str,"."));
        System.out.println(subEnd(str,"."));
//        String str = "root[ms2g][userinfo][name]";
//        String str2 = "root[msg][name]";
//        String regEx = "root\\[msg]\\[";
//        Pattern pattern = Pattern.compile(regEx);
//        Matcher matcher = pattern.matcher(str);
//        System.out.println(matcher.find());
//        System.out.println(str.replaceFirst(regEx, "").replaceFirst(SUFFIX, ""));
//        String str = "s1ij234kkajsdklf$$sdfkjsdklfj$$";
//        System.out.println(substring(str,"$"));
//        Map<String,String[]> params = new HashMap<String, String[]>();
//        params.put("123",new String[]{"sdf"});
//        for (Map.Entry<String, String[]> map:params.entrySet()){
//            System.out.println(map.getValue()[0]);
//        }
//        System.out.println(isNotEmpty(params));
//        String str = "123{0}-{1}:{2}:{3}";
//        System.out.println(StringUtils.isNotEmpty(str));
//        Object [] obj = new Object[]{12,123,23,"sdf"};
//        System.out.println(format(str,obj));
//        System.out.println(format(str,"123","yyy",999,new Date()));
    }

}
