package com.gyhr.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data   //自动生成 get & set 方法
@AllArgsConstructor //自动生成全参数构造器函数
public class ResultVo<T> {
    private String msg;
    private int code;
    private T Data;
}
