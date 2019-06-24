package com.eason.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import com.eason.nio.NIOUtil;

public class ConnectionCompleteHandler implements CompletionHandler<AsynchronousSocketChannel, Attachment>{

	@Override
	public void completed(AsynchronousSocketChannel clientSocket, Attachment attachment) {
		System.out.println("server AIO connection completed");
		
		AsynchronousServerSocketChannel serverSocket = attachment.getServerSocketChannel();
		if (serverSocket != null && serverSocket.isOpen()) {
			serverSocket.accept(attachment, this);
		}
		
		Attachment newAttachment = new Attachment();
		newAttachment.setServerSocketChannel(attachment.getServerSocketChannel());
		newAttachment.setNum(attachment.getNum());
		newAttachment.setStartTime(attachment.getStartTime());
		
		ByteBuffer readByteBuffer = ByteBuffer.allocate(NIOUtil.BUFFER_SIZE);
		newAttachment.setByteBuffer(readByteBuffer);
		clientSocket.read(readByteBuffer, newAttachment, new ReadCompleteHandler(clientSocket));
	}

	@Override
	public void failed(Throwable exc, Attachment attachment) {
		System.out.println("server AIO connection failed...");
	}
}
