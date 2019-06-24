package com.eason.aio.test;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.Channels;
import java.util.concurrent.Future;

public class AIOTest {
	
	private static void clientStart() {
		try {
			InetSocketAddress hostAddress = new InetSocketAddress(InetAddress
					.getByName("127.0.0.1"), 2583);
			AsynchronousSocketChannel clientSocketChannel = AsynchronousSocketChannel
					.open();
			Future<Void> connectFuture = clientSocketChannel
					.connect(hostAddress);
			connectFuture.get(); // Wait until connection is done.
			OutputStream os = Channels.newOutputStream(clientSocketChannel);
			ObjectOutputStream oos = new ObjectOutputStream(os);
			for (int i = 0; i < 5; i++) {
				oos.writeObject("Look at me " + i);
				Thread.sleep(1000);
			}
			oos.writeObject("EOF");
			oos.close();
			clientSocketChannel.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void serverStart() {
		try {
			InetSocketAddress hostAddress = new InetSocketAddress(InetAddress
					.getByName("127.0.0.1"), 2583);
			AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel
					.open().bind(hostAddress);
			while (true) {
				System.out.println("wait connection");
				Future<AsynchronousSocketChannel> serverFuture = serverSocketChannel
						.accept();
				System.out.println("accept connection");
				//future get method will block until server accept the client
				final AsynchronousSocketChannel clientSocket = serverFuture.get();
				System.out.println(clientSocket.getLocalAddress());
				if ((clientSocket != null) && (clientSocket.isOpen())) {
					InputStream connectionInputStream = Channels
							.newInputStream(clientSocket);
					ObjectInputStream ois = null;
					ois = new ObjectInputStream(connectionInputStream);
					while (true) {
						Object object = ois.readObject();
						if (object.equals("EOF")) {
							clientSocket.close();
							break;
						}
						System.out.println("Received :" + object);
					}
					ois.close();
					connectionInputStream.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		Thread serverThread = new Thread(new Runnable() {
			@Override
			public void run() {
				serverStart();
			}
		});
		serverThread.start();
		Thread clientThread = new Thread(new Runnable() {
			@Override
			public void run() {
				clientStart();
			}
		});
		clientThread.start();
		clientThread = new Thread(new Runnable() {
			@Override
			public void run() {
				clientStart();
			}
		});
		clientThread.start();
	}

}
