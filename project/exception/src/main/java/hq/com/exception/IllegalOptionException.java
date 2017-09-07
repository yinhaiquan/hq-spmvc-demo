package hq.com.exception;

import hq.com.enums.SystemCodeEnum;

import java.security.PrivilegedActionException;

/**
 * @title : 全局系统异常
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/7/11 16:02 星期二
 */
public class IllegalOptionException extends Exception {
    private SystemCodeEnum s_ = SystemCodeEnum.SYSTEM_OK;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public IllegalOptionException(SystemCodeEnum s_) {
        this.s_ = s_;
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public IllegalOptionException(String message, SystemCodeEnum s_) {
        super(message);
        this.s_ = s_;
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     * @since 1.4
     */
    public IllegalOptionException(String message, Throwable cause, SystemCodeEnum s_) {
        super(message, cause);
        this.s_ = s_;
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * PrivilegedActionException}).
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A <tt>null</tt> value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     * @since 1.4
     */
    public IllegalOptionException(Throwable cause, SystemCodeEnum s_) {
        super(cause);
        this.s_ = s_;
    }

    /**
     * Constructs a new exception with the specified detail message,
     * cause, suppression enabled or disabled, and writable stack
     * trace enabled or disabled.
     *
     * @param message            the detail message.
     * @param cause              the cause.  (A {@code null} value is permitted,
     *                           and indicates that the cause is nonexistent or unknown.)
     * @param enableSuppression  whether or not suppression is enabled
     *                           or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     * @since 1.7
     */
    public IllegalOptionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, SystemCodeEnum s_) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.s_ = s_;
    }

    public SystemCodeEnum getS_() {
        return s_;
    }

    public void setS_(SystemCodeEnum s_) {
        this.s_ = s_;
    }
}
