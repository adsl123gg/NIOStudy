package com.eason.bio;

public class IOMain {

	public static void main(String[] args) throws InterruptedException {
		int NUM = 50;
		Thread thread = null;
		
		IOServer ioServer = new IOServer();
		Thread serverThread = new Thread(ioServer);
		serverThread.start();
		
		for (int i = 0; i < NUM; i++) {
			thread = new Thread(new IOClient());
			thread.start();
		}
		
	}
}
