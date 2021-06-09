/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 *
 * @author Ryan
 */
public class TCPServer {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;


    public TCPServer() {
        try {
            serverSocket = new ServerSocket(11003);
            System.out.println("Server TCP is running...");
            while (true) {
                listening();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listening() {
        try {
            clientSocket = serverSocket.accept();
            System.out.println(clientSocket.getInetAddress());
            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
            new Thread(() -> {
                try{
                    while (true){
                        oos.writeObject((new Random()).nextInt());
                        
                    
                        
                        Thread.sleep(50);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }).start();
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    

    private void commandClose() throws IOException, ClassNotFoundException {
        clientSocket.close();
    }

    public static void main(String[] args) {
        new TCPServer();
    }
}
