package com.ner.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 京东评论列表
 *
 * @autor jiangll
 * @date 2020/8/28
 */
@Data
public class JDEvaluateDTO implements Serializable {
    private static final long serialVersionUID = 1909402736371472262L;

    private Object[] productAttr;
    private Object productCommentSummary;
    private Object[] hotCommentTagStatistics;
    private String jwotestProduct;
    private String maxPage;
    private String testId;
    private String score;
    private String soType;
    private String imageListCount;
    private String vTagStatistics;
    private String csv;
    private Object[] comments;
}
