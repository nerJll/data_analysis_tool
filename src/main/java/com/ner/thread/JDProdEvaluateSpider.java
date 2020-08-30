package com.ner.thread;

import com.ner.entity.SizeAnalysis;
import com.ner.utils.DataAnalysisUtil;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @autor jiangll
 * @date 2020/8/30
 */
@Data
public class JDProdEvaluateSpider implements Runnable {
    private int sta;
    private int end;
    private String prodId;
    private String prodName;
    private String prodUrl;
    private List<SizeAnalysis> sizeAnalyses;

    public JDProdEvaluateSpider(int sta, int end, String prodId, String prodName, String prodUrl) {
        this.sta = sta;
        this.end = end;
        this.prodId = prodId;
        this.prodName = prodName;
        this.prodUrl = prodUrl;
        this.sizeAnalyses = new LinkedList<>();
    }

    @Override
    public void run() {
        this.sizeAnalyses.addAll(new DataAnalysisUtil().jdTwoLevelSearch_thread(
                this.sta, this.end, this.prodId, this.prodName, this.prodUrl
        ));
    }
}
