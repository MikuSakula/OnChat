package OnChat;

import java.net.*;
import java.io.*;
public class Client {

	public static void main(String[] args) throws Exception{
		System.out.println("------Client-----");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("please scan your name");
		String uname = br.readLine();
		Socket client  = new Socket("localhost",8888);
		new Thread(new Send(client,uname)).start();
		new Thread(new Receive(client)).start();
		


	}

}
