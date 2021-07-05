/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class TCPServerController {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private volatile AtomicBoolean lock = new AtomicBoolean(false);
    private ObjectInputStream os;

    public TCPServerController(int port) {
        while (true) {
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("Server TCP with port : " + port + " is running...");
                listening();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void listening() {
        while (true) {
            try {
                System.out.println("accept wait accept");
                clientSocket = serverSocket.accept();
                System.out.println("client was accpeted ");
                System.out.println(clientSocket.getInetAddress());
                 os = new ObjectInputStream(clientSocket.getInputStream());
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            while (true) {
                                String data = (String) os.readObject();
//                            System.out.println(os.readObject());
                                System.out.println("----------------------------------------------");
                                System.out.println(data);
                                String[] array = data.split("\n");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
