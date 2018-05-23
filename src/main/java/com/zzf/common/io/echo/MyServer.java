package com.zzf.common.io.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyServer {
	private static ExecutorService executorService = Executors.newCachedThreadPool();// 创建一个线程池

	private static class HandleMsg implements Runnable {// 一旦有新的客户端请求，创建这个线程进行处理
		Socket client;

		public HandleMsg(Socket client) {
			this.client = client;
		}

		@Override
		public void run() {
			BufferedReader bufferedReader = null;// 创建字符缓存输入流
			PrintWriter printWriter = null;// 创建字符写入流
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));// 获取客户端输入流
				printWriter = new PrintWriter(client.getOutputStream(), true);// 获取客户端的输出流，true是随时刷新
				String inputLine = null;
				long a = System.currentTimeMillis();
				while ((inputLine = bufferedReader.readLine()) != null) {
					printWriter.println(inputLine);
				}
				long b = System.currentTimeMillis();
				System.out.println("此线程花费了：" + (b - a) + "秒！");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					bufferedReader.close();
					printWriter.close();
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(8686);
		Socket client = null;
		while (true) {
			client = server.accept();
			System.out.println(client.getRemoteSocketAddress() + "地址的客户端连接成功！");
			executorService.submit(new HandleMsg(client));
		}
	}

}
