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
    private static volatile AtomicInteger numMessOnSecond = new AtomicInteger(0);
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private volatile AtomicBoolean lock = new AtomicBoolean(false);
    private BufferedReader os;

    public TCPServerController(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server TCP with port : " + port + " is running...");

            listening();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void listening() {
            try {
                clientSocket = serverSocket.accept();
                System.out.println(clientSocket.getInetAddress());
                os = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public String readData() throws IOException, ClassNotFoundException {
        return (String) os.readLine();
    }


}
