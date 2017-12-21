package jmx;

/**
 * @title :
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/12/14 13:43 星期四
 */
public class Hello implements HelloMBean {
    private String name = "jim";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void hello(){
        System.out.println(name);
    }

    @Override
    public void printStr() {
        System.out.println("hello,"+name);
    }

    @Override
    public String getFFFFFUCK() {
        return name;
    }
    public void showName(String message) {
        System.out.println(message);
    }

    @Override
    public String show() {
        System.out.println(name);
        return name;
    }
}
