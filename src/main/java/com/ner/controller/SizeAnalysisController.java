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

import java.util.Arrays;
import java.util.HashMap;
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

    @GetMapping("/index")
    public ApiResult index() {
        //柱状图数据
        Map<String, Object> column = new HashMap<>(4);
        column.put("title", "京东bra罩杯分析");
        column.put("leData", Arrays.asList("北京", "上海", "深圳"));
        column.put("xData", Arrays.asList("A杯", "B杯", "C杯", "D杯", "E杯"));
        column.put("sData", Arrays.asList(
                Arrays.asList(559, 366, 229, 339, 324),
                Arrays.asList(223, 556, 333, 222, 123),
                Arrays.asList(433, 566, 122, 211, 97)
        ));
        //饼状图数据
        Map<String, Object> cake = new HashMap<>(3);
        cake.put("title", "京东bra罩杯分析");
        cake.put("leData", Arrays.asList("A杯", "B杯", "C杯", "D杯", "E杯"));
        cake.put("sData", Arrays.asList(559, 366, 229, 339, 324));

        Map<String, Map> result = new HashMap<>(2);
        result.put("column", column);
        result.put("cake", cake);
        return ApiResult.okData(result);
    }

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
