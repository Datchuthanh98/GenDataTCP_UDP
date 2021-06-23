/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Ryan
 */
public class TCPCLientController {
    private Socket socket;
    private ObjectInputStream ois;

    public TCPCLientController(InetAddress IP, int port){
        System.out.println("Clien TCP with"+port+ "is running");
        try{
            socket = new Socket(IP, port);
            System.out.println("Socket: "+IP.getHostAddress()+":"+port);
            getStream();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void getStream(){
        try{
            ois = new  ObjectInputStream(socket.getInputStream());

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public String readData() throws IOException, ClassNotFoundException {
        return (String) ois.readObject();
    }
    

}
