package com.ner.utils;

import com.ner.common.exception.BizException;
import com.ner.entity.SizeAnalysis;
import com.ner.model.JDEvaluateCommentDTO;
import com.ner.model.JDEvaluateDTO;
import com.ner.thread.JDProdDetailInfoSpider;
import com.ner.thread.JDProdEvaluateSpider;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

/**
 * @autor jiangll
 * @date 2020/8/24
 */
public final class DataAnalysisUtil {
    //天猫一级搜索前缀、后缀
    private final static String TMALL_ONELEVEL_URLPREFFIX = "https://list.tmall.com/search_product.htm?q=";
    private final static String TMALL_ONELEVEL_URLSUFFFIX = "&type=p&vmarket=&spm=875.7931836%2FB.a2227oh.d100&from=mallfp..pc_1_searchbutton";
    //京东一级搜索前缀、后缀
    private final static String JD_ONELEVEL_URLPREFFIX = "https://search.jd.com/Search?keyword=";
    private final static String JD_ONELEVEL_URLSUFFFIX = "&psort=3&click=1";
    //天猫、京东二级搜索前缀
    private final static String TWOLEVEL_URLPREFFIX = "https:";
    //京东评论翻页前缀、中缀、后缀
    private final static String JD_EVALUATE_URLPREFFIX = "https://club.jd.com/comment/productPageComments.action?callback=fetchJSON_comment98&productId=";
    private final static String JD_EVALUATE_URLMIDFIX = "&score=0&sortType=5&page=";
    private final static String JD_EVALUATE_URLSUFFFIX = "&pageSize=10&isShadowSku=0&rid=0&fold=1";
    //京东cookie
    private static String JD_COOKIE = "o2State={%22webp%22:true}; __jda=122270672.1598763538016193399461.1598763538.1598763538.1598763554.2; __jdb=122270672.3.1598763538016193399461|2.1598763554; __jdc=122270672; __jdv=76161171|baidu-pinzhuan|t_288551095_baidupinzhuan|cpc|0f3d30c8dba7459bb52f2eb5eba8ac7d_0_5855a37ae130486c9efeab03dc28afed|1598763553828; __jdu=1598763538016193399461; unpl=V2_ZzNtbUZeRxElCkAELxleBGIKFQ9KAxRBJVtHVi5LXww3VEYIclRCFnQUR11nG10UZwAZWUpcQBFFCEdkeBBVAWMDE1VGZxBFLV0CFSNGF1wjU00zQwBBQHcJFF0uSgwDYgcaDhFTQEJ2XBVQL0oMDDdRFAhyZ0AVRQhHZHsdVAxhBBBZQF5CFXIITlZ5EVQAYAMibUVncyVyDEVQchBsBFcCIh8WC0ERcwBPXTYZWA1uBRVfRlVKFHUPRlx5G1QNYgQSbUNnQA%3d%3d; areaId=24; ipLoc-djd=24-2144-3909-58337; PCSYCityID=CN_520000_0_0; shshshfpb=jt7FVRFtjalt4Fcp7XkAmTA%3D%3D; shshshfp=371165d5586c6fe82cdf2ff87d9457bc; shshshfpa=00c1d4aa-bad7-841c-6f0c-0c288c56079e-1594099274; shshshsID=926592bbb16a3b3fee3cb1e4bb795d85_3_1598763801159; 3AB9D23F7A4B3C9B=QJA6NBXMHQ245NBSQLKQMBS73PDAF62JIGJRFVVUWMVJGAUUI5IU2THTLGCLOGUG46NDZTIJP5WL7TG6XCZNMTVWD4";
    private static Map<String, String> jd_headers;
    //处理结果
    private List<SizeAnalysis> sizeAnalyses = new LinkedList<>();
    private List<SizeAnalysis> sizeAnalyses_thread = new LinkedList<>();

    static {
        jd_headers = new HashMap<>();
        jd_headers.put("cookie", JD_COOKIE);
    }

    //修改cookie 1-天猫，2-京东
    public static void updCookie(int type, String cookie) {
        if (type == 1) {

        } else if (type == 2) {
            jd_headers.replace("cookie", cookie);
        }
    }

