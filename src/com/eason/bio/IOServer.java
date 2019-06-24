package com.eason.bio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.eason.nio.NIOUtil;

public class IOServer implements Runnable{

	AtomicInteger num = new AtomicInteger(0);
	@Override
	public void run() {
		startServer();
	}

	private void startServer() {
		long startTime =  System.currentTimeMillis();
		ServerSocket serverSocket = null;
		List<Thread> list = new ArrayList<>();
		Thread thread = null;
		
		try {
			serverSocket = new ServerSocket(NIOUtil.PORT);
			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("get connection with "+ socket.getRemoteSocketAddress());
				thread = new Thread(new HandleRequestThread(serverSocket, socket, startTime));
				list.add(thread);
				thread.start();
			}
		} catch (Exception e) {
			System.out.println("Exception occurred in server");
		} finally {
			try {
				if (serverSocket != null) {
					serverSocket.close();
				}
				Thread.sleep(NIOUtil.SERVER_SLEEPTIME);
				System.out.println("server receive " + num.get() + " reqest and stop...");
				System.out.println(num.get()/NIOUtil.TESTTIME);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private class HandleRequestThread implements Runnable{

		private Socket socket;
		private long startTime;
		private ServerSocket serverSocket;
		public HandleRequestThread(ServerSocket serverSocket, Socket socket, long startTime) {
			this.socket = socket;
			this.startTime = startTime;
			this.serverSocket = serverSocket;
		}
		
		@Override
		public void run() {
			try {
				handleRequest();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void handleRequest() throws IOException {
			BufferedReader br = null;
			BufferedWriter bw = null;
			while (socket.isConnected()) {
				num.incrementAndGet();
				
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				br.readLine();
				System.out.println("server receive client message");
				String newData = System.currentTimeMillis() + " server return message to client!";
				System.out.println(newData);
				bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				bw.write(newData+"\n");
				bw.flush();
				
				long currentTime = System.currentTimeMillis();
				if (currentTime - startTime > 1000*NIOUtil.TESTTIME) {
					br.close();
					bw.close();
					if (serverSocket != null) {
						serverSocket.close();
					}
					break;
				}
			}
			
		}
	}

}
