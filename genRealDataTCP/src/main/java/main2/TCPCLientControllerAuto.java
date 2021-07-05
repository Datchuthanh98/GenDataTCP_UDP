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

public class TCPCLientControllerAuto {
    private Socket socket;
    private ObjectOutputStream ois;
    private String data1="";
    private String data2="";
    public Boolean ipPr1 = false;
    public Boolean ipPr2 = false;

    public TCPCLientControllerAuto(InetAddress IP, int port, int option , int msg){
        System.out.println("Clien TCP with"+port+ "is running");
        try{
            socket = new Socket(IP, port);
            System.out.println("Socket: "+IP.getHostAddress()+":"+port);

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
                    MockNeat mock = MockNeat.threadLocal();
                    while (true) {
                            String data1="";
                            String data2 ="";
                            //msg = 5000

                            Random rand = new Random();
                            String ipPrivateMatching = "10.11.12.13";
                            for(int i =0 ;i< msg;i++){

                                int a = rand.nextInt(2);
                                if(a == 1){
                                    data1 += genDataMatchFile1( ipPrivateMatching)+"\n";
                                    data2 += genDataMatchFile2(ipPrivateMatching)+"\n";
                                }else{
                                    data1 += genDataFakeFile1()+"\n";
                                    data2 += genDataFakeFile2()+"\n";
                                }
                            }
                        if (option == 1) {
//                            System.out.println(data1);
                            ois.writeObject(data1.trim());
                            ois.flush();
                        } else {
//                            System.out.println(data2);
                            ois.writeObject(data2.trim());
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

    public static String genDataMatchFile1(String ipPrivateMatching) {
        String data = "";
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String strDate = dateFormat.format(date);
        data += strDate + ",OK_RadiusMessage";

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
        data += "," + ipPrivateMatching;
        return data;
    }

    public static String genDataMatchFile2(String ipPrivateMatching) {
        String data = "OK_NVL01_1";
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String strDate = dateFormat.format(date);
        data += "," + strDate;
        Random rand = new Random();

        data += "," + ipPrivateMatching;
        data += "," + rand.nextInt(10000);

        MockNeat mock = MockNeat.threadLocal();

        for (int i = 0; i < 2; i++) {
            String ipv4 = mock.ipv4s().val();
            data += "," + ipv4;
            data += "," + rand.nextInt(10000);
        }
        return data;
    }

    public static void resetIpPrivateMatching() {

    }


}
