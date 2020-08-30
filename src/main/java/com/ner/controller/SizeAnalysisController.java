package com.ner.controller;

import com.ner.common.ApiResult;
import com.ner.common.BaseController;
import com.ner.entity.SizeAnalysis;
import com.ner.mapper.SizeAnalysisMapper;
import com.ner.service.impl.SizeAnalysisServiceImpl;
import com.ner.utils.DataAnalysisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

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

    //修改cookie 1-天猫，2-京东
    @PutMapping("/updCookie")
    public ApiResult updCookie(Integer type, String cookie) {
        DataAnalysisUtil.updCookie(type, cookie);
        return ApiResult.ok();
    }

    @GetMapping("/bra")
    public ApiResult bra() {
//        List<SizeAnalysis> sizeAnalysisList = new DataAnalysisUtil().analysisJD("胸罩");
//        service.saveJDBraEvaluateData(sizeAnalysisList);
//        return ApiResult.okData(sizeAnalysisList.size());
        List<SizeAnalysis> sizeAnalysisList = new DataAnalysisUtil().analysisTmall("胸罩");
        return ApiResult.ok();
    }
}
