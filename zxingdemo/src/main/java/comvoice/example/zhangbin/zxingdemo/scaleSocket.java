package comvoice.example.zhangbin.zxingdemo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

import android.util.Log;

public class scaleSocket {
	
	public static String mySocket(String parameter,int timeout){
	    String result = "";
		Socket sc =null;
		try{
			sc = new Socket();
			SocketAddress isa = new InetSocketAddress(ApplicationApp.ipAdd,ApplicationApp.port);
			sc.connect(isa, timeout);
			PrintWriter outs = new PrintWriter(sc.getOutputStream());
			outs.print(parameter+"\r\n");
			outs.flush();
			BufferedReader ins = new BufferedReader(new InputStreamReader(
				sc.getInputStream()));
			result = ins.readLine();
			Log.i("test", "result" + result);
			sc.close();
		}catch(SocketTimeoutException e1){
			e1.printStackTrace();
			result = "*";
		}catch(Exception e){
			e.printStackTrace();
			result = "*";		
		}
		return result;
	}
}
