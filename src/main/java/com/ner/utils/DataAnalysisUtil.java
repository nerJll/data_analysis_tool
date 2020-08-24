package com.ner.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @autor jiangll
 * @date 2020/8/24
 */
public final class DataAnalysisUtil {
    //设置代理，模范浏览器
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36";

    //返回页面html代码
    public static String sendGet(String url) {
        //1.生成httpclient，相当于该打开一个浏览器
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
        CloseableHttpResponse response = null;
        String html = null;
        //2.创建get请求，相当于在浏览器地址栏输入 网址
        HttpGet request = new HttpGet(url);
        try {
            request.setHeader("User-Agent", USER_AGENT);
            request.setConfig(requestConfig);
            //3.执行get请求，相当于在输入地址栏后敲回车键
            response = httpClient.execute(request);
            //4.判断响应状态为200，进行处理
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //5.获取响应内容
                HttpEntity httpEntity = response.getEntity();
                html = EntityUtils.toString(httpEntity, "GBK");
            } else {
                //TODO 如果返回状态不是200，比如404（页面不存在）等，根据情况做处理，这里略
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //6.关闭
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpClient);
        }
        return html;
    }

   /* private void paraseList(Document document){
        //根据网页标签解析源码
        Elements elements = document.select("#resultList .el");
        //去除表头
        elements.remove(0);
        for(Element element:elements){
            Elements elements1 = element.select("span");
            QCPage qcPage = new QCPage();
            qcPage.set(elements1.get(0).text(),elements1.get(1).text(),elements1.get(2).text(),elements1.get(3).text(),elements1.get(4).text());
            //将解析后的实体放入集合中
            list.add(qcPage);
            System.out.println(result+" : " +qcPage);
            result+=1;
        }
        *//**
         * 这里解析下一页地址的标签，获取下一页的Url,然后放在redis中
         *//*
        Elements elements1 = document.select("li[class=bk]").select("a");
        for(Element element:elements1){
            String url = element.attr("href");
            if(StringUtil.isNotBlank(url)){
                stringRedisTemplate.opsForList().leftPush("url",url);
            }
        }
    }*/
}
