/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main2;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Ryan
 */
public class TCPCLientController {
    private Socket socket;
    private PrintWriter ois;
    private String data1="";
    private String data2="";

    public TCPCLientController(InetAddress IP, int port,int option ,int msg){
        System.out.println("Clien TCP with"+port+ "is running");
        try{
            socket = new Socket(IP, port);
            System.out.println("Socket: "+IP.getHostAddress()+":"+port);

            try {
                File myObj = new File("data1_" + msg + ".txt");
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    data1 = data1 +data +"\n";
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
                    data2 = data2 +data +"\n";
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            getStream(option);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void getStream(final int option) throws IOException {
        ois = new PrintWriter(new PrintWriter(socket.getOutputStream()));

        new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        if (option == 1) {
                            ois.write(data1);
                            ois.flush();
                        } else {
                            ois.write(data2);
                            ois.flush();
                        }
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
