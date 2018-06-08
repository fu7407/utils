package com.zzf.common.io;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class InputStreamDemo {

	private final static String FILE_PATH = "D:\\workspace\\utils\\src\\main\\resources\\Demo.txt";

	public static void main(String[] args) throws IOException {
		InputStreamDemo.bufferedInputStream();
	}

	public static void fileInputStream() throws IOException {
		FileInputStream in = new FileInputStream(FILE_PATH);
		int temp = 0;
		// read方法一次只读一个，不转换为char的话，将打出ASCII码
		while ((temp = in.read()) != -1) {
			System.out.println((char) temp);
		}
	}

	public static void bufferedInputStream() throws IOException {
		FileInputStream fis = new FileInputStream(FILE_PATH);
		BufferedInputStream bis = new BufferedInputStream(fis);
		// 自定义缓冲区
		byte[] buffer = new byte[3];
		System.out.println(new String(buffer, 0, bis.read(buffer)));

		// String context = "";
		// byte[] buffer = new byte[1024];
		// int flag = 0;
		// while ((flag = bis.read(buffer)) != -1) {
		// context += new String(buffer, 0, flag);
		// }
		// System.out.println(context);

		bis.close();
	}

}
