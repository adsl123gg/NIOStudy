package com.eason.aio;

import java.io.IOException;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import com.eason.nio.NIOUtil;

public class WriteCompleteHandler implements CompletionHandler<Integer, Attachment> {

	AsynchronousSocketChannel clientSocket;
	public WriteCompleteHandler(AsynchronousSocketChannel clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void completed(Integer result, Attachment attachment) {
		attachment.getByteBuffer().clear();
		System.out.println("server WriteCompleteHandler completed " + result);
		
		if (System.currentTimeMillis() - attachment.getStartTime() > 1000*NIOUtil.TESTTIME) {
			try {
				if (clientSocket != null && clientSocket.isOpen()) {
					clientSocket.shutdownInput();
					clientSocket.shutdownOutput();
					clientSocket.close();
				}

				AsynchronousServerSocketChannel server = attachment.getServerSocketChannel();
				if (server != null && server.isOpen()) {
					server.close();
				}
			} catch (IOException e) {
				System.out.println("serverSocketChannel close failed");
			}
		}
		
	}

	@Override
	public void failed(Throwable exc, Attachment attachment) {
		System.out.println("server WriteCompleteHandler failed");
		
	}

}
