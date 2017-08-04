package hq.com.jpa.dto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Describle: 安全中心-用户实体类
 * @Author: yinhaiquan
 * @Date: Created By 上午 10:31 2017/5/30 0030
 * @Modified By
 */
public class ShiroUserDto implements Serializable {
    private static final long serialVersionUID = -7451224851560114939L;

    private int id;
    private String name;
    private int sex;
    private Date createTime;
    private String phoneNumber;
    private String email;
    private String adress;
    private int status;
    private String number;
    private Date birthday;

    public ShiroUserDto() {
    }

    public ShiroUserDto(int id, String name, int sex,
                        Date createTime, String phoneNumber, String email,
                        String adress, int status, String number, Date birthday) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.createTime = createTime;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.adress = adress;
        this.status = status;
        this.number = number;
        this.birthday = birthday;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "{" +
                "id:" + id +
                ", name:'" + name + '\'' +
                ", sex:" + sex +
                ", createTime:" + createTime +
                ", phoneNumber:'" + phoneNumber + '\'' +
                ", email:'" + email + '\'' +
                ", adress:'" + adress + '\'' +
                ", status:" + status +
                ", number:'" + number + '\'' +
                ", birthday:" + birthday +
                '}';
    }

}
