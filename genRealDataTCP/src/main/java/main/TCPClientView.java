package main;

import redis.MsgQueueRedis;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TCPClientView {

    private static volatile AtomicInteger numMessOnSecond = new AtomicInteger(0);

    public static void main(String[] args) throws UnknownHostException {
        try {
            final TCPCLientController clientController1 = new TCPCLientController(InetAddress.getByName("localhost"), 11000);
            final TCPCLientController clientController2 = new TCPCLientController(InetAddress.getByName("localhost"), 11001);

            // read data from server 1
            new Thread(() -> {
                try {
                    while (true) {
                        String data = clientController1.readData();
                        System.out.println(data);
                        numMessOnSecond.getAndIncrement();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            // read data from server 2
            new Thread(() -> {
                try {
                    while (true) {
                        String data = clientController2.readData();
                        System.out.println(data);
                        numMessOnSecond.getAndIncrement();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            new Thread(() -> {
                try {
                    while (true) {
                        System.out.println("numMessOnSecond : " + numMessOnSecond);
                        numMessOnSecond.set(0);
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
