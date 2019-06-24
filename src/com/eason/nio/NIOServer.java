package com.eason.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class NIOServer implements Runnable{

	AtomicInteger num = new AtomicInteger(0);
	public void startServer() {
		int SIZE = 48;
		long startTime = System.currentTimeMillis();
		
		ServerSocketChannel serverSocketChannel = null;
		Selector selector = null;
		try {
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.socket().bind(new InetSocketAddress(NIOUtil.PORT));
			serverSocketChannel.configureBlocking(false);
			
			selector = Selector.open();
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

			ByteBuffer buf = ByteBuffer.allocate(SIZE);
		    byte bs[] = new byte[SIZE];
		    int size = 0;
		    
		    long currentTime = 0;
		    
			while(true){
				selector.select();
				
				Set<SelectionKey> selectionKeys = selector.selectedKeys();
				Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
				
				while (keyIterator.hasNext()) {
					SelectionKey key = keyIterator.next();
					if (key.isAcceptable()) {
						SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
						socketChannel.configureBlocking(false);
						socketChannel.register(selector, SelectionKey.OP_READ);
						
						System.out.println("get connection with "+ socketChannel.getRemoteAddress());
					}else if (key.isReadable()) {
						num.incrementAndGet();
						SocketChannel socketChannel = (SocketChannel) key.channel();
						size = handleRequest(buf, bs, size, socketChannel);
					}
					
					keyIterator.remove();
				}
				
				currentTime = System.currentTimeMillis();
				if (currentTime - startTime > 1000*NIOUtil.TESTTIME) {
					break;
				}
				
			}
		} catch (IOException e) {
//			e.printStackTrace();
			System.out.println("Exception occurred in server");
		} finally {
			try {
				close(serverSocketChannel, selector);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println(num.get()/NIOUtil.TESTTIME);
	}


	private void close(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
		selector.close();
		serverSocketChannel.close();
	}


	private int handleRequest(ByteBuffer buf, byte[] bs, int size, SocketChannel socketChannel) throws IOException {
		boolean flag = false;
		while (socketChannel.read(buf) != -1) {
			buf.flip();
			while (buf.hasRemaining()) {
				size = appendAndEcho(buf.get(), bs, size);
				
				if (size == 0) {
					sendResponse(socketChannel, buf);
					flag = true;
					break;
				}
			}
			if (flag) {
				break;
			}
			
			buf.clear();
		}
		return size;
	}


	private void sendResponse(SocketChannel socketChannel, ByteBuffer buf) {
		String newData = System.currentTimeMillis() + " server return message to client!";
		System.out.println(newData);
		
		NIOUtil.sendData(socketChannel, buf, newData);
		buf.clear();
	}


	private int appendAndEcho(byte b, byte[] bs, int size) {
		bs[size] = b;
		size++;
		
		if ((char)b == '!') {
			System.out.println("server receive client message");
//			System.out.println(new String(bs));
			Arrays.fill(bs, (byte)0);
			size = 0;
		}

		return size;
	}


	@Override
	public void run() {
		startServer();
	}
}
