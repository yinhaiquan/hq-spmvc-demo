package hq.com.aop.utils;

import java.util.Arrays;

/**
 * @title :
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/5/23 14:14 星期二
 */
public class ClassObject {

    private Class[] cls;
    private Object[] objs;

    public ClassObject() {
    }

    public ClassObject(Class[] cls, Object[] objs) {
        this.cls = cls;
        this.objs = objs;
    }

    public Class[] getCls() {
        return cls;
    }

    public void setCls(Class[] cls) {
        this.cls = cls;
    }

    public Object[] getObjs() {
        return objs;
    }

    public void setObjs(Object[] objs) {
        this.objs = objs;
    }

    @Override
    public String toString() {
        return "ClassObject{" +
                "cls=" + Arrays.toString(cls) +
                ", objs=" + Arrays.toString(objs) +
                '}';
    }
}
