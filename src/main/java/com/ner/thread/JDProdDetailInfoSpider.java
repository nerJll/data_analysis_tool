package com.ner.thread;

import com.ner.entity.SizeAnalysis;
import com.ner.utils.DataAnalysisUtil;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * 京东评论爬取任务
 *
 * @autor jiangll
 * @date 2020/8/29
 */
@Data
public class JDProdDetailInfoSpider implements Runnable {
    private List<String> urls;
    private List<SizeAnalysis> sizeAnalyses;

    public JDProdDetailInfoSpider(List<String> urls) {
        this.urls = urls;
        this.sizeAnalyses = new LinkedList<>();
    }

    @Override
    public void run() {
        for (String url : this.urls) {
            this.sizeAnalyses.addAll(new DataAnalysisUtil().jdTwoLevelSearch(url));
        }
    }
}
