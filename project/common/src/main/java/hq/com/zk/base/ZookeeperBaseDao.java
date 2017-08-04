package hq.com.zk.base;

import hq.com.aop.utils.StringUtils;
import org.apache.curator.utils.PathUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @title : zk基础包
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/7/10 18:03 星期一
 */
public class ZookeeperBaseDao {
    private static final String SEPARATOR = "/";
    private static final String REGEX = "[/]{1,}";

    /**
     * 格式化节点path
     *
     * @Describled: 转换path为zk的标准路径 以/开头,最后不带/
     * @param path
     * @return
     */
    public static String format(String path){
        if (StringUtils.isEmpty(path)){
            return SEPARATOR;
        }

        //单个字符"/"
        if (path.equals(SEPARATOR)){
            return path;
        }
        //包含多个字符"//////....."替换成"/"
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()){
            path = path.replaceAll(REGEX,SEPARATOR);
        }
        StringBuffer temp = new StringBuffer(path);
        //未以/开头
        if (!path.startsWith(SEPARATOR)){
            temp.insert(0,SEPARATOR);
        }
        //以/结尾
        if (path.endsWith(SEPARATOR)){
            return format(temp.toString().substring(0,temp.length()-1));
        }
        return temp.toString();
    }

    /**
     * 拼接两个节点path
     *
     * @Describled: 链接两个path,并转化为zk的标准路径
     * @param path1
     * @param path2
     * @return
     */
    public static String contact(String path1,String path2){
        if(path2.startsWith(SEPARATOR)) {
            path2 = path2.substring(1);
        }
        if(path1.endsWith(SEPARATOR)) {
            return format(path1 + path2);
        } else {
            return format(path1 + SEPARATOR + path2);
        }
    }
}
