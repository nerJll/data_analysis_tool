package com.ner.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据分析模型
 *
 * @autor jiangll
 * @date 2020/8/26
 */
@Data
@ToString
@TableName("size_analysis")
public class SizeAnalysis implements Serializable {
    private static final long serialVersionUID = 5255342673411554917L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    @TableField("prod_name")
    private String prodName;

    @TableField("prod_url")
    private String prodUrl;

    @TableField("prod_size")
    private String prodSize;

    @TableField("prod_type")
    private String prodType;

    @TableField("evaluate_date")
    private Date evaluateDate;

    @TableField("user_name")
    private String userName;

    @TableField("evaluate_content")
    private String evaluateContent;

    @TableField("pic_urls")
    private String picUrls;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
}
