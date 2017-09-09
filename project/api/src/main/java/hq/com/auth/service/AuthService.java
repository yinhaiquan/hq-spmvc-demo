package hq.com.auth.service;

import hq.com.aop.annotation.Log;
import hq.com.aop.vo.OutParam;
import hq.com.auth.vo.InterfaceAuthInParam;
import hq.com.exception.IllegalOptionException;

/**
 * @title : 接口权限安全管理业务层
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/9/9 13:57 星期六
 */
public interface AuthService {

    /**
     * 分页查询接口权限配置列表
     *
     * @param interfaceAuthInParam 入参
     * @return
     */
    @Log(desc = "分页查询接口权限配置列表参数:{0}")
    public OutParam findInterfaceAuthList(InterfaceAuthInParam interfaceAuthInParam) throws IllegalOptionException;

    /**
     * 更新验签权限/令牌权限
     *
     * @param interfaceAuthInParam
     * @return
     */
    @Log(desc = "更新验签权限/令牌权限参数:{0}")
    public OutParam updateAuth(InterfaceAuthInParam interfaceAuthInParam) throws IllegalOptionException;
}
