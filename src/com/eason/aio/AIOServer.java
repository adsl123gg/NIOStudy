package com.eason.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

import com.eason.nio.NIOUtil;

public class AIOServer{

	AtomicInteger num = new AtomicInteger(0);
	AsynchronousServerSocketChannel server = null;
	long startTime = System.currentTimeMillis();
	
	public void startServer() {
		try {
//			ExecutorService threadPool = Executors.newFixedThreadPool(10);
//			AsynchronousChannelGroup asyncChannelGroup 
//					= AsynchronousChannelGroup.withThreadPool(threadPool);
			
//			server = AsynchronousServerSocketChannel.open(asyncChannelGroup).bind(new InetSocketAddress("127.0.0.1", NIOUtil.PORT));
			server = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress("127.0.0.1", NIOUtil.PORT));
			
			System.out.println("start accept");
			
			Attachment attachment = new Attachment();
			attachment.setServerSocketChannel(server);
			attachment.setNum(num);
			attachment.setStartTime(startTime);
			
			server.accept(attachment, new ConnectionCompleteHandler());
		} catch (IOException e) {
			System.out.println("Exception occurred in server");
		} 
	}

}

