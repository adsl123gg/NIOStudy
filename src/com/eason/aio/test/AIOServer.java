package com.eason.aio.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.Channels;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.atomic.AtomicInteger;

import com.eason.nio.NIOUtil;

public class AIOServer{

	AtomicInteger num = new AtomicInteger(0);
	AsynchronousServerSocketChannel server = null;
	long startTime = System.currentTimeMillis();
	
	public void startServer() {
		try {
//			ExecutorService threadPool = Executors.newFixedThreadPool(100);
//			AsynchronousChannelGroup asyncChannelGroup 
//					= AsynchronousChannelGroup.withThreadPool(threadPool);
//		server = AsynchronousServerSocketChannel.open(asyncChannelGroup).bind(new InetSocketAddress("127.0.0.1", NIOUtil.PORT));
		server = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress("127.0.0.1", NIOUtil.PORT));
		
			System.out.println("start accept");
			server.accept(server, new CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel>() {
				@Override
				public void completed(AsynchronousSocketChannel clientSocket, AsynchronousServerSocketChannel serverSocket) {
					System.out.println("accept completed");
					if (serverSocket.isOpen()) {
						serverSocket.accept(serverSocket, this);
					}
					
					while (clientSocket != null && clientSocket.isOpen()) {
						num.incrementAndGet();
						try {
							InputStream connectionInputStream = Channels
									.newInputStream(clientSocket);
							BufferedReader reader = new BufferedReader(new InputStreamReader(connectionInputStream));
							reader.readLine();
							System.out.println("server receive client message");
							String newData = System.currentTimeMillis() + " server return message to client!";
							System.out.println(newData);
							BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(Channels.newOutputStream(clientSocket)));
							bw.write(newData+"\n");
							bw.flush();
							
							if (System.currentTimeMillis() - startTime > 1000*NIOUtil.TESTTIME) {
								clientSocket.close();
							}
							
						} catch (IOException e) {
							e.printStackTrace();
						}
						
					}
					
				}

				@Override
				public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
					System.out.println("AIO failed...");
					
				}
			});
			
		} catch (IOException e) {
			System.out.println("Exception occurred in server");
		} 
	}

}
