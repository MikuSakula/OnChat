package OnChat;
import java.io.*;
import java.net.*;
public class Receive implements Runnable{

	private DataInputStream dis;
	private Socket client;
	private boolean isRunning = true;
	public Receive(Socket client) {
		this.client = client;
		try {
			dis = new DataInputStream(client.getInputStream());
		} catch (IOException e) {
			release();
			System.out.println("");
		}
	}
	
	private String receive() {
		String msg="";
		try {
			msg = dis.readUTF();
		}catch(IOException e ) {
			System.out.println("");
			release();
		}
		return msg;
	}

	public void run() {
		while(isRunning) {
			String msg = receive();
			if(!msg.equals("")) {
				System.out.println(msg);
			}
		}
		
	}
	private void release(){
		this.isRunning =false;
		Utils.close(dis,client);
		
	}
}
