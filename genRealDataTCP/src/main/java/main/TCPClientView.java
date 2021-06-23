package main;

import redis.MsgQueueRedis;

import java.io.FileWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
//de t demo cho xem
public class TCPClientView {

    private static volatile AtomicInteger numMessOnSecondData1 = new AtomicInteger(0);
    private static volatile AtomicInteger numMessOnSecondData2 = new AtomicInteger(0);

    public static void main(String[] args) throws UnknownHostException {
        try {
            final TCPCLientController clientController1 = new TCPCLientController(InetAddress.getByName("localhost"), 11000);
            final TCPCLientController clientController2 = new TCPCLientController(InetAddress.getByName("localhost"), 11001);
//            final FileWriter myWriterFile1 = new FileWriter("data1.txt");
//            final FileWriter myWriterFile2 = new FileWriter("data2.txt");

            // read data from server 1
            new Thread(new Runnable() {
                public void run() {
                    try {
                        while (true) {
                            String data = clientController1.readData();
                            System.out.println("================================");
                            System.out.println(data);
//                            System.out.println(data);
                            System.out.println("================================");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // read data from server 2
            new Thread(new Runnable() {
                public void run() {
                    try {
                        while (true) {
                            String data = clientController2.readData();
//                            System.out.println(data);
//                            myWriterFile2.write(data+ "\n");
                        }
                    } catch (Exception e) {
//                        e.printStackTrace();
                    }
                }
            }).start();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
