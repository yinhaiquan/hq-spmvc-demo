package hq.com.aop.utils;

/**
 * @title :
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/5/23 15:35 星期二
 */
public class KeyObject {
    private String key;
    private Class tpye;
    private boolean isString;

    public KeyObject() {
    }

    public KeyObject(String key, boolean isString, Class tpye) {
        this.key = key;
        this.isString = isString;
        this.tpye = tpye;
    }

    public Class getTpye() {
        return tpye;
    }

    public void setTpye(Class tpye) {
        this.tpye = tpye;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isString() {
        return isString;
    }

    public void setString(boolean string) {
        isString = string;
    }

    @Override
    public String toString() {
        return "KeyObject{" +
                "key='" + key + '\'' +
                ", tpye=" + tpye +
                ", isString=" + isString +
                '}';
    }
}
