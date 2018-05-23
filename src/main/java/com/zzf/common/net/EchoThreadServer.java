package com.zzf.common.net;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * 多线程服务端
 * 
 * @author Administrator
 *
 */
public class EchoThreadServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(8888);
		Socket client = null;
		boolean f = true;
		while (f) {// 无限制接收客户端连接
			System.out.println("服务器运行，等待客户端连接......");
			client = server.accept();// 接收客户端连接
			new Thread(new EchoThread(client)).start();
		}
		server.close();
	}

}
