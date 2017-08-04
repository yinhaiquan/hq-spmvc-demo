package hq.com.redis.dao.demo;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import hq.com.aop.utils.JprotobufUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 * @title : 测试序列化对象存储redis
 * @describle :
 * <p>
 * <b>note:</b>
 * 测试结果：1. protobuf序列化后大小是Serializable序列化后的1/5~1/4
 * 2. 节省空间，且效率高于Serializable序列化。
 * 3. protobuf数据类型中不支持Date类型，故需要转成String类型存储
 * </p>
 * Create By yinhaiquan
 * @date 2017/7/4 11:22 星期二
 */
@Deprecated
public class Test2 {
    @Protobuf(fieldType = FieldType.INT32, order = 1, required = true)
    private int age;
    @Protobuf(fieldType = FieldType.STRING, order = 2, required = true)
    private String name;
    @Protobuf(fieldType = FieldType.STRING, order = 3, required = true)
    private String desc;

    public Test2() {
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

    public static void main(String[] args) throws IOException {
        Test2 test1 = new Test2();
        test1.setAge(12);
        test1.setDesc("sdfsdfsdfsdf是打发斯蒂芬");
        test1.setName("123sldkfjs234");
        System.out.println(JprotobufUtils.getInstance(Test2.class).serialization(test1).length);
    }
}
