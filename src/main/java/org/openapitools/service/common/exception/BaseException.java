//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.openapitools.service.common.exception;

public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    protected String code;

    public BaseException(String code, String message) {
        super("@" + code + "@" + message);
        this.code = code;
    }

    public BaseException(String code, String message, Throwable cause) {
        super("@" + code + "@" + message, cause);
        this.code = code;
    }

    public String getOriginalMessage() {
        return super.getMessage().substring(2 + this.code.length());
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
