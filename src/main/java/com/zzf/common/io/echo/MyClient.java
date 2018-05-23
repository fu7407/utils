package com.zzf.common.io.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MyClient {
	public static void main(String[] args) throws IOException {
		Socket client = null;
		BufferedReader bufferedReader = null;
		PrintWriter printWriter = null;
		try {
			client = new Socket();
			client.connect(new InetSocketAddress("localhost", 8686));
			printWriter = new PrintWriter(client.getOutputStream(), true);
			printWriter.println("hello");
			printWriter.flush();

			bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			System.out.println("来自服务器的信息是：" + bufferedReader.readLine());
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
