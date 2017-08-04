package hq.com.aop.utils;

import java.math.BigDecimal;

/**
 * @title : 高精度计算工具类
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/6/29 16:08 星期四
 */
public class BigDecimalUtils {
    /*默认小数精确位数*/
    private static final int DEFAULT_PRECISION = 10;

    /**
     * 提供精确的加法运算
     * <p>
     * <b>note:(建议使用String类型，精确度更高!)</b>
     * 1. 若percision为-1，则不添加精确位数.
     * 2. 支持String、Double类型.
     * 3. 默认加法运算
     * </p>
     *
     * @param percision 小数精确位数
     * @param v_1       被运算数
     * @param v_n       运算数集合
     * @param type      运算类型 ADD 加法,SUBTRACT 减法,MULTIPLY 乘法,DIVIDE 除法;
     * @return
     */
    public final static BigDecimal op(int percision, BigDecimalType type, Object v_1, Object... v_n) {
        if (StringUtils.isEmpty(v_1) || StringUtils.isEmpty(v_n)) {
            throw new IllegalArgumentException("提供精确的加法运算参数为空!");
        }
        BigDecimal b_v_1 = checkInstance(v_1);
        if (percision < 0) {
            percision = DEFAULT_PRECISION;
        }
        switch (type) {
            case SUBTRACT:
                for (Object obj : v_n) {
                    b_v_1 = b_v_1.subtract(checkInstance(obj));
                }
                return b_v_1.setScale(percision, BigDecimal.ROUND_HALF_UP);
            case MULTIPLY:
                for (Object obj : v_n) {
                    b_v_1 = b_v_1.multiply(checkInstance(obj));
                }
                return b_v_1.setScale(percision, BigDecimal.ROUND_HALF_UP);
            case DIVIDE:
                for (Object obj : v_n) {
                    b_v_1 = b_v_1.divide(checkInstance(obj), percision, BigDecimal.ROUND_UP);
                    System.out.println(b_v_1.toString());
                }
                return b_v_1;
            default:
                for (Object obj : v_n) {
                    b_v_1 = b_v_1.add(checkInstance(obj));
                }
                return b_v_1.setScale(percision, BigDecimal.ROUND_HALF_UP);
        }
    }

    public static void main(String[] args) {
        BigDecimal b = op(10, BigDecimalType.MULTIPLY, "0.00494", "3.23");
        System.out.println(b.toString());
        System.out.println(b.doubleValue());
    }

    private static BigDecimal checkInstance(Object obj) {
        if (obj instanceof String) {
            return new BigDecimal(String.valueOf(obj));
        } else if (obj instanceof Double) {
            return new BigDecimal(Double.parseDouble(String.valueOf(obj)));
        } else {
            throw new IllegalArgumentException("提供精确的加法运算参数类型不合法!");
        }
    }

    /**
     * 运算类型枚举
     */
    public enum BigDecimalType {
        ADD, SUBTRACT, MULTIPLY, DIVIDE;
    }
}
