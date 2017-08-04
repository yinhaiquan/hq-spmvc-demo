package hq.com.jpa.dao;

import hq.com.base.BaseDao;
import hq.com.jpa.po.ShiroUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @Describle:
 * @Author: YinHq
 * @Date: Created By 下午 12:37 2017/5/30 0030
 * @Modified By
 */
public interface ShiroUserDao extends BaseDao<ShiroUser, Integer>, ShiroUserDaoJpa {
    /**
     * 根据用户编号和密码查询用户信息
     *
     * @param number   用户编号
     * @param password 登录密码
     * @return
     */
    @Query("from ShiroUser where number=:nb and password=:pd")
    public ShiroUser findUserInfo(@Param("nb") String number, @Param("pd") String password);

    @Query("from ShiroUser where number=:nb")
    public ShiroUser findUserInfo(@Param("nb") String number);

    @Modifying
    @Query("update ShiroUser set status=? where id=?")
    public void updateUserStatus(int status, int id);
}
