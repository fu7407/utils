package com.zzf.common.googlepr;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class DomainEvaluate {
	
	private static Log logger = LogFactory.getLog(DomainEvaluate.class);
	
	
	public String getResponseBodyStr(String url){
		String str = "";
		try {
			SimpleWebClient.getRequestHttp(url);
			//str = new String(HttpClient.GetDataAuto(url));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return str;
	}
	
    /**
     * 正则匹配结果
     * @param value
     * @return
     */
    public String MatchRank(String value,String regex,int index) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        if (matcher.find()) {
            return matcher.group(index);
        }
        return null;
    }
	
	/**
	 * getResponseBodyStr(url) retun Rank_1:1:9
	 * @param domain - (String)
	 * @return PR rating (int) or -1 if unavailable or internal error happened.
	 */
	public String getGooglePR(String domain) {
		JenkinsHash jenkinsHash = new JenkinsHash();
		long hash = jenkinsHash.hash(("info:" + domain).getBytes());
		String url = "http://toolbarqueries.google.com/tbr?client=navclient-auto&hl=en&"
		   + "ch=6" + hash + "&ie=UTF-8&oe=UTF-8&features=Rank&q=info:" + domain;
		String regex = "Rank_1:[0-9]:([0-9]+)";
		String value = MatchRank(getResponseBodyStr(url),regex,1);
		return value==null?"0":value;
	  }
    
    /**
     * 分析指定链接结果，并返回整型数�?
     * 
     * @param searchURL
     * @param anchor
     * @param trail
     * @return
     */
    public String getLinks(String searchURL) {
		String regex = "(找到相关结果约|找到相关结果|About)([0-9|,]+)(个|results)";
        String count = MatchRank(getResponseBodyStr(searchURL),regex,2);
        if(count==null){
        	count="0";
        }else{
        	count = count.replace(",", "");
        	count = count.replace(".", "");
        }
        return count;
    }
	
    
    public String getGoogleSite(String domain) {
        String searchURL="http://www.google.com.hk/search?hl=en&safe=off&btnG=Search&q=site%3A"+domain;
        //String searchURL="http://www.google.com.hk/search?hl=zh-CN&safe=off&btnG=Search&q=site%3A"+domain;
        return getLinks(searchURL);
    }
    
    public String getBaiduSite(String domain) {
        String searchURL="http://www.baidu.com/s?wd=site%3A"+domain;
        return getLinks(searchURL);
    }
    
    public String get360Site(String domain) {
        String searchURL="http://www.so.com/s?q=site%3A"+domain;
        return getLinks(searchURL);
    }
	
	/**
	 * 获取alexa排名
     *  <ALEXA VER="0.9" URL="qq.com/" HOME="0" AID="=" IDN="qq.com/">
		<KEYWORDS>
		<KEYWORD VAL="Chinese Simplified"/>
		</KEYWORDS><DMOZ>
		<SITE BASE="qq.com/" TITLE="QQ.COM" DESC="China&amp;#039;s largest and most used Internet service portal owned by Tencent, Inc founded in November, 1998. Presently, Tencent is aiming its operations at the strategic goal of providing users with a &amp;quot;one-stop online life service&amp;quot;. Tencent&amp;#039;s Internet platforms QQ, QQ.com, QQ Games, and PaiPai.com have brought together China&amp;#039;s largest Internet community. Tencent&amp;#039;s communications and information-sharing services include QQ.com, QQ Instant Messenger, QQ Mail, and search engine SOSO. Linked up with heavily used features such as forums, chat rooms, and QQ Groups, Tencent&amp;#039;s Qzone has grown into China&amp;#039;s largest personal Internet space. These services foster group interaction and resource sharing. Virtual products such as QQ Show, QQ Pet, QQ Game, and QQ Music/Radio/Live have been successful in providing entertainment and customization options to users. Mobile phone users can take advantage of a number of value-added wireless services. Tencent&amp;#039;s PaiPai.com is a C2C on-line shopping platform that seamlessly integrates into Tencent&amp;#039;s other community platforms.As of June 30th, 2009, the number of registered QQ Instant Messenger users has reached 990.0 million. Active users numbered at 448.0 million. Peak concurrent users have reached 61.30 million. QQ Games platform counted about 6.2 million users simultaneously on-line. QQ.com has become China&amp;#039;s most visited Internet portal website. PaiPai.com has also become China&amp;#039;s second largest Internet shopping platform.">
		<CATS>
		<CAT ID="Top/World/Chinese_Simplified_CN/计算�?互联网络/门户网站" TITLE="互联网络/门户网站" CID="254903"/>
		<CAT ID="Top/World/Chinese_Simplified_CN/计算�?互联网络/聊天/即时通讯/腾讯QQ" TITLE="即时通讯/腾讯QQ" CID="254912"/>
		</CATS>
		</SITE>
		</DMOZ>
		<SD>
		<POPULARITY URL="qq.com/" TEXT="7" SOURCE="panel"/>
		<REACH RANK="7"/>
		<RANK DELTA="-1"/>
		<COUNTRY CODE="CN" NAME="China" RANK="2"/>
		</SD>
		</ALEXA>
	 * @param domain
	 */
	public String getAlexaRanking(String domain) {
        String alexa = "0";
        String url = "http://data.alexa.com/data?cli=10&url=" + domain;
        try {
            URLConnection conn = new URL(url).openConnection();
            InputStream is = conn.getInputStream();
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            Element element = doc.getDocumentElement();
            NodeList nodeList = element.getElementsByTagName("POPULARITY");
            if (nodeList.getLength() > 0) {
                Element elementAttribute = (Element) nodeList.item(0);
                String ranking = elementAttribute.getAttribute("TEXT");
                if(!"".equals(ranking)){
                	alexa = ranking;
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	logger.error(e.getMessage());
        }
        return alexa;
    }
	
	/**
	 * 获得百度权重（从爱站网获得）
	 * return data : document.write('<a href="http://www.aizhan.com/baidu/qq.com/" target="_blank" title="10/10">10</a>');
	 * @param domain
	 */
	public String getBaiduQzByAizhan(String domain){
		String url = "http://www.aizhan.com/getbr.php?url="+domain+"&style=1";
		String regex = ">([0-9]+)</a>";
        String baiduQz = MatchRank(getResponseBodyStr(url),regex,1);
		return baiduQz==null?"0":baiduQz;
	}
	
	/**
	 * 获得百度权重（从中国站长站获得）
	 * @param domain
	 */
	public String getBaiduQzByChinaz(String domain){
		String url = "http://mytool.chinaz.com/baidusort.aspx?host="+domain;
		String regex = "百度权重�?font color=\"blue\">([0-9]+)</font>";
        String baiduQz = MatchRank(getResponseBodyStr(url),regex,1);
		return baiduQz==null?"0":baiduQz;
	}
	
	/**
	 * {"alexa":7,"google":13600000,"baidu":100000000,"qz":10,"pr":8,"status":"ok"}
	 * @param domain
	 */
	public void getAllBy521php(String domain){
		String url = "http://www.521php.com/api/pr/?url="+domain+"&index=1";
		String value = getResponseBodyStr(url);
		try {
	        JSONObject json = new JSONObject(value);
	        logger.warn("google = " + json.get("google"));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 获得�?��的参数（从爱站网获得�?
	 * @param domain
	 */
	public void getAllByAizhan(String domain){
		String url = "http://www.aizhan.com/siteall/"+domain+"/";
		String value = getResponseBodyStr(url);
		String regex_alexa = "<span id=\"alexa_3months\" style=\"font-weight:bold\" class=\"orange\">([0-9|,]+)</span>";
		String alexa = MatchRank(getResponseBodyStr(url),regex_alexa,1);

		String regex_ip_pv = "<span id=\"alexa_IPPV\">IP&asymp; ([0-9|,]+) PV&asymp; ([0-9|,]+)</span>";
		String IP = MatchRank(getResponseBodyStr(url),regex_ip_pv,1);
		String pv = MatchRank(getResponseBodyStr(url),regex_ip_pv,2);
		
		String regex_Google_PR = "<span id=\"main_pr\"><img align=\"absmiddle\" src=\"http://static.aizhan.com/images/pr/pr([0-9]+).gif\"></span>";
		String GooglePR = MatchRank(getResponseBodyStr(url),regex_Google_PR,1);
		
		String regex_Baidu_Qz = "<img align=\"absmiddle\" src=\"http://static.aizhan.com/images/brs/([0-9]+).gif\">";
		String baiduQz = MatchRank(getResponseBodyStr(url),regex_Baidu_Qz,1);
		
		String regex_BaiduSite = "<td id=\"baidu\">([^>]+)>([0-9|,]+)</a></td>";
		String BaiduSite = MatchRank(getResponseBodyStr(url),regex_BaiduSite,2);
		
		String regex_GoogleSite = "<td id=\"google\">([^>]+)>([0-9|,]+)</a></td>";
		String GoogleSite = MatchRank(getResponseBodyStr(url),regex_GoogleSite,2);
		
		String regex_360Site = "<td id=\"360so\">([^>]+)>([0-9|,]+)</a></td>";
		String s360Site = MatchRank(getResponseBodyStr(url),regex_360Site,2);
			
	}
	
	/**
	 * 获得�?��（从中国站长站获得）
	 * http://www.alexa.cn/index.php?url=sina.com
	 * @param domain
	 */
	public void getAllByChinaz(String domain){
		String url = "http://seo.chinaz.com/?host="+domain+"/";
		try {
			String a = getResponseBodyStr(url);
	        //logger.warn("a = " + a);
	        Pattern pattern = Pattern.compile("<span id=\"alexa_3months\" style=\"font-weight:bold\" class=\"orange\">([0-9|,]+)</span>");
	        Matcher match = pattern.matcher(a);
	        while (match.find()) {
	        	logger.warn("alexa = " + match.group(1));
	        }
	        pattern = Pattern.compile("<span id=\"alexa_IPPV\">IP&asymp; ([0-9|,]+) PV&asymp; ([0-9|,]+)</span>");
	        match = pattern.matcher(a);
	        while (match.find()) {
	        	logger.warn("IP = " + match.group(1));
	        	logger.warn("PV = " + match.group(2));
	        }
	        pattern = Pattern.compile("<img src=\"/template/default/images/ranks/Rank_([0-9]+).gif\" />");
	        match = pattern.matcher(a);
	        while (match.find()) {
	        	logger.warn("Google PR = " + match.group(1));
	        }
	        pattern = Pattern.compile("<img src=\"/template/default/images/baiduapp/([0-9]+).gif\" /></a>");
	        match = pattern.matcher(a);
	        while (match.find()) {
	        	logger.warn("Baidu Qz = " + match.group(1));
	        }
	        pattern = Pattern.compile("<td id=\"baidu\">([^>]+)>([0-9|,]+)</a></td>");
	        match = pattern.matcher(a);
	        while (match.find()) {
	        	logger.warn("BaiduSite = " + match.group(2));
	        }
	        pattern = Pattern.compile("<td id=\"google\">([^>]+)>([0-9|,]+)</a></td>");
	        match = pattern.matcher(a);
	        while (match.find()) {
	        	logger.warn("GoogleSite = " + match.group(2));
	        }
	        pattern = Pattern.compile("<td id=\"360so\">([^>]+)>([0-9|,]+)</a></td>");
	        match = pattern.matcher(a);
	        while (match.find()) {
	        	logger.warn("360Site = " + match.group(2));
	        }
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * http://www.aizhan.com/siteall/"+domain+"/
	 * http://www.aizhan.com/getbr.php?url="+domain+"&style=1
	 * http://www.521php.com/api/pr/?url="+domain+"&index=1
	 * @param domain
	 */
	public void get(String domain){
		String url = "http://www.aizhan.com/siteall/"+domain+"/";
        logger.warn("url = " + url);
		try {
			//document.write('<a href="http://www.aizhan.com/baidu/qq.com/" target="_blank" title="10/10">10</a>');
			String a = getResponseBodyStr(url);
	        Pattern pattern = Pattern.compile("<span id=\"alexa_IPPV\">IP&asymp; ([0-9|,]+) PV&asymp; ([0-9|,]+)</span>");
	        logger.warn("a = " + a);
	        Matcher match = pattern.matcher(a);
	        while (match.find()) {
	        	logger.warn("match.group() = " + match.group(1));
	        	logger.warn("match.group() = " + match.group(2));
	        }
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public Long getDomainPrice(){
		Long price = 0L;
		return price;
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		DomainEvaluate de = new DomainEvaluate();
        //de.getGooglePR("qq.com");
		//de.getAlexaRanking("qq.com");
//		de.getBaiduQzByAizhan("qq.com");
//		de.getBaiduQzByChinaz("igo.com");
//        de.getGoogleSite("qq.com");
        de.getBaiduSite("qq.com");
//        de.get360Site("appmaster.com");
//		de.getAllBy521php("qq.com");
//		de.getAllByAizhan("appmaster.com");
		//de.getAllByChinaz("qq.com");
		
//		System.out.println("GooglePagerRank值：" +de.getGooglePR("baidu.com"));
//		de.get("qq.com");
//		System.out.println(de.getAlexaRanking("qq.com"));
//		byte[] b = HttpClient.GetDataAuto("http://www.aizhan.com/siteall/qq.com/");
//		byte[] b = HttpClient.GetDataAuto("http://seo.chinaz.com/?host=qq.com");
//		String a = new String(b);
//		System.out.println("*********************"+a);
	}

}
