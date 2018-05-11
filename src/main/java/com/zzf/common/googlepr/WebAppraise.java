package com.zzf.common.googlepr;

import java.io.IOException;
/**
 * Copyright 2008
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class WebAppraise {
    private String googleSum;
    private String baiduSum;
    private String msnSum;
    private String altaVistaSum;
    private String allTheWebSum;
    private String yahooSum;
    private String testURL;
    public WebAppraise(final String url) {
        if (url != null && !"".equals(url)) {
            this.testURL = url.trim();
            if (this.testURL.startsWith("http://")) {
                this.testURL = this.testURL.substring(7);
            }
            if (this.testURL.startsWith("https://")) {
                this.testURL = this.testURL.substring(8);
            }
        } else {
            throw new RuntimeException("url is NULL!");
        }
    }
    /**
     * 分析指定链接结果，并返回整型数�?
     * 
     * @param searchURL
     * @param anchor
     * @param trail
     * @return
     */
    private static int getLinks(final String searchURL, final String anchor,
            final String trail) {
        int count = 0;
        String serverResponse;
        System.out.println("********searchURL="+searchURL);
        try {
            // 我国特色…�?
            if (searchURL.startsWith("http://www.baidu.com")) {
                // 永不离休的gb2312同志(-_-||)
                serverResponse = SimpleWebClient.getRequestHttp(searchURL,
                        "gb2312");
            } else {
                serverResponse = SimpleWebClient.getRequestHttp(searchURL);
            }
        } catch (IOException e) {
            serverResponse = e.getMessage();
        }
        int pos = serverResponse.indexOf(anchor);
        if (pos > 1) {
            serverResponse = serverResponse.substring(pos + anchor.length());
            pos = serverResponse.indexOf(trail);
            String value = serverResponse.substring(0, pos).trim();
            value = value.replace(",", "");
            value = value.replace(".", "");
            count = Integer.parseInt(value);
        }
        return count;
    }
    public String getAllTheWebSite() {
        return getAllTheWebSite(false);
    }
    public String getAllTheWebSite(boolean isDomain) {
        try {
            String allTheWeb;
            if (isDomain) {
                allTheWeb = "http://www.alltheweb.com/search?cat=web&cs=utf8&rys=0&itag=crv&_sb_lang=any&q=linkdomain%3A"
                        + this.testURL;
            } else {
                allTheWeb = "http://www.alltheweb.com/search?cat=web&cs=utf-8&q=link%3Ahttp%3A%2F%2F"
                        + this.testURL + "&_sb_lang=any";
            }
            allTheWebSum = ""
                    + getLinks(allTheWeb, "<span class=\"ofSoMany\">",
                            "</span>");
        } catch (Exception ex) {
            allTheWebSum = ex.getMessage();
        }
        return allTheWebSum;
    }
    public String getAltaVistaSite() {
        return getAltaVistaSite(false);
    }
    public String getAltaVistaSite(boolean isDomain) {
        try {
            String altaVista;
            if (isDomain) {
                altaVista = "http://www.altavista.com/web/results?itag=ody&q=link%3A"
                        + this.testURL + "&kgs=0&kls=0";
            } else {
                altaVista = "http://www.altavista.com/web/results?itag=ody&kgs=0&kls=0&q=site%3A"
                        + this.testURL;
            }
            altaVistaSum = "" + getLinks(altaVista, "AltaVista found ", " ");
        } catch (Exception ex) {
            altaVistaSum = ex.getMessage();
        }
        return altaVistaSum;
    }
    public String getGooglePR() {
        return GooglePageRank.GooglePR(this.testURL);
    }
    public String getGoogleSite() {
        return getGoogleSite(false);
    }
    public String getGoogleSite(final boolean isDomian) {
        try {
            String google;
            // 反向链接
            if (isDomian) {
                google = "http://www.google.com/search?hl=en&q=link%3A"
                        + this.testURL;
            } else {
                google = "http://www.google.com/search?hl=en&q=site%3A"
                        + this.testURL;
            }
            googleSum = "" + getLinks(google, "about <b>", "</b>");
        } catch (Exception ex) {
            googleSum = ex.getMessage();
        }
        return googleSum;
    }
    public String getBaiduSite() {
        return getBaiduSite(false);
    }
    public String getBaiduSite(final boolean isDomian) {
        try {
            String baidu;
            if (isDomian) {
                baidu = "http://www.baidu.com/s?wd=domain%3A" + this.testURL
                        + "&cl=3";
            } else {
                baidu = "http://www.baidu.com/s?wd=site%3A" + this.testURL;
            }
            baiduSum = "" + getLinks(baidu, "找到相关网页", "篇");
        } catch (Exception ex) {
            String baidu;
            if (isDomian) {
                baidu = "http://www.baidu.com/s?wd=domain%3A" + this.testURL
                        + "&cl=3";
            } else {
                baidu = "http://www.baidu.com/s?wd=site%3A" + this.testURL;
            }
            baiduSum = "" + getLinks(baidu, "找到相关网页约", "篇");
        }
        return baiduSum;
    }
    public String getYahooSite() {
        return getYahooSite(false);
    }
    public String getYahooSite(final boolean isDomian) {
        try {
            String yahoo;
            if (isDomian) {
                yahoo = "http://sitemap.cn.yahoo.com/search?p=" + this.testURL
                        + "&bwm=i";
                yahooSum = "" + getLinks(yahoo, "<strong>", "</strong>");
            } else {
                yahoo = "http://www.yahoo.cn/s?p=site%3A" + this.testURL
                        + "&pid=hp&v=web";
                yahooSum = "" + getLinks(yahoo, "找到相关网页约", "条");
            }
        } catch (Exception ex) {
            yahooSum = ex.getMessage();
        }
        return yahooSum;
    }
    public String getMsnSite() {
        return getMsnSite(false);
    }
    public String getMsnSite(boolean isDomain) {
        try {
            String msn;
            if (isDomain) {
                msn = "http://cnweb.search.live.com/results.aspx?q=link%3A"
                        + this.testURL + "&mkt=zh-cn&scope=&FORM=LIVSO";
            } else {
                msn = "http://cnweb.search.live.com/results.aspx?q=site%3A"
                        + this.testURL + "&go=&form=QBRE";
            }
            msnSum = "" + getLinks(msn, "共", "条搜索结果");
        } catch (Exception ex) {
            msnSum = ex.getMessage();
        }
        return msnSum;
    }
    public String getTestURL() {
        return testURL;
    }
}
