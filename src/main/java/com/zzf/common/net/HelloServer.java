package com.zzf.common.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 单线程服务器端程序
 * 
 * @author Administrator
 *
 */
public class HelloServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(8888);
		Socket client = null;
		PrintStream out = null;
		BufferedReader buf = null;
		boolean f = true;
		while (f) {// 无限制接收客户端连接
			System.out.println("服务器运行，等待客户端连接......");
			client = server.accept();// 接收客户端连接
			buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintStream(client.getOutputStream());
			boolean flag = true;
			while (flag) {
				String str = buf.readLine();// 不断接收客户端发来的信息
				if (str == null || "".equals(str)) {
					flag = false;
				} else {
					if ("bye".equals(str)) {
						flag = false;
					} else {
						out.println("ECHO:" + str);// 向客户端回显示信息
					}
				}
			}
			out.close();
			client.close();
		}
		server.close();
	}

}
