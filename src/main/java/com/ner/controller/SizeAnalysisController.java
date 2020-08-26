package com.ner.controller;

import com.ner.common.ApiResult;
import com.ner.common.BaseController;
import com.ner.entity.SizeAnalysis;
import com.ner.entity.Stu;
import com.ner.mapper.SizeAnalysisMapper;
import com.ner.mapper.StuMapper;
import com.ner.service.impl.SizeAnalysisServiceImpl;
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
@RequestMapping("/size/analysis")
public class SizeAnalysisController extends BaseController<SizeAnalysisServiceImpl, SizeAnalysisMapper, SizeAnalysis, Long> {
}
