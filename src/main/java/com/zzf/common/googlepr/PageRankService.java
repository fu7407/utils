package com.zzf.common.googlepr;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class PageRankService {
	/**
	 * List of available google datacenter IPs and addresses
	 */
	static final public String [] GOOGLE_PR_DATACENTER_IPS = new String[]{
	            "64.233.161.100",
	            "64.233.161.101",
	            "64.233.177.17",
	            "64.233.183.91",
	            "64.233.185.19",
	            "64.233.189.44",
	            "66.102.1.103",
	            "66.102.9.115",
	            "66.249.81.101",
	            "66.249.89.83",
	            "66.249.91.99",
	            "66.249.93.190",
	            "72.14.203.107",
	            "72.14.205.113",
	            "72.14.255.107",
	            "toolbarqueries.google.com",
	            };

	/**
	 * Default constructor
	 */
	public PageRankService() {

	}

	/**
	 * Must receive a domain in form of: "http://www.51youlian.com"
	 * @param domain - (String)
	 * @return PR rating (int) or -1 if unavailable or internal error happened.
	 */
	public int getGooglePR(String domain) {
		 
		String result = "";
	 
		JenkinsHash jenkinsHash = new JenkinsHash();
		long hash = jenkinsHash.hash(("info:" + domain).getBytes());
	 
		//Append a 6 in front of the hashing value.
		String url = "http://toolbarqueries.google.com/tbr?client=navclient-auto&hl=en&"
		   + "ch=6" + hash + "&ie=UTF-8&oe=UTF-8&features=Rank&q=info:" + domain;
	 
		System.out.println("Sending request to : " + url);
	 
		try {
			URLConnection conn = new URL(url).openConnection();
	 
			BufferedReader br = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
	 
			String input;
			while ((input = br.readLine()) != null) {
	 
				// What Google returned? Example : Rank_1:1:9, PR = 9
				System.out.println("###################"+input);
	 
				result = input.substring(input.lastIndexOf(":") + 1);
			}
	 
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	 
		if ("".equals(result)) {
			return 0;
		} else {
			return Integer.valueOf(result);
		}
	 
	  }
	
	public void getAlexa(String domain){
		String url = "http://data.alexa.com/data/+wQ411en8000lA?cli=10&dat=snba&ver=7.0&cdt=alx_vw=20&"
            + "wid=12206&act=00000000000&bw=964&t=0&ttl=35371&vis=1&rq=4&url="+domain;
       	 
    		try {
    			URLConnection conn = new URL(url).openConnection();
    	 
    			BufferedReader br = new BufferedReader(new InputStreamReader(
    				conn.getInputStream()));
    	 
    			String input;
    			while ((input = br.readLine()) != null) {
    				System.out.println("*********************"+input);
    	 
    				//result = input.substring(input.lastIndexOf(":") + 1);
    			}
    	 
    		} catch (Exception e) {
    			System.out.println(e.getMessage());
    		}

	}


	public static void main(String [] args) {
		PageRankService obj = new PageRankService();
		//System.out.println(obj.getPR("qq.com"));
		obj.getAlexa("jingdong.cn");
	}
}
