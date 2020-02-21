package OnChat;

import java.net.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.*;

public class Chat {
	
	private static CopyOnWriteArrayList<Channel> all = new CopyOnWriteArrayList<Channel>();

	public static void main(String[] args)throws Exception {
		System.out.println("------Server-----");
		ServerSocket server = new ServerSocket(8888);
		
		while(true) {
			Socket client = server.accept();
			System.out.println("one client has been set");
			Channel c = new Channel(client);
			all.add(c);
			new Thread(c).start();			
		}

	}
	static class Channel implements Runnable{
		private DataInputStream dis;
		private DataOutputStream dos;
		private Socket client;
		private boolean isRunning ;
		private String uname;
		public Channel(Socket client) {
			this.client = client;
			try {
				dis = new DataInputStream(client.getInputStream());
				dos = new DataOutputStream(client.getOutputStream());	
				this.isRunning = true;
				this.uname = receive();
				this.send("welcome to Chat");
				this.sendOthers(this.uname+"come to the Chat",true);
			} catch (IOException e1) {
				System.out.println("creat wrong");
				release();
			}
			
		}
		private String receive() {
			String msg = "";
			try {
				msg = dis.readUTF();
			} catch (IOException e) {
				System.out.println("reveive wrong");
				release();
			}
			return msg;
		}
		private void send(String msg) {
			try {
				dos.writeUTF(msg);
				dos.flush();
			} catch (IOException e) {
				System.out.println("send wrong");
				release();
			}
			return ;
		}
		private void sendOthers(String msg,boolean isSys) {
			boolean isPrivate = msg.startsWith("@");
			if(isPrivate) {  // @xxx:msg
				int idx = msg.indexOf(":");
				String targetNmae = msg.substring(1,idx);
				msg = msg.substring(idx+1);
				for(Channel other:all) {
					if(other.uname.equals(targetNmae)) {
						other.send(this.uname+"to you say:"+msg);
					}
				}
				
			}else {
				for(Channel other:all) {
					if(other==this) {
						continue;
					}else {
						if(!isSys)
							other.send(uname + " to everybody:"+msg);
						else {
							other.send(msg);
						}
					}
				}
			}
		}
		private void release() {
			this.isRunning = false;
			Utils.close(dis,dos,client);
			all.remove(this);
			sendOthers(this.uname+"exit the char",true);
			return ;
		}
		public void run() {
			while(isRunning) {
				String msg = receive();
				if(!msg.equals("")) {
					sendOthers(msg,false);
				}
			}	
		}
	}

}
