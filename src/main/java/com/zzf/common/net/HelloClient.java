package com.zzf.common.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * 客户端程序
 * 
 * @author Administrator
 *
 */
public class HelloClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Socket client = new Socket("localhost", 8888);
		BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));// 接收服务端发来的信息
		PrintStream out = new PrintStream(client.getOutputStream());// 向服务端输出信息
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));// 从键盘接收数据
		boolean flag = true;
		while (flag) {
			System.out.println("输入信息：");
			String str = input.readLine();// 从键盘接收数据
			out.println(str);// 向服务端输出信息
			if ("bye".equals(str)) {
				flag = false;
			} else {
				String echo = buf.readLine();// 接收服务端发来的信息
				System.out.println(echo);
			}
			client.close();
			buf.close();
		}
	}

}
