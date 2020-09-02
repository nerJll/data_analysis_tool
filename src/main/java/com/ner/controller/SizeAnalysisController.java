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
import java.util.Map;

/**
 * @autor jiangll
 * @date 2020/8/22
 */
@RestController
@RequestMapping("/size/analysis")
public class SizeAnalysisController extends BaseController<SizeAnalysisServiceImpl, SizeAnalysisMapper, SizeAnalysis, Long> {
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public SizeAnalysisController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //首页数据
    @GetMapping("/index")
    public ApiResult index(@RequestParam(required = false) Map<String, String> param) {
        return ApiResult.okData(service.braAnalysis(param));
    }

    //修改cookie 1-天猫，2-京东
    @PutMapping("/updCookie")
    public ApiResult updCookie(Integer type, String cookie) {
        DataAnalysisUtil.updCookie(type, cookie);
        return ApiResult.ok();
    }

    //爬取数据
    @GetMapping("/bra")
    public ApiResult bra() {
        List<SizeAnalysis> sizeAnalysisList = new DataAnalysisUtil().analysisJD("胸罩");
        service.saveJDBraEvaluateData(sizeAnalysisList);
        return ApiResult.okData(sizeAnalysisList.size());
//        List<SizeAnalysis> sizeAnalysisList = new DataAnalysisUtil().analysisTmall("胸罩");
//        return ApiResult.ok();
    }
}
