package com.ner.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ner.entity.SizeAnalysis;

import java.util.List;

/**
 * @autor jiangll
 * @date 2020/8/23
 */
public interface SizeAnalysisService extends IService<SizeAnalysis> {
    void saveJDBraEvaluateData(List<SizeAnalysis> sizeAnalysisList);
}
