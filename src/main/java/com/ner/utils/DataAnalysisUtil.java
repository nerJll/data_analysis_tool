package com.ner.utils;

import com.ner.common.exception.BizException;
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
    //天猫搜索前缀、后缀
    private final static String TMALLURL_PREFFIX = "https://list.tmall.com/search_product.htm?q=";
    private final static String TMALLURL_SUFFFIX = "&type=p&vmarket=&spm=875.7931836%2FB.a2227oh.d100&from=mallfp..pc_1_searchbutton";

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

    //天猫一级搜索（执行商品搜索）,返回商品链接列表
    private static List<String> tmallOneLevelSearch(String keyWords) {
        //一级搜索链接
        String url = new StringBuilder(TMALLURL_PREFFIX)
                .append(keyWords)
                .append(TMALLURL_SUFFFIX)
                .toString();
        //产品链接，一页60
        List<String> urlList = new ArrayList<>(60);
        //获取页面
        Document doc = getHtmlByUrl(url);
        //可以通过元素的标签获取html中的特定元素
        Elements es = doc.getElementsByClass("productImg");
        for (Element e : es) {
            Attributes as = e.attributes();
            String href = as.get("href");
            if (href.length() > 2)
                urlList.add(href.substring(2));
        }
        return urlList;
    }

    //天猫二级搜索（点击商品链接）
    private static String tmallTwoLevelSearch(String url) {
        return null;
    }

    //天猫商品分析
    public static void analysisTmall(String keyWords) {
        List<String> urlList = tmallOneLevelSearch(keyWords);
        System.out.println(urlList.toString());
    }
}
