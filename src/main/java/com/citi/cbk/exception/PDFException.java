package com.citi.cbk.exception;

/**
 * Created by fgm on 2017/4/22.
 */
public class PDFException extends BaseException {

    private static final long serialVersionUID = 4060881054566004037L;

    public PDFException() {
        super("PDF Exception");
    }

    public PDFException(int errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public PDFException(String errorMsg) {
        super(errorMsg);
        this.errorCode = 500;
        this.errorMsg = errorMsg;
    }

    public PDFException(String errorMsg, Exception e) {
        super(errorMsg, e);
        this.errorCode = 500;
        this.errorMsg = errorMsg;
    }
}
