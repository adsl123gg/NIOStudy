package com.eason.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class NIOClient implements Runnable{
	
	int SIZE = 48;
	byte bs[] = new byte[SIZE];
	int size = 0;

	public void sendHttpRequest() {
		SocketChannel socketChannel = null;
		
		try {
			socketChannel = SocketChannel.open();
			socketChannel.connect(new InetSocketAddress(NIOUtil.PORT));
			socketChannel.configureBlocking(false);
			
			ByteBuffer buf = ByteBuffer.allocate(48);
			
			while (socketChannel.isConnected()) {
				sendData(socketChannel, buf);
				
				buf.flip();
				recvData(socketChannel, buf);
			}
			
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("Exception occurred in client");
		} finally {
			try {
				socketChannel.close();
//				System.out.println("client close");
			} catch (Exception e) {
//				e.printStackTrace();
				System.out.println("Exception occurred in client");
			}
		}
	}

	private void sendData(SocketChannel socketChannel, ByteBuffer buf) throws InterruptedException {
		Thread.sleep(NIOUtil.CLIENT_SLEEPTIME);
		
		String newData = System.currentTimeMillis() + " client send message to server!";
		System.out.println(newData);
		NIOUtil.sendData(socketChannel, buf, newData);
		buf.clear();
	}
	
	private void recvData(SocketChannel socketChannel, ByteBuffer buf) throws IOException {
		if (!socketChannel.isConnected()) {
			return;
		}
		boolean flag = false;
		while (socketChannel.read(buf) != -1) {
			buf.flip();
			while (buf.hasRemaining()) {
				size = appendAndEcho(buf.get(), bs, size);
				if (size == 0) {
					flag = true;
					break;
				}
			}
			if (flag) {
				break;
			}
			buf.clear();
		}
	} 
	
	private static int appendAndEcho(byte b, byte[] bs, int size) {
		bs[size] = b;
		size++;

		if ((char)b == '!') {
			System.out.println("client receive server message");
//			System.out.println(new String(bs));
			Arrays.fill(bs, (byte)0);
			size = 0;
		}
		return size;
	}

	@Override
	public void run() {
		sendHttpRequest();
	}
}
