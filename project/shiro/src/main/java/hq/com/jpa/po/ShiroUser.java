package hq.com.jpa.po;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Describle: 安全中心-用户实体类
 * @Author: yinhaiquan
 * @Date: Created By 上午 10:31 2017/5/30 0030
 * @Modified By
 */
@Entity
@Table(name = "t_shiro_user")
public class ShiroUser implements Serializable {
    private static final long serialVersionUID = -7451224851560114939L;

    private int id;
    private String name;
    private String password;
    private int sex;
    private Date createTime;
    private String phoneNumber;
    private String email;
    private String adress;
    private int status;
    private String number;
    private Date birthday;

    public ShiroUser() {
    }

    public ShiroUser(int id, String name, String password, int sex,
                     Date createTime, String phoneNumber, String email,
                     String adress, int status, String number, Date birthday) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.sex = sex;
        this.createTime = createTime;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.adress = adress;
        this.status = status;
        this.number = number;
        this.birthday = birthday;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_id", unique = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "c_name", length = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "c_password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "n_sex")
    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "t_createtime")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "c_phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "c_email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "c_address")
    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    @Column(name = "n_status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "c_number")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "t_birthday")
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

}
