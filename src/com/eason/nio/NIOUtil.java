package com.eason.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOUtil {
	public static final int BUFFER_SIZE = 48;
	
	public static final int CLIENT_SLEEPTIME = 5;
	public static final int SERVER_SLEEPTIME = 3000;
	public static final int TESTTIME = 10;
	public static final int PORT = 8067;
	
	public static void sendData(SocketChannel socketChannel, ByteBuffer buf, String data) {
		buf.clear();
		buf.put(data.getBytes());

		buf.flip();

		while(buf.hasRemaining()) {
			try {
				socketChannel.write(buf);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
