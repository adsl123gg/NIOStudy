package com.eason.aio.test;

import com.eason.bio.IOClient;
import com.eason.nio.NIOUtil;

public class AIOMain {

	public static void main(String[] args) {
		AIOServer server = new AIOServer();
		server.startServer();
		
		Thread thread = null;
		for (int i = 0; i < 50; i++) {
			thread = new Thread(new IOClient());
			thread.start();
		}
		
		try {
			thread.join();
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("server received " + server.num.get() + " request and stop...");
		System.out.println("tps is " + server.num.get()/NIOUtil.TESTTIME);
	}

}
