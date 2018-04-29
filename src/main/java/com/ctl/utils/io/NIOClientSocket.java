package com.ctl.utils.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Scanner;

public class NIOClientSocket {
	// 定义检测SocketChannel的selector对象
	private Selector selector = null;
	// 定义处理编码和解码的字符集
	private Charset charset = Charset.forName("gbk");
	// 客户端socketchannel
	private SocketChannel sc = null;

	public void init() throws IOException {
		selector = Selector.open();
		InetSocketAddress isa = new InetSocketAddress("192.168.42.1", 1234);
		sc = SocketChannel.open(isa);
		sc.configureBlocking(false);
		sc.register(selector, SelectionKey.OP_READ);
		new ClientThread().start();
		Scanner scan = new Scanner(System.in);
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			sc.write(charset.encode(line));
		}
	}

	private class ClientThread extends Thread {
		public void run() {
			try {
				while (selector.select() > 0) {
					for (SelectionKey sk : selector.selectedKeys()) {
						// 删除正在处理的SelectionKey
						selector.selectedKeys().remove(sk);
						if (sk.isReadable()) {
							SocketChannel sc = (SocketChannel) sk.channel();
							ByteBuffer buff = ByteBuffer.allocate(1024);
							String content = "";
							while (sc.read(buff) > 0) {
								sc.read(buff);
								buff.flip();
								content += charset.decode(buff).toString();
							}
							System.out.println("服务端:" + content);
							sk.interestOps(SelectionKey.OP_READ);
						}
					}
				}
			} catch (IOException ex) {//ServerScoket关闭
				System.err.println(ex.getMessage());
				//ex.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		try {
			new NIOClientSocket().init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}