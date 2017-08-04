package hq.com.jpa.dao.impl;

import hq.com.aop.utils.StringUtils;
import hq.com.base.BaseJpaDaoSupport;
import hq.com.base.Pager;
import hq.com.jpa.dao.ShiroUserDaoJpa;
import hq.com.jpa.po.ShiroUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Describle:
 * @Author: YinHq
 * @Date: Created By 上午 11:39 2017/5/30 0030
 * @Modified By
 */
public class ShiroUserDaoJpaImpl extends BaseJpaDaoSupport<ShiroUser, Integer> implements ShiroUserDaoJpa {

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
    @Override
    public Pager selectShiroUserList(Map<String, Object> paramMap, Pager pager) {
        StringBuffer mainSql = new StringBuffer();
        List<Object> paramList = new ArrayList<Object>();
        mainSql.append("from ShiroUser obj where 1=1");
        if (null != paramMap) {
            String name = (String) paramMap.get("name");
            String phoneNumber = (String) paramMap.get("phoneNumber");
            String number = (String) paramMap.get("number");
            if (StringUtils.isNotEmpty(name)) {
                mainSql.append(" and obj.name =?");
                paramList.add(name);
            }
            if (StringUtils.isNotEmpty(phoneNumber)) {
                mainSql.append(" and obj.phoneNumber =?");
                paramList.add(phoneNumber);
            }
            if (StringUtils.isNotEmpty(number)) {
                mainSql.append(" and obj.number =?");
                paramList.add(number);
            }
        }
        mainSql.append(" order by obj." + pager.getSort() + " " + pager.getOrder());
        return this.executePageableQuery(mainSql, paramList, pager.getPage(), pager.getPageSize());
    }
}
