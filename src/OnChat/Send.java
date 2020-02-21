package OnChat;
import java.io.*;
import java.net.*;
public class Send implements Runnable{
	private String uname = "";
	private BufferedReader console ;
	private DataOutputStream dos;
	private Socket client;
	private boolean isRunning =true;
	public Send(Socket client,String uname) {
		this.client = client;
		this.uname = uname;
		this.isRunning = true;
		console = new BufferedReader(new InputStreamReader(System.in));
		try {
			dos = new DataOutputStream(client.getOutputStream());
			send(uname);
		} catch (IOException e) {
			System.out.println("");
			this.release();
		}

	}

	public void run() {
		while(isRunning) {
			String msg = getStrFromConsole();
			if(!msg.equals("")) {
				send(msg);
			}
		}
		
	}
	private void send(String msg) {
		try {
			dos.writeUTF(msg);
			dos.flush();			
		} catch (IOException e) {
			System.out.println("");
			release();
		}
	}
	private String getStrFromConsole() {
		try {
			return console.readLine();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	private void release(){
		this.isRunning =false;
		Utils.close(dos,client);
		
	}

}
