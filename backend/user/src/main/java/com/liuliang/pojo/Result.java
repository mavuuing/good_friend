package com.liuliang.pojo;


import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {

    private Integer code; //编码：1成功，0和其它数字为失败
    private String msg; //错误信息
    private T data; //数据

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = 1;
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.code = 1;
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = 0;
        return result;
    }

    public static Result<?> fail(int i, String msg) {
        Result<?> result = new Result<>();
        result.msg = msg;
        result.code = i;
        return result;
    }

    public static Result<?> success(Object messageId, String msg) {
        Result<?> result = new Result<>();
        result.msg = msg;
//        获取消息id
        result.code = (Integer) messageId;
        return result;
    }
}
