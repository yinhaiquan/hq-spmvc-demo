package hq.com.auth.vo;

import hq.com.aop.annotation.Bean;
import hq.com.aop.annotation.Parameter;
import hq.com.aop.aopenum.ParamType;
import hq.com.aop.vo.InParam;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @title : 接口权限配置入参映射实体类
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/9/9 11:34 星期六
 */
@Bean
@Component("interfaceAuth")
public class InterfaceAuthInParam extends InParam implements Serializable {
    private static final long serialVersionUID = 8895638583228271487L;
    @Parameter(value = "id", type = ParamType.INT, desc = "接口权限配置ID")
    private int id;
    @Parameter(value = "name", type = ParamType.STRING, desc = "类名/方法名")
    private String name;
    @Parameter(value = "startDate", type = ParamType.STRING, desc = "开始时间")
    private String startDate;
    @Parameter(value = "enDate", type = ParamType.STRING, desc = "结束时间")
    private String enDate;
    @Parameter(value = "tag", type = ParamType.INT, desc = "验签/令牌开关值")
    private int tag;
    @Parameter(value = "type", type = ParamType.INT, desc = "验签/令牌")
    private int type;

    public InterfaceAuthInParam() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEnDate() {
        return enDate;
    }

    public void setEnDate(String enDate) {
        this.enDate = enDate;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "InterfaceAuthInParam{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startDate='" + startDate + '\'' +
                ", enDate='" + enDate + '\'' +
                ", tag=" + tag +
                ", type=" + type +
                '}';
    }
}
