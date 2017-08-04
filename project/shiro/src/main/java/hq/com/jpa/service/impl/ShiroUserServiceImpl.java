package hq.com.jpa.service.impl;

import hq.com.aop.annotation.Log;
import hq.com.aop.exception.IllegalArgumentsException;
import hq.com.aop.utils.StringUtils;
import hq.com.aop.vo.OutParam;
import hq.com.base.Pager;
import hq.com.enums.SystemCodeEnum;
import hq.com.exception.IllegalOptionException;
import hq.com.jpa.dao.ShiroUserDao;
import hq.com.jpa.dto.ShiroUserDto;
import hq.com.jpa.po.ShiroUser;
import hq.com.jpa.service.ShiroUserService;
import hq.com.jpa.vo.ShiroUserInParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Describle:
 * @Author: YinHq
 * @Date: Created By 下午 1:31 2017/5/30 0030
 * @Modified By
 */
@Service("shiroUserSvc")
public class ShiroUserServiceImpl implements ShiroUserService {

    @Resource
    private ShiroUserDao shiroUserDao;

    @Log(desc = "用户编码/用户密码：${0}")
    @Override
    public OutParam findUserInfo(ShiroUserInParam shiroUserInParam) throws IllegalOptionException {
        try {
            ShiroUser user = shiroUserDao.findUserInfo(shiroUserInParam.getNumber(), shiroUserInParam.getPassword());
            ShiroUserDto sud = null;
            if (StringUtils.isNotEmpty(user)) {
                sud = new ShiroUserDto();
                sud.setName(user.getName());
                sud.setPhoneNumber(user.getPhoneNumber());
                sud.setStatus(user.getStatus());
            }
            OutParam op = new OutParam();
            op.setDesc(SystemCodeEnum.SYSTEM_OK.getDesc());
            op.setCode(SystemCodeEnum.SYSTEM_OK.getCode());
            op.setContent(sud);
            return op;
        } catch (Exception e) {
            throw new IllegalOptionException(SystemCodeEnum.SYSTEM_ERROR);
        }
    }

    @Log(desc = "用户编码${0}-用户密码${1}")
    @Override
    public ShiroUserDto findUserInfo(String number, String password) throws IllegalArgumentsException {
        try {
            ShiroUser user = shiroUserDao.findUserInfo(number, password);
            ShiroUserDto sud = null;
            if (StringUtils.isNotEmpty(user)) {
                sud = new ShiroUserDto();
                sud.setName(user.getName());
                sud.setPhoneNumber(user.getPhoneNumber());
                sud.setStatus(user.getStatus());
            }
            return sud;
        } catch (Exception e) {
            throw new IllegalArgumentsException(e.getMessage(), e);
        }
    }

    @Override
    public ShiroUserDto findUserInfo(String number) throws IllegalArgumentsException {
        try {
            ShiroUser user = shiroUserDao.findUserInfo(number);
            ShiroUserDto sud = null;
            if (StringUtils.isNotEmpty(user)) {
                sud = new ShiroUserDto();
                sud.setName(user.getName());
                sud.setPhoneNumber(user.getPhoneNumber());
                sud.setStatus(user.getStatus());
            }
            return sud;
        } catch (Exception e) {
            throw new IllegalArgumentsException(e.getMessage(), e);
        }
    }

    @Transactional(value = "jpaTransactionManager")
    @Override
    public void updateUserStatus(int status, int id) throws IllegalArgumentsException {

    }

    @Override
    public Pager selectShiroUserList(Map<String, Object> paramMap, Pager pager) throws IllegalArgumentsException {
        return null;
    }
}
