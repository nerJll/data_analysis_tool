package com.ner.utils;

import com.ner.common.exception.BizException;
import com.ner.entity.SizeAnalysis;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @autor jiangll
 * @date 2020/8/24
 */
public final class DataAnalysisUtil {
    //天猫一级搜索前缀、后缀
    private final static String TMALL_ONELEVEL_URLPREFFIX = "https://list.tmall.com/search_product.htm?q=";
    private final static String TMALL_ONELEVEL_URLSUFFFIX = "&type=p&vmarket=&spm=875.7931836%2FB.a2227oh.d100&from=mallfp..pc_1_searchbutton";
    //天猫二级搜索前缀
    private final static String TMALL_TWOLEVEL_URLPREFFIX = "https:";

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

    //天猫一级搜索（商品搜索）,返回商品链接列表
    private static List<String> tmallOneLevelSearch(String keyWords) {
        //一级搜索链接
        String url = new StringBuilder(TMALL_ONELEVEL_URLPREFFIX)
                .append(keyWords)
                .append(TMALL_ONELEVEL_URLSUFFFIX)
                .toString();
        //产品链接，一页60
        List<String> urlList = new ArrayList<>(60);
        //获取页面
        Document doc = getHtmlByUrl(url);
        //可以通过元素的标签获取html中的特定元素
        Elements es = doc.getElementsByClass("productImg");
        for (Element e : es) {
            urlList.add(e.attributes().get("href"));
        }
        return urlList;
    }

    //天猫二级搜索(详情页）
    private static List<SizeAnalysis> tmallTwoLevelSearch(String url) {
        String prodUrl = new StringBuilder(TMALL_TWOLEVEL_URLPREFFIX)
                .append(url)
                .toString();
        Document doc = getHtmlByUrl(prodUrl);
        //产品名称
        Element prodName = doc.getElementsByTag("h1").get(1);
        System.out.println(prodName.text());
        //产品尺寸
        Element prodSize = doc.getElementById("bd")
                .getElementById("J_Reviews");
        System.out.println(prodSize.text());
        return null;
    }

    //天猫三级查询（评论翻页）
    private static List<SizeAnalysis> tmallThreeLevelSearch(String url) {
        return null;
    }

    public static void analysisTmall(String keyWords) {
        List<String> urlList = tmallOneLevelSearch(keyWords);
        for (String url : urlList) {
            tmallTwoLevelSearch(url);
        }
    }
}
