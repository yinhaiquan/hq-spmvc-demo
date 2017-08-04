package hq.com.redis.dao.demo;

import java.io.Serializable;

/**
 * @title : 测试序列化对象存储redis
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/7/4 11:22 星期二
 */
@Deprecated
public class Test1 implements Serializable {
    private int age;
    private String name;
    private String desc;

    public Test1() {
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
