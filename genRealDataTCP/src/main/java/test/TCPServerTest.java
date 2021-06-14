// Coded By :: A_Asaker
package test;
import java.io.*;
import java.net.*;

public class TCPServerTest{
	public static void main(String[] args){
		try{
			ServerSocket sock=new ServerSocket(1211);
			System.out.println("[+] Server Established!");
			Socket conn=sock.accept();//establishes connection
			System.out.println("[+] Client Connected.");
			//DataInputStream data = new DataInputStream(conn.getInputStream()));
			BufferedReader dataRead = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			BufferedWriter dataWrite = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String str_data="";
//			while(!str_data.equals("Done")){
//				str_data=data.readLine();
//				//str_data=data.readUTF();
//				System.out.println("=> "+str_data);
//				dataWrite.write("Server got "+data);
//			}

			while (true){
				dataWrite.write("data uchiha \n");
			}
//			sock.close();
		}
		catch(Exception e){System.out.println(e);}
	}

}

