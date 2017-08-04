package hq.com.aop.aopenum;

/**
 * Created by yinhaiquan on 2017/5/20.
 */
public enum Tag {
    YES(true),
    NO(false);

    private boolean tag;

    private Tag(boolean tag) {
        this.tag = tag;
    }

    public boolean isTag() {
        return tag;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }
}
