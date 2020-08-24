package com.ner.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ner.entity.Stu;

/**
 * @autor jiangll
 * @date 2020/8/23
 */
public interface StuService extends IService<Stu> {

    Long updStu(Stu stu);
}
