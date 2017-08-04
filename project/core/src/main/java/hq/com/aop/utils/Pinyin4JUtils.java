package hq.com.aop.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * @title : 汉字-拼音工具类
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/6/16 11:36 星期五
 */
public class Pinyin4JUtils {

    /**
     * 获取字符串拼音首字母大写
     * <p>
     * <b>note:</b>
     * 规则： 1. 汉字取首字母大写
     * 2. 数字/字母取大写
     * 3. 其他特殊字符过滤不处理
     * </p>
     *
     * @param chword
     * @return
     */
    public static String getFirstPinyin(String chword) {
        char[] chars = chword.trim().toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        StringBuffer sb = new StringBuffer();
        try {
            for (int i = 0; i < chars.length; i++) {
                String str = String.valueOf(chars[i]);
                // 如果字符是中文,则将中文转为汉语拼音,并取第一个字母
                if (str.matches("[\\u4e00-\\u9fa5]+")) {
                    sb.append(PinyinHelper.toHanyuPinyinStringArray(chars[i], defaultFormat)[0].substring(0, 1));
                    continue;
                }
                // 如果字符是数字,取数字
                if (str.matches("[0-9]+")) {
                    sb.append(chars[i]);
                    continue;
                }
                // 如果字符是字母,取字母
                if (str.matches("[a-zA-Z]+")) {
                    sb.append(chars[i]);
                    continue;
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }
        return StringUtils.isNotEmpty(sb) ? sb.toString().toUpperCase() : null;
    }

    public static void main(String[] args) {
        System.out.println(getFirstPinyin("123skdjfhjk"));
        System.out.println(getFirstPinyin("银海区安"));
        System.out.println(getFirstPinyin("123sk呵呵你妹djfhjk"));
        System.out.println(getFirstPinyin("123skdjfhj109=-=-。。。，，，、、、~！@@#%@%爽肤水k"));
    }
}
