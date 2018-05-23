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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
