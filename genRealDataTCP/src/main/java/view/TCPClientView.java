package view;

import controller.TCPCLient;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TCPClientView {
    public static Integer numMessOnSecond = 0;

    public static void main(String[] args) throws UnknownHostException {

        try {
            final TCPCLient clientController1 = new TCPCLient(InetAddress.getByName("localhost"), 11000);
            final TCPCLient   clientController2 = new TCPCLient(InetAddress.getByName("localhost"), 11001);
            new Thread(new Runnable() {
                public void run() {
                    try {
                        while (true) {
                           String data= clientController1.readData();
                            System.out.println(data);
                            numMessOnSecond++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            new Thread(new Runnable() {
                public void run() {
                    try {
                        while (true) {
                            String data = clientController2.readData();
                            System.out.println(data);
                            numMessOnSecond++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

//            new Thread(new Runnable() {
//                public void run() {
//                    try {
//                        while (true) {
////                            System.out.println("numMessOnSecond : "+ numMessOnSecond);
//                            numMessOnSecond = 0;
//                            Thread.sleep(100);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
