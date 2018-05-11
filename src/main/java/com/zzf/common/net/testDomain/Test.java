package com.zzf.common.net.testDomain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.Gson;
import com.zzf.common.net.HttpClient;
import com.zzf.common.net.testDomain.req.BaseReqArg;
import com.zzf.common.net.testDomain.req.BaseRespArg;
import com.zzf.common.net.testDomain.req.CheckDomainReq;
import com.zzf.common.net.testDomain.req.CheckDomainResp;
import com.zzf.common.net.testDomain.req.RegisterReq;

public class Test {

    private static final String ENCODING = "utf-8";
    private static final String SUFFIX = ".wang";
	
	//正式环境
	public static final String URLPATH = "http://dl.yuminghome.com/";
	public static final String USERID="3007";
	public static final String APIKEY="3dcc966b6b4a7ffc824958e09c9b2927";
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String sourcePath = "e:"+File.separator+"domain"+File.separator+"abc.txt";
		String targetPath = "e:"+File.separator+"domain"+File.separator+"abcNew.txt";
		check(sourcePath, targetPath);
	}
	
	public static void check(String sourcePath, String targetPath)throws Exception{
		FileInputStream file = new FileInputStream(sourcePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(file)); 
		String domain = "";
		File newfile = new File(targetPath);
		BufferedWriter bw = new BufferedWriter(new FileWriter(newfile));
		while((domain=br.readLine())!=null){     
            if(domain!=null && !"".equals(domain.trim())){  
        		if(!CheckDomain(domain+SUFFIX)){
        			bw.write(domain);
        			bw.newLine();
        			//System.out.println(domain);
        			//Register(domain+SUFFIX);
        		}else{
        			//System.out.println(domain+"已被注册"+(j++));
        		}   
            }     
        }
		bw.close();
		System.out.println("执行完毕！！！");
	}
	
	public static <T> T com(BaseReqArg req,String url,Class<T> classOfT)throws Exception{
		Gson gs = new Gson();
		String jsonstr = gs.toJson(req);
		byte[] b =	HttpClient.PostData(new URL(url), jsonstr.getBytes(ENCODING));
		return gs.fromJson(new String(b, ENCODING), classOfT);
	}
	
	/**
	 * 检测能否注册(false:可以注册)
	 * @throws Exception
	 */
	public static boolean CheckDomain(String domain)throws Exception{
		String url = Test.URLPATH + "API/ApiDomain/checkDomain.json";
		CheckDomainReq req = new CheckDomainReq();
		req.userid = Test.USERID;
		req.apikey = Test.APIKEY;
		req.domain = domain;
		CheckDomainResp  resp = com(req,url,CheckDomainResp.class);
		if(resp.errorCode==0){
			return resp.isRegistered;
		}else{
			//System.out.println("CheckDomain错误信息："+resp.errorMsg);
			return true;
		}
	}
	
	/**
	 * 注册
	 * @throws Exception
	 */
	public static void Register(String domain)throws Exception{
		String url = Test.URLPATH + "API/ApiDomain/register.json";
		RegisterReq req = new RegisterReq();
		req.userid = Test.USERID;
		req.apikey = Test.APIKEY;
		req.domain = domain;
		req.years = 1;
		req.contactId = 3L;//3:国际临时模板  4：国内临时模板
		BaseRespArg resp = com(req,url,BaseRespArg.class);
		if(resp.errorCode==0){
		}else{
			System.out.println("Register错误信息："+resp.errorMsg);
		}
	}
	
}
