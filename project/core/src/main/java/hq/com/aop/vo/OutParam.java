package hq.com.aop.vo;

/**
 * @title : 出参
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/5/23 11:00 星期二
 */
public class OutParam implements Param {
    private int code;
    private String desc;
    private Object content;

    public OutParam() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Object encode() {
        return null;
    }
}
