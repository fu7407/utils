package com.zzf.common.net;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * ���̷߳�������
 * @author Administrator
 *
 */
public class EchoThreadServer {

	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception {
		ServerSocket server = new ServerSocket(8888);
		Socket client = null;
		boolean f = true;
		while(f){//�����ƽ��տͻ�������
			System.out.println("���������У��ȴ��ͻ�������......");
			client = server.accept();//���տͻ�������
			new Thread(new EchoThread(client)).start();
		}
		server.close();
	}

}
