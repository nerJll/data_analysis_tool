package com.ner.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ner.common.exception.BizException;
import com.ner.entity.SizeAnalysis;
import com.ner.mapper.SizeAnalysisMapper;
import com.ner.service.SizeAnalysisService;
import com.ner.utils.IdWorker;
import com.ner.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @autor jiangll
 * @date 2020/8/23
 */
@Service
public class SizeAnalysisServiceImpl extends ServiceImpl<SizeAnalysisMapper, SizeAnalysis> implements SizeAnalysisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SizeAnalysisServiceImpl(RedisTemplate<String, Object> redisTemplate, JdbcTemplate jdbcTemplate) {
        this.redisTemplate = redisTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveJDBraEvaluateData(List<SizeAnalysis> sizeAnalysisList) {
        if (CollectionUtils.isEmpty(sizeAnalysisList)) return;
        sizeAnalysisList.forEach(sizeAnalysis -> {
            sizeAnalysis.setId(IdWorker.id.nextId());
            sizeAnalysis.setCreateTime(new Date());
        });
        redisTemplate.opsForValue().set("jdBraEvaluateData", sizeAnalysisList, 1, TimeUnit.HOURS);
        String insertSql;
        try {
            insertSql = SqlUtils.getInsertSql(sizeAnalysisList, SizeAnalysis.class);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("转换sql失败");
            throw new BizException("服务器内部错误:201");
        }
        jdbcTemplate.execute(insertSql);
    }
}
