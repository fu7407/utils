package com.zzf.common.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
/**
 * �ͻ��˳���
 * @author Administrator
 *
 */
public class HelloClient {

	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception {
		Socket client = new Socket("localhost",8888);
		BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));//���շ���˷�������Ϣ
		PrintStream out = new PrintStream(client.getOutputStream());//�����������Ϣ
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));//�Ӽ��̽�������
		boolean flag = true;
		while(flag){
			System.out.println("������Ϣ��");
			String str = input.readLine();//�Ӽ��̽�������
			out.println(str);//�����������Ϣ
			if("bye".equals(str)){
				flag = false;
			}else{
				String echo = buf.readLine();//���շ���˷�������Ϣ
				System.out.println(echo);
			}
			client.close();
			buf.close();
		}
	}

}
