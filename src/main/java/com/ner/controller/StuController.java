package com.ner.controller;

import com.ner.common.ApiResult;
import com.ner.common.BaseController;
import com.ner.entity.Stu;
import com.ner.mapper.StuMapper;
import com.ner.service.impl.StuServiceImpl;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @autor jiangll
 * @date 2020/8/22
 */
@RestController
@RequestMapping("/stu")
public class StuController extends BaseController<StuServiceImpl, StuMapper, Stu, Long> {

    @Override
    public ApiResult upd(@RequestBody @Valid Stu entity, BeanPropertyBindingResult result) throws InterruptedException {
        return ApiResult.okData(service.updStu(entity));
    }

    @Override
    public ApiResult add(@RequestBody @Valid Stu entity, BeanPropertyBindingResult result) {
        return super.add(entity, result);
    }
}
