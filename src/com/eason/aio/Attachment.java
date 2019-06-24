package com.eason.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class Attachment {

	private AsynchronousServerSocketChannel serverSocketChannel;
	public AsynchronousServerSocketChannel getServerSocketChannel() {
		return serverSocketChannel;
	}
	public void setServerSocketChannel(AsynchronousServerSocketChannel serverSocketChannel) {
		this.serverSocketChannel = serverSocketChannel;
	}
	public AtomicInteger getNum() {
		return num;
	}
	public void setNum(AtomicInteger num) {
		this.num = num;
	}
	private AtomicInteger num;
	private long startTime;
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	private ByteBuffer byteBuffer;
	public ByteBuffer getByteBuffer() {
		return byteBuffer;
	}
	public void setByteBuffer(ByteBuffer byteBuffer) {
		this.byteBuffer = byteBuffer;
	}
}
