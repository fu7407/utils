package com.zzf.common.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class EchoThread implements Runnable {

	private Socket client = null;
	
	public EchoThread(Socket client) {
		super();
		this.client = client;
	}

	public void run() {
		PrintStream out = null;
		BufferedReader buf = null;
		try {
			buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintStream(client.getOutputStream());
			boolean flag = true;
			while(flag){
				String str = buf.readLine();//���Ͻ��տͻ��˷�������Ϣ
				if(str==null||"".equals(str)){
					flag = false;
				}else{
					if("bye".equals(str)){
						flag = false;
					}else{
						out.println("ECHO:"+str);//��ͻ��˻���ʾ��Ϣ
					}
				}
			}
			out.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
