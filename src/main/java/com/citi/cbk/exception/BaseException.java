package com.citi.cbk.exception;

/**
 * Created by fgm on 2017/4/22.
 */
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 4560681515935716490L;
    public int errorCode;
    public String errorMsg;

    public BaseException() {
        super("runtime exception");
    }

    public BaseException(int errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BaseException(String errorMsg) {
        super(errorMsg);
        this.errorCode = 500;
        this.errorMsg = errorMsg;
    }

    public BaseException(String errorMsg, Exception e) {
        super(errorMsg, e);
        this.errorCode = 500;
        this.errorMsg = errorMsg;
    }
}
