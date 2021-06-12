package main;

import redis.JedisFactory;
import redis.clients.jedis.Jedis;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TCPClientView {
    public static Integer numMessOnSecond = 0;

    public static void main(String[] args) throws UnknownHostException {

       try {
            final TCPCLientController clientController1 = new TCPCLientController(InetAddress.getByName("localhost"), 11000);
            final TCPCLientController clientController2 = new TCPCLientController(InetAddress.getByName("localhost"), 11001);
            new Thread(new Runnable() {
                public void run() {
                    try {
                        while (true) {
                           String data= clientController1.readData();
                            if (data.startsWith("Match")) System.out.println(data);
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
                            numMessOnSecond++;
                            if (data.startsWith("Match")) System.out.println(data);
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
                            System.out.println("numMessOnSecond : "+ numMessOnSecond);
                            numMessOnSecond = 0;
                            Thread.sleep(1000);
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
