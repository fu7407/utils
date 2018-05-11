package com.zzf.common.net;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class NetDemo {
	
	public void inetAddressDemo()throws Exception{
		InetAddress locAdd = InetAddress.getLocalHost();
		System.out.println("����IP��ַ��"+locAdd.getHostAddress());
		InetAddress remAdd = InetAddress.getByName("www.mldnjava.cn");
		System.out.println("mldnjava��IP��ַ��"+remAdd.getHostAddress());
	}
	
	public void urlDemo()throws Exception{
		URL url = new URL("http","www.mldnjava.cn",80,"/curriculum.htm");
		InputStream input = url.openStream();
		byte [] b = new byte[100];
		for(int i=0;i<b.length;i++){
			b[i] = (byte)input.read();
		}
		System.out.println("����Ϊ��"+new String(b));
	}
	
	public void urlConnectionDemo()throws Exception{
		URL url = new URL("http://www.mldnjava.cn");
		URLConnection urlCon = url.openConnection();
		System.out.println("���ݴ�С��"+urlCon.getContentLength());
		System.out.println("�������ͣ�"+urlCon.getContentType());
	}
	
	/**
	 * ���롢����
	 *
	 */
	public void encodDemo()throws Exception{
		String str = "hello,����";
		String encod = URLEncoder.encode(str, "UTF-8");
		System.out.println("����֮������ݣ�"+encod);
		String decod = URLDecoder.decode(encod, "UTF-8");
		System.out.println("����֮������ݣ�"+decod);
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args)throws Exception{
		NetDemo net = new NetDemo();
		//net.inetAddressDemo();
		//net.urlDemo();
		//net.urlConnectionDemo();
		net.encodDemo();
	}
}
