package org.saladframework.dao;

/**
 * 不期望的异常
 *
 * @author LiuKeFeng
 * @date 2017年4月10日
 */
public class UnExpectedException extends RuntimeException {
    private static final long serialVersionUID = 6409066485217841653L;

    public UnExpectedException() {
        super();
    }

    public UnExpectedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UnExpectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnExpectedException(String message) {
        super(message);
    }

    public UnExpectedException(Throwable cause) {
        super(cause);
    }

}
