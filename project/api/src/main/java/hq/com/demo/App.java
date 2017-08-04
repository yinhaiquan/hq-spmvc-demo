package hq.com.demo;


import hq.com.aop.annotation.Log;
import hq.com.vo.UserInParam;
import org.springframework.stereotype.Component;

/**
 * Hello world!
 */
@Component(value = "appSvc")
public class App {
    public void say(String str, String sdf) {
        System.out.println(str + "%%%%%" + sdf);
    }

    @Log(desc = "str:{0}")
    public String say(String str) {
        return "hello" + str;
    }

    @Log
    public String say() {
        return "fuck";
    }

    public void say(String name, int age) {
        System.out.println(name + "  " + age);
    }

    public void say(UserInParam userInDto) {
        System.out.println(userInDto.getName() + "***" + userInDto.getAge() + "***" + userInDto.getDesc());
    }

}
