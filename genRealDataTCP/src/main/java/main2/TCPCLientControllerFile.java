/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main2;

import net.andreinc.mockneat.MockNeat;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Ryan
 */
public class TCPCLientControllerFile {
    private Socket socket;
    private ObjectOutputStream ois;
    private String data1="";
    private String data2="";

    public TCPCLientControllerFile(InetAddress IP, int port, int option , int msg){
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

            getStream(option,msg);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void getStream(final int option , final int msg) throws IOException {
        ois = new ObjectOutputStream(socket.getOutputStream());

        new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        if (option == 1) {
                            ois.writeObject(data1.trim());
                            ois.flush();
                        } else {
                            ois.writeObject(data2.trim());
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
