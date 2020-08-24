package com.ner.common.page;

import lombok.Data;

/**
 * 功能描述：分页结果的封装
 *
 * @author jll
 * @date 2019/4/8 11:41
 */
@Data
public class PageInfo<T> {
    /**
     * 是否成功
     **/
    private Boolean success;

    private String code;

    private String message = "请求成功";

    private T data;

    private int count;

}
