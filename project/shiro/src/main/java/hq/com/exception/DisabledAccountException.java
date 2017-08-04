package hq.com.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * @Describle: 帐号已被禁用
 * @Author: YinHq
 * @Date: Created By 下午 2:14 2017/6/4 0004
 * @Modified By
 */
public class DisabledAccountException extends AuthenticationException {
    public DisabledAccountException() {
        super();
    }

    public DisabledAccountException(String message) {
        super(message);
    }

    public DisabledAccountException(Throwable cause) {
        super(cause);
    }

    public DisabledAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
