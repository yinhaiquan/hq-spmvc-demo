package hq.com.jpa.service;

import hq.com.aop.exception.IllegalArgumentsException;
import hq.com.aop.vo.OutParam;
import hq.com.base.Pager;
import hq.com.exception.IllegalOptionException;
import hq.com.jpa.dto.ShiroUserDto;
import hq.com.jpa.vo.ShiroUserInParam;

import java.util.Map;

/**
 * @Describle:
 * @Author: YinHq
 * @Date: Created By 下午 1:27 2017/5/30 0030
 * @Modified By
 */
public interface ShiroUserService {
    public OutParam findUserInfo(ShiroUserInParam shiroUserInParam) throws IllegalOptionException;

    public ShiroUserDto findUserInfo(String number, String password) throws IllegalArgumentsException;

    public ShiroUserDto findUserInfo(String number) throws IllegalArgumentsException;

    public void updateUserStatus(int status, int id) throws IllegalArgumentsException;

    public Pager selectShiroUserList(Map<String, Object> paramMap, Pager pager) throws IllegalArgumentsException;
}
