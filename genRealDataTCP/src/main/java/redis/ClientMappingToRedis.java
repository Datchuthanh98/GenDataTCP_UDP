package redis;

import main.TCPCLientController;
import redis.MsgQueueRedis;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientMappingToRedis {
    private static  volatile AtomicInteger numMessOnSecond = new AtomicInteger(0);
    private static Node root = new Node();
    private static MsgQueueRedis msgQueueRedis = new MsgQueueRedis("Matching");
    public static Integer numMatching= 0;

    public static PriorityBlockingQueue<String> queue = new PriorityBlockingQueue<>(1000000, (s1, s2) -> {
        for (int i = 0; i < 14; i++) {
            if (s1.charAt(i) < s2.charAt(i + 8)) return -1;
            else if (s1.charAt(i) > s2.charAt(i + 8)) return 1;
        }
        if (s1.startsWith("NVL01")) return 1;
        else if (s2.startsWith("NVL01")) return -1;
        return 0;
    });

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
                        queue.add(data);
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

            new Thread(() -> {
                try {
                    // startTime and count for calculation per second
                    long startTime = System.currentTimeMillis();
                    int count = 0;

                    while (true) {
                        // get data from queue if queue is not empty
                        String data = queue.poll();
                        if (data == null) continue;
                        if (data.startsWith("NVL01")) {
                            // server 2
                            // SEARH ON TRIE
                            Node curr = root;
                            String[] dataArr = data.split(",");
                            String privateIp = dataArr[2];
                            String[] ipArr = privateIp.split("\\.");
                            for (int i = 0; i < 4; i++) {
                                int tmp = Integer.parseInt(ipArr[i]);
                                if (curr.next[tmp] == null) break;
                                curr = curr.next[tmp];
                            }
                            if (curr.data != null) {
                                // found Phone Number Matching and insert to Database
                                System.out.println(data + " " + curr.data);
                                msgQueueRedis.add(data + " " + curr.data);
                                numMatching++;
                                System.out.println("numMatching " +numMatching);
                            }
                        } else {
                            // server 1
                            // ADD TO TRIE
                            Node curr = root;
                            String[] dataArr = data.split("\\|");
                            String ip = dataArr[4];
                            String[] ipArr = ip.split("\\.");
                            for (int i = 0; i < 4; i++) {
                                int tmp = Integer.parseInt(ipArr[i]);
                                if (curr.next[tmp] == null) curr.next[tmp] = new Node();
                                curr = curr.next[tmp];
                            }
                            curr.data = data;
                        }

                        // calculation per second
                        count++;
                        long currTime = System.currentTimeMillis();
                        if (currTime - startTime >= 1000) {
                            System.out.println("Processed " + count + " in 1s");
                            System.out.println("Left in queue: " + queue.size());
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
        String data;
        Node[] next = new Node[256];
    }
}
