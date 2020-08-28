package com.ner.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 京东评论详情
 *
 * @autor jiangll
 * @date 2020/8/28
 */
@Data
public class JDEvaluateCommentDTO implements Serializable {
    private static final long serialVersionUID = 246226491373683585L;

    //尺码
    private String productSize;
    //颜色
    private String productColor;
    //评论时间
    private String creationTime;
    //用户昵称
    private String nickname;
    //评论内容
    private String content;
    //图片地址
    private Object[] images;
}
