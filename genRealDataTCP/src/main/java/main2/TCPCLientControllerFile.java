package main2;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Ryan
 */
public class TCPCLientControllerFile {
    private Socket socket;
    private ObjectOutputStream ois;
//    private String data1="";
    StringBuilder data1 = new StringBuilder();
//    private String data2="";
    StringBuilder data2 = new StringBuilder();

    public TCPCLientControllerFile(int msg){
        try{

            try {
                File myObj = new File("data1_" + msg + ".txt");
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    data1.append(data).append("\n");
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            try {
                File myObj = new File("data2_" + msg + ".txt");
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    data2.append(data).append("\n");
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void start(InetAddress IP, int port, final int option) throws IOException {
        System.out.println("Clien TCP with"+port+ "is running");
        socket = new Socket(IP, port);
        System.out.println("Socket: "+IP.getHostAddress()+":"+port);

        ois = new ObjectOutputStream(socket.getOutputStream());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (option == 1) {
                            ois.writeObject(data1.toString().trim());
                        } else {
                            ois.writeObject(data2.toString().trim());
                        }
                        ois.flush();
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
