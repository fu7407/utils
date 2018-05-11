package com.zzf.common.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * ���̷߳������˳���
 * @author Administrator
 *
 */
public class HelloServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		ServerSocket server = new ServerSocket(8888);
		Socket client = null;
		PrintStream out = null;
		BufferedReader buf = null;
		boolean f = true;
		while(f){//�����ƽ��տͻ�������
			System.out.println("���������У��ȴ��ͻ�������......");
			client = server.accept();//���տͻ�������
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
		}
		server.close();
	}

}
