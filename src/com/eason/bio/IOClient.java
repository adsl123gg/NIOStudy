package com.eason.bio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.eason.nio.NIOUtil;

public class IOClient implements Runnable {
	   public void sendHttpRequest() {
		   Socket s = null;
	      try {
	         s = new Socket("127.0.0.1", NIOUtil.PORT);
	         
	         while (s.isConnected()) {
	        	 try {
					Thread.sleep(NIOUtil.CLIENT_SLEEPTIME);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	 
	        	 InputStream is = s.getInputStream();
		         OutputStream os = s.getOutputStream();
		         
		         String newData = System.currentTimeMillis() + " client send message to server!";
		         System.out.println(newData);
		         BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
		         bw.write(newData+"\n");
		         bw.flush();
		         
		         BufferedReader br = new BufferedReader(new InputStreamReader(is));
		         br.readLine();
		         System.out.println("client receive server message");
			}
	         
	      } catch (Exception e) {
	    	  System.out.println("Exception occurred in client");
	      }finally {
			try {
				if (s != null && !s.isClosed()) {
					s.close();
				}
				System.out.println("client close");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Exception occurred in client");
			}
		}
	   }

	@Override
	public void run() {
		sendHttpRequest();
	}
}
