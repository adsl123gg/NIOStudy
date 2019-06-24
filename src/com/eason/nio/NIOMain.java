package com.eason.nio;

import java.util.ArrayList;
import java.util.List;

public class NIOMain {
	public static void main(String[] args) throws InterruptedException {
		Thread thread = null;
		int NUM = 50;
		
		NIOServer nioServer = new NIOServer();
		Thread serverThread = new Thread(nioServer);
		serverThread.start();
		
		List<Thread> list = new ArrayList<>();
		for (int i = 0; i < NUM; i++) {
			thread = new Thread(new NIOClient());
			list.add(thread);
			thread.start();
		}
		
		serverThread.join();
		
		for (Thread th : list) {
			if (th.isAlive()) {
				th.interrupt();
			}
		}
		
		Thread.sleep(NIOUtil.SERVER_SLEEPTIME);
		System.out.println("server receive " + nioServer.num.get() + " reqest and stop...");
		
		return;
	}

}
