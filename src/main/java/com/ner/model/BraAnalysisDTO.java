package com.ner.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @autor jiangll
 * @date 2020/9/1
 */
@Data
public class BraAnalysisDTO implements Serializable {
    private static final long serialVersionUID = -2582650313541987725L;

    private String ACup;
    private String BCup;
    private String CCup;
    private String DCup;
    private String ECup;
}
