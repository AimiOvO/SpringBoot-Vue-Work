package com.gyhr.web.system.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginParm implements Serializable {
    private String username;
    private String password;
    private String userType;

}
