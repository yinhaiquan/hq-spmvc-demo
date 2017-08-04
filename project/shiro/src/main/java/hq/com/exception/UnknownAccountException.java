package hq.com.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * @Describle: 帐号不存在
 * @Author: YinHq
 * @Date: Created By 下午 2:16 2017/6/4 0004
 * @Modified By
 */
public class UnknownAccountException extends AuthenticationException {
    public UnknownAccountException() {
        super();
    }

    public UnknownAccountException(String message) {
        super(message);
    }

    public UnknownAccountException(Throwable cause) {
        super(cause);
    }

    public UnknownAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
