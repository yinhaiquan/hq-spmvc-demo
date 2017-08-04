package hq.com.jpa.dao;

import hq.com.base.Pager;

import java.util.Map;

/**
 * @Describle:
 * @Author: YinHq
 * @Date: Created By 下午 12:38 2017/5/30 0030
 * @Modified By
 */
public interface ShiroUserDaoJpa {
    /**
     * 分页查询用户信息
     *
     * @param paramMap 查询参数：
     *                 name 用户名称
     *                 phoneNumber 用户手机
     *                 number 用户编号
     * @param pager
     * @return
     */
    public Pager selectShiroUserList(Map<String, Object> paramMap, Pager pager);
}
