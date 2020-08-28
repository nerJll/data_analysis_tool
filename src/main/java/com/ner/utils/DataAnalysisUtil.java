package com.ner.utils;

import com.ner.common.exception.BizException;
import com.ner.entity.SizeAnalysis;
import com.ner.model.JDEvaluateCommentDTO;
import com.ner.model.JDEvaluateDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @autor jiangll
 * @date 2020/8/24
 */
public final class DataAnalysisUtil {
    //天猫一级搜索前缀、后缀
    private final static String TMALL_ONELEVEL_URLPREFFIX = "https://list.tmall.com/search_product.htm?q=";
    private final static String TMALL_ONELEVEL_URLSUFFFIX = "&type=p&vmarket=&spm=875.7931836%2FB.a2227oh.d100&from=mallfp..pc_1_searchbutton";
    //天猫、京东二级搜索前缀
    private final static String TWOLEVEL_URLPREFFIX = "https:";
    //京东一级搜索前缀、后缀
    private final static String JD_ONELEVEL_URLPREFFIX = "https://search.jd.com/Search?keyword=";
    private final static String JD_ONELEVEL_URLSUFFFIX = "&psort=3&click=1";
    //京东评论翻页前缀、中缀、后缀
    private final static String JD_EVALUATE_URLPREFFIX = "https://club.jd.com/comment/productPageComments.action?callback=fetchJSON_comment98&productId=";
    private final static String JD_EVALUATE_URLMIDFIX = "&score=0&sortType=5&page=";
    private final static String JD_EVALUATE_URLSUFFFIX = "&pageSize=10&isShadowSku=0&rid=0&fold=1";

    //模拟点击链接获取页面html
    private static Document getHtmlByUrl(String url) {
        Document doc;
        try {
            //先获得的是整个页面的html标签页面
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException("获取网页内容失败");
        }
        return doc;
    }

    //天猫、京东一级搜索（商品搜索）,返回商品链接列表
    private static List<String> oneLevelSearch(String url, String imgClazz, int type) {
        //产品链接，一页60
        List<String> urlList = new ArrayList<>(60);
        //获取页面
        Document doc = getHtmlByUrl(url);
        //可以通过元素的标签获取html中的特定元素
        Elements es = doc.getElementsByClass(imgClazz);
        for (Element e : es) {
            //要分析的产品个数阈值
            if (urlList.size() > 60) break;
            //1-天猫，2-京东
            if (type == 1) {
                urlList.add(e.attributes().get("href"));
            } else {
                if (e.childNodeSize() > 1) {
                    urlList.add(e.childNode(1).attributes().get("href"));
                }
            }
        }
        return urlList;
    }

    //天猫二级搜索(详情页）
    private static List<SizeAnalysis> tmallTwoLevelSearch(String url) {
        List<SizeAnalysis> sizeAnalyses = new ArrayList<>();
        String prodUrl = TWOLEVEL_URLPREFFIX + url;
        Document doc = getHtmlByUrl(prodUrl);
        //产品名称
        Elements prodNameTag = doc.getElementsByTag("h1");
        if (prodNameTag.hasText())
            System.out.println(prodNameTag.last().text());
        // todo 由于天猫评论加密，后续再研究
        //产品尺寸,类型，评论，日期，用户名，评论内容，图片地址
        Elements prodSize = doc.getElementById("bd")
                .getElementById("J_Detail")
                .getElementsByClass("评论");
        //模拟翻页
        List<SizeAnalysis> analyses1 = tmallThreeLevelSearch(null);
        if (CollectionUtils.isNotEmpty(analyses1))
            sizeAnalyses.addAll(analyses1);
        return sizeAnalyses;
    }

    //天猫三级查询（评论翻页）
    private static List<SizeAnalysis> tmallThreeLevelSearch(String url) {
        return null;
    }

    //天猫评论分析
    public static void analysisTmall(String keyWords) {
        //一级搜索链接
        String url = TMALL_ONELEVEL_URLPREFFIX + keyWords + TMALL_ONELEVEL_URLSUFFFIX;
        List<String> urlList = oneLevelSearch(url, "productImg", 1);
        for (String url1 : urlList) {
            tmallTwoLevelSearch(url1);
        }
    }

    //京东二级搜索(详情页）
    private static List<SizeAnalysis> jdTwoLevelSearch(String url) {
        List<SizeAnalysis> sizeAnalyses = new LinkedList<>();
        //产品链接
        String prodUrl = TWOLEVEL_URLPREFFIX + url;
        //产品id
        String prodId = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
        Document doc = getHtmlByUrl(prodUrl);
        //产品名称
        String prodName = doc.getElementsByClass("sku-name").text();
        //产品评论分析，京东最多100页
        int currentPage = 0;
        while (currentPage < 100) {
            Document doc1 = getHtmlByUrl(JD_EVALUATE_URLPREFFIX + prodId + JD_EVALUATE_URLMIDFIX + currentPage + JD_EVALUATE_URLSUFFFIX);
            String html = doc1.text();
            if (StringUtils.isBlank(html)) continue;
            String jDEvaluateDTOJSON = html.substring(html.indexOf("{"), html.lastIndexOf("}") + 1);
            JDEvaluateDTO jdEvaluateDTO = JsonUtil.fromJson(jDEvaluateDTOJSON, JDEvaluateDTO.class);
            if (jdEvaluateDTO == null) break;
            JDEvaluateCommentDTO[] jdEvaluateCommentDTOS = JsonUtil.fromJson(JsonUtil.toJson(jdEvaluateDTO.getComments()), JDEvaluateCommentDTO[].class);
            if (ArrayUtils.isEmpty(jdEvaluateCommentDTOS)) break;
            for (JDEvaluateCommentDTO dto : jdEvaluateCommentDTOS) {
                SizeAnalysis sizeAnalysis = SizeAnalysis.fromJDEvaluateCommentDTO(dto);
                sizeAnalysis.setProdName(prodName);
                sizeAnalysis.setProdUrl(prodUrl);
                sizeAnalyses.add(sizeAnalysis);
            }
            currentPage++;
        }
        return sizeAnalyses;
    }

    //京东评论分析
    public static List<SizeAnalysis> analysisJD(String keyWords) {
        List<SizeAnalysis> sizeAnalyses = new LinkedList<>();
        String jdOneLevelUrl = JD_ONELEVEL_URLPREFFIX + keyWords + JD_ONELEVEL_URLSUFFFIX;
        List<String> urls = oneLevelSearch(jdOneLevelUrl, "p-img", 2);
        for (String url1 : urls) {
            sizeAnalyses.addAll(jdTwoLevelSearch(url1));
        }
        return sizeAnalyses;
    }
}
