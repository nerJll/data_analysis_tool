package com.ner.controller;

import com.ner.common.ApiResult;
import com.ner.common.BaseController;
import com.ner.entity.SizeAnalysis;
import com.ner.entity.Stu;
import com.ner.mapper.SizeAnalysisMapper;
import com.ner.mapper.StuMapper;
import com.ner.service.impl.SizeAnalysisServiceImpl;
import com.ner.service.impl.StuServiceImpl;
import com.ner.utils.DataAnalysisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @autor jiangll
 * @date 2020/8/22
 */
@RestController
@RequestMapping("/size/analysis")
public class SizeAnalysisController extends BaseController<SizeAnalysisServiceImpl, SizeAnalysisMapper, SizeAnalysis, Long> {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/bra")
    public ApiResult bra() {
        List<SizeAnalysis> sizeAnalysisList = DataAnalysisUtil.analysisJD("胸罩");
        service.saveJDBraEvaluateData(sizeAnalysisList);
        return ApiResult.ok();
    }
}
