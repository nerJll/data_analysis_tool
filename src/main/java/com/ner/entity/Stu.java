package com.ner.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @autor jiangll
 * @date 2020/8/22
 */
@Data
@TableName("stu")
public class Stu implements Serializable {
    private static final long serialVersionUID = 218415125771317736L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    @TableField("code")
    @NotBlank(message = "【学号】不能为空")
    private String code;

    @TableField("name")
    @NotBlank(message = "【姓名】不能为空")
    private String name;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