    //模拟点击链接获取页面html
    private Document getHtmlByUrl(int type, String url) {
        Document doc = null;
        try {
            //先获得的是整个页面的html标签页面
            if (type == 1) {

            } else if (type == 2) {
                doc = Jsoup.connect(url).headers(jd_headers).get();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException("获取网页内容失败，请修改cookie");
        }
        return doc;
    }

    //天猫、京东一级搜索（商品搜索）,返回商品链接列表
    private List<String> oneLevelSearch(String url, String imgClazz, int type) {
        //产品链接，一页60
        List<String> urlList = new ArrayList<>(60);
        //获取页面
        Document doc = getHtmlByUrl(type, url);
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
    private List<SizeAnalysis> tmallTwoLevelSearch(String url) {
        List<SizeAnalysis> sizeAnalyses = new LinkedList<>();
        String prodUrl = TWOLEVEL_URLPREFFIX + url;
        Document doc = getHtmlByUrl(1, prodUrl);
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
    private List<SizeAnalysis> tmallThreeLevelSearch(String url) {
        return null;
    }

    //天猫评论爬取
    public List<SizeAnalysis> analysisTmall(String keyWords) {
        //一级搜索链接
        String url = TMALL_ONELEVEL_URLPREFFIX + keyWords + TMALL_ONELEVEL_URLSUFFFIX;
        List<String> urlList = oneLevelSearch(url, "productImg", 1);
        for (String url1 : urlList) {
            tmallTwoLevelSearch(url1);
        }
        return null;
    }

    //京东二级搜索(详情页）
    public List<SizeAnalysis> jdTwoLevelSearch(String url) {
        //产品链接
        String prodUrl = TWOLEVEL_URLPREFFIX + url;
        //产品id
        String prodId = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
        Document doc = getHtmlByUrl(2, prodUrl);
        //产品名称
        String prodName = doc.getElementsByClass("sku-name").text();
        //产品评论分析，京东最多100页
        int size = 10, pageSize = 100 / size;
        JDProdEvaluateSpider[] jdpess = new JDProdEvaluateSpider[size];
        List<Thread> ts = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            JDProdEvaluateSpider jdpes = new JDProdEvaluateSpider(i * pageSize, (i + 1) * pageSize, prodId, prodName, prodUrl);
            Thread t = new Thread(jdpes);
            t.start();
            jdpess[i] = jdpes;
            ts.add(t);
        }
        synchronized (sizeAnalyses_thread) {
            try {
                for (int i = 0; i < size; i++) {
                    ts.get(i).join();
                }
            } catch (InterruptedException e) {
                System.out.println("线程出错");
                e.printStackTrace();
                throw new BizException("服务器内部错误：203");
            }
        }
        for (int i = 0; i < size; i++) {
            sizeAnalyses_thread.addAll(jdpess[i].getSizeAnalyses());
        }
        return sizeAnalyses_thread;
    }

    //京东二级搜索 线程任务
    public List<SizeAnalysis> jdTwoLevelSearch_thread(int sta, int end, String prodId, String prodName, String prodUrl) {
        List<SizeAnalysis> sizeAnalyses = new LinkedList<>();
        for (int i = sta; sta < end; sta++) {
            Document doc1 = getHtmlByUrl(2, JD_EVALUATE_URLPREFFIX + prodId + JD_EVALUATE_URLMIDFIX + sta + JD_EVALUATE_URLSUFFFIX);
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
        }
        return sizeAnalyses;
    }

    //京东评论爬取
    public List<SizeAnalysis> analysisJD(String keyWords) {
        //总共分析几页数据
        int page = 1;
        while (page < 3) {
            //一级搜索链接
            String jdOneLevelUrl = JD_ONELEVEL_URLPREFFIX + keyWords + JD_ONELEVEL_URLSUFFFIX;
            List<String> urls = oneLevelSearch(jdOneLevelUrl, "p-img", 2);
            //二级详情链接
            if (CollectionUtils.isNotEmpty(urls)) {
                if (urls.size() == 1) {
                    sizeAnalyses.addAll(jdTwoLevelSearch(urls.get(0)));
                } else {
                    //多线程处理评论
//                JDProdDetailInfoSpider jdpdis1 = new JDProdDetailInfoSpider(urls.subList(0, urls.size() / 2));
//                JDProdDetailInfoSpider jdpdis2 = new JDProdDetailInfoSpider(urls.subList(urls.size() / 2, urls.size()));
                    int size = 10, pageSize = 30 / size;
                    JDProdDetailInfoSpider[] jpdpiss = new JDProdDetailInfoSpider[size];
                    List<Thread> ts = new ArrayList<>(size);
                    for (int i = 0; i < 10; i++) {
                        JDProdDetailInfoSpider jdpdis = new JDProdDetailInfoSpider(urls.subList(i * pageSize, (i + 1) * pageSize));
                        Thread t = new Thread(jdpdis);
                        t.start();
                        jpdpiss[i] = jdpdis;
                        ts.add(t);
                    }
                    synchronized (sizeAnalyses) {
                        try {
                            for (int i = 0; i < size; i++) {
                                ts.get(i).join();
                            }
                        } catch (InterruptedException e) {
                            System.out.println("线程出错");
                            e.printStackTrace();
                            throw new BizException("服务器内部错误：202");
                        }
                    }
                    for (int i = 0; i < size; i++) {
                        sizeAnalyses.addAll(jpdpiss[i].getSizeAnalyses());
                    }
                }
            }
            page++;
        }
        sizeAnalyses = new ArrayList<>(sizeAnalyses);
        return sizeAnalyses;
    }
}
