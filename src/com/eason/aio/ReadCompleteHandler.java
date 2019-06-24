package com.eason.aio;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.atomic.AtomicInteger;

public class ReadCompleteHandler implements CompletionHandler<Integer, Attachment>{

	AsynchronousSocketChannel clientSocket;
	public ReadCompleteHandler(AsynchronousSocketChannel clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	@Override
	public void completed(Integer result, Attachment attachment) {
		if (result < 1) {
			System.out.println("server read handler completed but copy data failed...");
			return;
		}
		
		ByteBuffer byteBuffer = attachment.getByteBuffer();
		AtomicInteger num = attachment.getNum();
		if (clientSocket != null && clientSocket.isOpen()) {
			clientSocket.read(byteBuffer, attachment, this);
		}
		
		System.out.println("server read handler completed");
		num.incrementAndGet();
		
		byte[] reqBytes = new byte[result];
		byteBuffer.flip();
		byteBuffer.get(reqBytes);
		System.out.println("server receive client message");
		
		try {
			String response = System.currentTimeMillis() + " server return message to client!\n";
			byte[] respBytes = response.getBytes("utf-8");
			byteBuffer.clear();
			byteBuffer.put(respBytes);
			byteBuffer.flip();
			clientSocket.write(byteBuffer, attachment, new WriteCompleteHandler(clientSocket));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void failed(Throwable exc, Attachment attachment) {
		System.out.println("server read handler failed");
	}
}
