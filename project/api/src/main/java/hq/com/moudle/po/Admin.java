package hq.com.moudle.po;

import java.util.Date;

/**
 * @Describle:
 * @Author: YinHq
 * @Date: Created By 下午 8:56 2017/5/29 0029
 * @Modified By
 */
public class Admin {
    private int id;
    private String name;
    private String password;
    private String sex;
    private Date time;

    public Admin() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", time=" + time +
                '}';
    }
}
