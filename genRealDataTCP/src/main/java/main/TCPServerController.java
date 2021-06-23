/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import net.andreinc.mockneat.MockNeat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class TCPServerController {
    private static volatile AtomicInteger numMessOnSecond = new AtomicInteger(0);
    private ServerSocket serverSocket;
    private Socket clientSocket;
    public static String ipPrivateMatching = "10.100.14.16";
    private volatile AtomicBoolean lock = new AtomicBoolean(false);
    private String data1="";
    private String data2="";

    public TCPServerController(int port, int option, int msg) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server TCP with port : " + port + " is running...");

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

            listening(option);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void listening(final int option) {

        while (true){
        try {
            clientSocket = serverSocket.accept();
            System.out.println(clientSocket.getInetAddress());
//            final ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());

           final PrintWriter oos = new PrintWriter( new PrintWriter(clientSocket.getOutputStream()));

            new Thread(new Runnable() {
                public void run() {
                    try {
                        while (true) {
//                            while (lock.getAndSet(true)) ;
//                            numMessOnSecond.getAndIncrement();

                            if (option == 1) {
                                oos.write(data1);
                                oos.flush();
                            } else {
                                oos.write(data2);
                                oos.flush();
                            }
                            lock.set(false);
                            Thread.sleep(10000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        } catch (Exception e) {
            e.printStackTrace();
        }
        }

    }


    private void commandClose() throws IOException, ClassNotFoundException {
        clientSocket.close();
    }

}
