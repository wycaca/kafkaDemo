package com.citi.cbk.exception;

/**
 * Created by fgm on 2017/4/22.
 */
public class FreeMarkerException extends BaseException {
    private static final long serialVersionUID = 5382833931116911740L;

    public FreeMarkerException() {
        super("FreeMarker Exception");
    }

    public FreeMarkerException(int errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public FreeMarkerException(String errorMsg) {
        super(errorMsg);
        this.errorCode = 500;
        this.errorMsg = errorMsg;
    }

    public FreeMarkerException(String errorMsg, Exception e) {
        super(errorMsg, e);
        this.errorCode = 500;
        this.errorMsg = errorMsg;
    }
}
