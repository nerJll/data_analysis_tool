package com.ner.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.ner.model.JDEvaluateCommentDTO;
import com.ner.utils.JsonUtil;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.time.DateUtils;

import java.io.Serializable;
import java.text.ParseException;
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

    public static SizeAnalysis fromJDEvaluateCommentDTO(JDEvaluateCommentDTO dto) {
        SizeAnalysis sizeAnalysis = new SizeAnalysis();
        sizeAnalysis.setUserName(dto.getNickname());
        sizeAnalysis.setProdSize(dto.getProductSize());
        sizeAnalysis.setProdType(dto.getProductColor());
        sizeAnalysis.setEvaluateContent(dto.getContent());
        sizeAnalysis.setPicUrls(JsonUtil.toJson(dto.getImages()));
        try {
            sizeAnalysis.setEvaluateDate(DateUtils.parseDate(dto.getCreationTime(), "yyyy-MM-dd HH:mm:ss"));
        } catch (ParseException e) {
            System.out.println("时间转换失败");
            e.printStackTrace();
        }
        return sizeAnalysis;
    }
}
