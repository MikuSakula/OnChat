package OnChat;
import java.io.*;
import java.net.*;
public class Utils {
	public static void close(Closeable...targets) {
		for(Closeable target:targets) {
			try {
				if(null!=target)
					target.close();
				
			}catch(Exception e) {
				
			}
		}
	}

}
