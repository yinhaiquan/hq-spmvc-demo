package hq.com.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * @Describle: 用户名/密码错误
 * @Author: YinHq
 * @Date: Created By 下午 1:58 2017/6/4 0004
 * @Modified By
 */
public class IncorrectCredentialsException extends AuthenticationException {
    public IncorrectCredentialsException() {
        super();
    }

    public IncorrectCredentialsException(String message) {
        super(message);
    }

    public IncorrectCredentialsException(Throwable cause) {
        super(cause);
    }

    public IncorrectCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
