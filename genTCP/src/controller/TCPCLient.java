/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Ryan
 */
public class TCPCLient {
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    public TCPCLient(InetAddress IP, int port){
        System.out.println("ClientController()");
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
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public int readData() throws IOException, ClassNotFoundException{
        return (int)ois.readObject();
    }
    
   
    public int multiply(int a,int b) throws IOException, ClassNotFoundException{
        oos.writeObject("Multiply");
        oos.writeObject(a);
        oos.writeObject(b);
        return (int)ois.readObject();
    }
}
