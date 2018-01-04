package hq.com.realm;

import hq.com.aop.exception.IllegalArgumentsException;
import hq.com.aop.utils.StringUtils;
import hq.com.jpa.dto.ShiroUserDto;
import hq.com.enums.UserStatus;
import hq.com.exception.UnknownAccountException;
import hq.com.exception.DisabledAccountException;
import hq.com.exception.IncorrectCredentialsException;
import hq.com.jpa.service.ShiroUserService;
import hq.com.session.RedisSessionDao;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * @Describle:
 * @Author: YinHq
 * @Date: Created By 下午 3:51 2017/5/29 0029
 * @Modified By
 */
public class AuthoriztionHandlerRealm extends AuthorizingRealm {
    public static Logger log = LoggerFactory.getLogger(AuthoriztionHandlerRealm.class);
    @Resource
    private ShiroUserService shiroUserService;

    @Resource(name = "sessionDAO")
    private RedisSessionDao sessionDao;

    @Value("${shiro.kicking.switch}")
    private boolean kickingSwitch = false;

    /**
     * 授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRole("fuck");
        return info;
    }

    /**
     * 认证
     *
     * 获取缓存session列表，并获取session中缓存用户对象信息
     * Collection<Session> sessions = redisSessionDAO.getActiveSessions();
     * for (Session session : sessions) {
     * if (null==session || null==session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)){
     * continue;
     * }
     * SimplePrincipalCollection su = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
     * SecUser sssu = (SecUser) su.getPrimaryPrincipal();
     * logger.info(String.format("被冻结用户[%s]----遍历已登录session用户信息[%s]",secUser.getLoginName(),sssu.getLoginName()));
     * if (secUser.getLoginName().equals(sssu.getLoginName())) {
     * logger.info(String.format("用户[%s]登录session信息已被清除",secUser.getLoginName()));
     * redisSessionDAO.delete(session);
     * break;
     * }
     * }
     *
     *
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取令牌信息
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //踢人开关 默认关闭
        log.info("踢人开关：true开启false关闭:{}", kickingSwitch);
        if (kickingSwitch) {
            //单点登录(踢人)
            Collection<Session> sessions = sessionDao.getActiveSessions();
            for (Session session : sessions) {
                if (token.getUsername().equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {
                    sessionDao.delete(session);
                    break;
                }
            }
        }
        ShiroUserDto shiroUserDto = null;
        try {
            //判断用户合法性
            ShiroUserDto userDto = shiroUserService.findUserInfo(token.getUsername());
            if (StringUtils.isEmpty(userDto)) {
                this.assertCredentialsMatch(token, null);
                throw new UnknownAccountException("账号不存在");
            } else {
                //判断用户名/密码是否正确
                shiroUserDto = shiroUserService.findUserInfo(token.getUsername(), new String(token.getPassword()));
            }
        } catch (IllegalArgumentsException e) {
            throw new AuthenticationException(e);
        }
        if (StringUtils.isNotEmpty(shiroUserDto)) {
            if (shiroUserDto.getStatus() == UserStatus.EFFECTIVE.getCode()) {
                SecurityUtils.getSubject().getSession().setAttribute("user", shiroUserDto);
                AuthenticationInfo info = new SimpleAuthenticationInfo(token.getUsername(), token.getPassword(), getName());
                this.assertCredentialsMatch(token, info);
                return info;
            } else {
                this.assertCredentialsMatch(token, null);
                throw new DisabledAccountException("帐号已被禁用");
            }
        } else {
            this.assertCredentialsMatch(token, null);
            throw new IncorrectCredentialsException("用户名/密码错误!");
        }
    }
}
