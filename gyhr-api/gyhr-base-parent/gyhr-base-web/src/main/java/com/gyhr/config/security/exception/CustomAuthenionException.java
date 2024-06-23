package com.gyhr.config.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 自定义异常
 */
public class CustomAuthenionException extends AuthenticationException {
    public CustomAuthenionException(String msg) {
        super(msg);
    }
}
