package main;

import redis.JedisFactory;
import redis.clients.jedis.Jedis;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class TCPClientView {
    public static Integer numMessOnSecond = 0;
    public static PriorityBlockingQueue<String> queue = new PriorityBlockingQueue<>(1000000, (s1, s2) -> {
        for (int i = 0; i < 14; i++) {
            if (s1.charAt(i) < s2.charAt(i + 8)) return -1;
            else if (s1.charAt(i) > s2.charAt(i)) return 1;
        }
        return 0;
    });
    public static Node root = new Node();

    public static void main(String[] args) throws UnknownHostException {

        try {
            final TCPCLientController clientController1 = new TCPCLientController(InetAddress.getByName("localhost"), 11000);
            final TCPCLientController clientController2 = new TCPCLientController(InetAddress.getByName("localhost"), 11001);

            // read data from server 1
            new Thread(() -> {
                try {
                    while (true) {
                        String data = clientController1.readData();
                        queue.add(data);
                        numMessOnSecond++;
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
                        queue.add(data);
                        numMessOnSecond++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            new Thread(() -> {
                try {
                    while (true) {
                        System.out.println("numMessOnSecond : " + numMessOnSecond);
                        numMessOnSecond = 0;
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            new Thread(() -> {
                try {
                    long startTime = System.currentTimeMillis();
                    int count = 0;
                    while (true) {
                        String data = queue.poll();
                        if (data == null) continue;
                        if (data.startsWith("NVL01")) {
                            // server 2
                            // search on trie
                            Node curr = root;
                            String[] dataArr = data.split(",");
                            String publicIp = dataArr[4];
                            String privateIp = dataArr[2];
                            String[] ipArr = privateIp.split("\\.");
                            for (int i = 0; i < 4; i++) {
                                int tmp = Integer.parseInt(ipArr[i]);
                                if (curr.next[tmp] == null) break;
                                curr = curr.next[tmp];
                            }
                            if (curr.phone != null) {
                                // found
                                System.out.println(publicIp + " " + curr.phone);
                            }
                        } else {
                            // server 1
                            // add to trie
                            Node curr = root;
                            String[] dataArr = data.split("\\|");
                            String phone = dataArr[3];
                            String ip = dataArr[4];
                            String[] ipArr = ip.split("\\.");
                            for (int i = 0; i < 4; i++) {
                                int tmp = Integer.parseInt(ipArr[i]);
                                if (curr.next[tmp] == null) curr.next[tmp] = new Node();
                                curr = curr.next[tmp];
                            }
                            curr.phone = phone;
                        }
                        count++;
                        long currTime = System.currentTimeMillis();
                        if (currTime - startTime >= 1000) {
                            System.out.println("Processed " + count + " in 1s");
                            count = 0;
                            startTime = currTime;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class Node {
        String phone;
        Node[] next = new Node[256];
    }
}
