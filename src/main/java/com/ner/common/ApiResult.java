package com.ner.common;

import com.ner.utils.JsonUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Comment:
 * 公共json 返回数据对象
 *
 * @author : jll
 * @version : 1.0
 * @date : 2019/3/12
 */
public  class ApiResult<T> implements Serializable {
    private static final long serialVersionUID = -3187120723769022777L;
    /**
     * 是否成功
     **/
    private Boolean success;
    /**
     * 返回码
     **/
    private String code;
    /**
     * 信息
     **/
    private String message;
    /**
     * 返回结果
     **/
    private T data;
    private Object other;

    public Boolean getSuccess() {
        return success;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public Object getOther() {
        return other;
    }

    public ApiResult setSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public ApiResult setCode(String code) {
        this.code = code;
        return this;
    }

    public ApiResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public ApiResult setData(T data) {
        this.data = data;
        return this;
    }

    public ApiResult setOther(Object other) {
        this.other = other;
        return this;
    }

    public static ApiResult ok() {
        ApiResult result = new ApiResult();
        result.setCode("200");
        result.setMessage("操作成功");
        result.setSuccess(true);
        return result;
    }

    /**
     * 成功 返回 数据 信息使用默认
     *
     * @param data
     * @return
     */
    public static ApiResult okData(Object data) {
        ApiResult result = new ApiResult();
        result.setData(data);
        result.setMessage("操作成功");
        result.setCode("200");
        result.setSuccess(true);
        return result;
    }

    public static ApiResult okData(Object data, Object other) {
        ApiResult result = new ApiResult();
        result.setData(data);
        result.setOther(other);
        result.setMessage("操作成功");
        result.setCode("200");
        result.setSuccess(true);
        return result;
    }

    /**
     * 成功返回信息，data 为null
     *
     * @param message
     * @return
     */
    public static ApiResult okMessage(String message) {
        ApiResult result = new ApiResult();
        result.setCode("200");
        result.setSuccess(true);
        result.setMessage(message);
        return result;
    }

    /**
     * 成功 返回 信息和数据
     *
     * @param message 需要返回的信息
     * @param data    需要返回的数据
     * @return
     */
    public static ApiResult okMessageAndData(String message, Object data) {
        ApiResult result = new ApiResult();
        result.setData(null);
        result.setCode("200");
        result.setSuccess(true);
        result.setData(data);
        result.setMessage(message);
        return result;
    }


    /**
     * 成功
     *
     * @return
     */
    public static ApiResult err() {
        ApiResult result = new ApiResult();
        result.setCode("500");
        result.setSuccess(false);
        result.setMessage("操作失败");
        return result;
    }

    /**
     * 成功 返回 数据 信息使用默认
     *
     * @param o
     * @return
     */
    public static ApiResult errData(Object o) {
        ApiResult result = new ApiResult();
        result.setData(o);
        result.setCode("500");
        result.setMessage("操作失败");
        result.setSuccess(false);
        return result;
    }

    /**
     * 成功返回信息，data 为null
     *
     * @param message
     * @return
     */
    public static ApiResult errMessage(String message) {
        ApiResult result = new ApiResult();
        result.setCode("200");
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }

    /**
     * 成功 返回 信息和数据
     *
     * @param message 需要返回的信息
     * @param data    需要返回的数据
     * @return
     */
    public static ApiResult errMessageAndData(String message, Object data) {
        ApiResult result = new ApiResult();
        result.setCode("500");
        result.setSuccess(false);
        result.setData(data);
        result.setMessage(message);
        return result;
    }

    /**
     * 向客户端返回json数据
     *
     * @param response
     * @param message
     */
    public static void out(HttpServletResponse response, String message) {
        out(response, "200", message);
    }

    /**
     * 向客户端返回json数据
     *
     * @param response
     * @param message
     */
    public static void out(HttpServletResponse response, String code, String message) {
        ServletOutputStream stream = null;
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            stream = response.getOutputStream();
            Map<String, Object> result = new HashMap<>();
            result.put("code", code);
            result.put("success", false);
            result.put("message", message);
            stream.write(JsonUtil.toJson(result).getBytes());
        } catch (Exception var16) {

        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException var15) {
                    var15.printStackTrace();
                }
            }
        }
    }
}
