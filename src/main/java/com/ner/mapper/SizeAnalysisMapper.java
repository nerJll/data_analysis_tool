package com.ner.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ner.entity.SizeAnalysis;
import com.ner.model.BraAnalysisDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @autor jiangll
 * @date 2020/8/23
 */
public interface SizeAnalysisMapper extends BaseMapper<SizeAnalysis> {

    @SqlParser(filter = true)
    BraAnalysisDTO braAnalysis(@Param("param") Map<String, String> param);
}
