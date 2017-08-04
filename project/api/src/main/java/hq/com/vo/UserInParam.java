package hq.com.vo;

import hq.com.aop.annotation.Bean;
import hq.com.aop.annotation.Parameter;
import hq.com.aop.aopenum.ParamType;
import hq.com.aop.vo.InParam;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @title : 请求入参
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/5/23 11:01 星期二
 */
@Bean
@Component("userInfo")
public class UserInParam extends InParam implements Serializable {
    private static final long serialVersionUID = 1639274495198032640L;

    @Parameter(value = "name", type = ParamType.STRING, desc = "name")
    private String name;
    @Parameter(value = "age", type = ParamType.INT)
    private int age;
    @Parameter("desc")
    private String desc;

    public UserInParam() {
    }

    public UserInParam(String name, int age, String desc) {
        this.name = name;
        this.age = age;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public Object decode() {
        return super.decode();
    }
}
