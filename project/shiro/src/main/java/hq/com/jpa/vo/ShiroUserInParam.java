package hq.com.jpa.vo;

import hq.com.aop.annotation.*;
import hq.com.aop.annotation.Parameter;
import hq.com.aop.aopenum.ParamType;
import hq.com.aop.vo.InParam;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @Describle: 安全中心-用户实体类-入参
 * @Author: yinhaiquan
 * @Date: Created By 上午 10:31 2017/5/30 0030
 * @Modified By
 */
@Bean
@Component("shirouserInfo")
public class ShiroUserInParam extends InParam implements Serializable {
    @Parameter(value = "id", type = ParamType.INT, desc = "id")
    private int id;
    @Parameter(value = "name", type = ParamType.STRING, desc = "name")
    private String name;
    @Parameter(value = "password", type = ParamType.STRING, desc = "password")
    private String password;
    @Parameter(value = "sex", type = ParamType.INT, desc = "sex")
    private int sex;
    @Parameter(value = "phoneNumber", type = ParamType.STRING, desc = "phoneNumber")
    private String phoneNumber;
    @Parameter(value = "email", type = ParamType.STRING, desc = "email")
    private String email;
    @Parameter(value = "adress", type = ParamType.STRING, desc = "adress")
    private String adress;
    @Parameter(value = "status", type = ParamType.INT, desc = "status")
    private int status;
    @Parameter(value = "number", type = ParamType.STRING, desc = "number")
    private String number;

    public ShiroUserInParam() {
    }

    public ShiroUserInParam(int id, String name, String password, int sex, String phoneNumber, String email,
                            String adress, int status, String number) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.adress = adress;
        this.status = status;
        this.number = number;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "ShiroUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", sex=" + sex +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", adress='" + adress + '\'' +
                ", status=" + status +
                ", number='" + number + '\'' +
                '}';
    }
}
