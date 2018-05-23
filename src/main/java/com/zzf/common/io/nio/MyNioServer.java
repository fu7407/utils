package com.zzf.common.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class MyNioServer {
	private Selector selector;
	private final static int port = 8686;
	private final static int BUF_SIZE = 10240;

	private void initServer() throws IOException {
		// 创建通道管理器对象
		this.selector = Selector.open();
		// 创建一个通道对象channel
		ServerSocketChannel channel = ServerSocketChannel.open();
		channel.configureBlocking(false);// 将通道设置为非阻塞
		channel.socket().bind(new InetSocketAddress(port));// 将通道绑定端口

		// 将上述的通道管理器和通道绑定，并未该通道注册OP_ACCEPT事件
		// 注册事件后，当该事件到达时，selector.select()会返还（一个key），如果该事件没有到达selector.select()会一直阻塞
		SelectionKey selectionKey = channel.register(selector, SelectionKey.OP_ACCEPT);

		while (true) {
			selector.select();// 这是一个阻塞方法，一会等待直到有数据可读，返回值是key的数量（可以有多个）
			Set keys = selector.selectedKeys();// 如果channel有数据了，将生成key放入keys集合中
			Iterator iterator = keys.iterator();
			while (iterator.hasNext()) {
				SelectionKey key = (SelectionKey) iterator.next();
				iterator.remove();// 拿到当前key实例后记得在迭代器中将这个元素删除，非常重要，否则会出错
				if (key.isAcceptable()) {// 判断当前key所代表的channel是否在acceptable状态，如果是就进行接收
					doAccept(key);
				} else if (key.isReadable()) {
					doRead(key);
				} else if (key.isWritable() && key.isValid()) {
					doWrite(key);
				} else if (key.isConnectable()) {
					System.out.println("连接成功！");
				}
			}
		}
	}

	public void doAccept(SelectionKey key) throws IOException {
		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
		System.out.println("ServerSocketChannel正在循环监听");
		SocketChannel clientChannel = serverChannel.accept();
		clientChannel.configureBlocking(false);
		clientChannel.register(key.selector(), SelectionKey.OP_READ);
	}

	public void doRead(SelectionKey key) throws IOException {
		SocketChannel clientChannel = (SocketChannel) key.channel();
		ByteBuffer byteBuffer = ByteBuffer.allocate(BUF_SIZE);
		long byteRead = clientChannel.read(byteBuffer);
		while (byteRead > 0) {
			byteBuffer.flip();
			byte[] data = byteBuffer.array();
			String info = new String(data).trim();
			System.out.println("从客户端发来的消息是：" + info);
			byteBuffer.clear();
			byteRead = clientChannel.read(byteBuffer);
		}
		if (byteRead == -1) {
			clientChannel.close();
		}
	}

	public void doWrite(SelectionKey key) throws IOException {
		ByteBuffer byteBuffer = ByteBuffer.allocate(BUF_SIZE);
		byteBuffer.flip();
		SocketChannel clientChannel = (SocketChannel) key.channel();
		while (byteBuffer.hasRemaining()) {
			clientChannel.write(byteBuffer);
		}
		byteBuffer.compact();
	}

	public static void main(String[] args) throws IOException {
		MyNioServer myNioServer = new MyNioServer();
		myNioServer.initServer();
	}
}
