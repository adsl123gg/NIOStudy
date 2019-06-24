package com.eason.aio;

import com.eason.bio.IOClient;
import com.eason.nio.NIOUtil;

public class AIOMain {

	public static void main(String[] args) throws InterruptedException {
		int threadNum = 50;
		
		AIOServer server = new AIOServer();
		server.startServer();
		
		Thread thread = null;
		for (int i = 0; i < threadNum; i++) {
			thread = new Thread(new IOClient());
			thread.join();
			thread.start();
		}
		
		while (thread.isAlive()) {
			Thread.sleep(3000);
		}
		
		System.out.println("main thread running");
		
		System.out.println("server received " + server.num.get() + " request and stop...");
		System.out.println("tps is " + server.num.get()/NIOUtil.TESTTIME);
	}

}
