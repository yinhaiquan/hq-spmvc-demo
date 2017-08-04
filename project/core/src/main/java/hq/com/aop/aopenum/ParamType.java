package hq.com.aop.aopenum;

/**
 * @title :
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/5/24 16:16 星期三
 */
public enum ParamType {
    STRING(String.class),
    INT(Integer.class),
    LONG(Long.class),
    DOUBLE(Double.class),
    FLOAT(Float.class);
    private Class cls;

    private ParamType(Class cls) {
        this.cls = cls;
    }

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }
}
