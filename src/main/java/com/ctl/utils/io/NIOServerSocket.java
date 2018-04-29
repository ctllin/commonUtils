package com.ctl.utils.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.Charset;

public class NIOServerSocket {
	// 用于检测所有的Channel状态Selector
	private Selector selector = null;
	// 定义实现编码、解码的字符集对象
	private Charset charset = Charset.forName("gbk");

	public void init() throws IOException {
		selector = Selector.open();
		// 通过open方法来打开一个未绑定的ServerSocketChannel实例
		ServerSocketChannel server = ServerSocketChannel.open();
		// InetSocketAddress isa=new InetSocketAddress("127.0.0.1",22);
		InetSocketAddress isa = new InetSocketAddress("192.168.42.1", 1234);
		server.socket().bind(isa);
		// 设置ServerScoket以非阻塞的方式工作
		server.configureBlocking(false);
		// 将server注册到指定Selector对象
		// static int OP_ACCEPT 用于套接字接受操作的操作集位。
		// static int OP_CONNECT 用于套接字连接操作的操作集位。
		// static int OP_READ 用于读取操作的操作集位。
		// static int OP_WRITE 用于写入操作的操作集位。
		server.register(selector, SelectionKey.OP_ACCEPT);
		//while ((selectorSelectSize=selector.select()) > 0) {//只要selecet。select()<=0就会阻塞到这里
		while (selector.select() > 0) {//只要selecet。select()<=0就会阻塞到这里
			//System.out.println("selector.select():"+selectorSelectSize);
			// 依次处理selector上的每个已选择的SelectionKey
			for (SelectionKey sk : selector.selectedKeys()) {
				// 从selector上的以选择key集中删除正在处理的SelectionKey
				selector.selectedKeys().remove(sk);
				// 如果sk对应的通道包含客户端的连接请求
				if (sk.isAcceptable()) {
					// 调用accept方法接受连接，产生服务器对应的SocketChannel
					SocketChannel sc = server.accept();
					System.err.println("客户端连接channel:"+sc);
					// 设置采用非阻塞模式
					sc.configureBlocking(false);
					// 将该SocketChannel也注册到selector
					sc.register(selector, SelectionKey.OP_READ);
					// 将sk对应的Channel设置成准备接受其他请求
					sk.interestOps(SelectionKey.OP_ACCEPT);

				}
				// 如果sk对应的通道有数据需要读取
				if (sk.isReadable()) {
					// 获取该SelectionKey对应的Channel，该Channel中有可读的数据
					SocketChannel sc = (SocketChannel) sk.channel();
					// 定义准备执行读取数据的bytebuffer
					ByteBuffer buff = ByteBuffer.allocate(1024);
					String content = "";
					try {
						while (sc.read(buff) > 0) {
							buff.flip();
							content += charset.decode(buff).toString();
						}
						System.out.println(content);
						// 将sk对用的Channel设置成准备下一次读取
						sk.interestOps(SelectionKey.OP_READ);
					} catch (IOException ex) {//客户端连接关闭
						sk.cancel();
						if (sk.channel() != null) {
							sk.channel().close();
						}
						System.err.println(ex.getMessage());
						//ex.printStackTrace();
					}
					// 如果content的长度大于0，即聊天内容不为空
					if (content.length() > 0) {//回向每一个客户端发送，如果想向指定的客户端发送需要选择指定的 targetChannel
						// 遍历该selector里面注册的所有SelectKey
						for (SelectionKey key : selector.keys()) {
							// 获取该key的channel
							Channel targetChannel = key.channel();
							System.err.println("key:"+key+" channel:"+targetChannel);
							// 如果该channel是SocketChannel对象
							if (targetChannel instanceof SocketChannel) {
								// 将读取的内容写入到该Channel中
								SocketChannel dest = (SocketChannel) targetChannel;
								dest.write(charset.encode("你还好吗?"));
							}
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		try {
			new NIOServerSocket().init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}