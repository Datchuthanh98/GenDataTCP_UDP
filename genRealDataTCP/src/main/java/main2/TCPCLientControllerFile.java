/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main2;

import net.andreinc.mockneat.MockNeat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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
    private PrintWriter ois;
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
                        Thread.sleep(10000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    public static String genDataFakeFile1() {
        String data = "";
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String strDate = dateFormat.format(date);
        data += strDate + ",RadiusMessage";

        if (Math.random() < 0.5) {
            data += ",Start";
        } else {
            data += ",Stop";
        }

        Random rand = new Random();
        String phone = "84";
        for (int i = 0; i < 9; i++) {
            phone += rand.nextInt(10);
        }

        data += "," + phone;

        MockNeat mock = MockNeat.threadLocal();

        String ipv4 = mock.ipv4s().val();
        data += "," + ipv4;
        return data;
    }


    public static String genDataFakeFile2() {
        String data = "NVL01_1";
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String strDate = dateFormat.format(date);
        data += "," + strDate;

        MockNeat mock = MockNeat.threadLocal();
        Random rand = new Random();
        for (int i = 0; i < 3; i++) {
            String ipv4 = mock.ipv4s().val();
            data += "," + ipv4;
            data += "," + rand.nextInt(10000);
        }
        return data;
    }


}
