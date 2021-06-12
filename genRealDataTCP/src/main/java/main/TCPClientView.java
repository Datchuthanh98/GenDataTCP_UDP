package main;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.PriorityBlockingQueue;

public class TCPClientView {
    public static Integer numMessOnSecond = 0;
    public static Node root = new Node();
    // priority queue with blocking for thread-safe process
    public static PriorityBlockingQueue<String> queue = new PriorityBlockingQueue<>(1000000, (s1, s2) -> {
        // comparator so that element go in the queue will be sorted
        for (int i = 0; i < 14; i++) {
            if (s1.charAt(i) < s2.charAt(i + 8)) return -1;
            else if (s1.charAt(i) > s2.charAt(i)) return 1;
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
                while (true) {
                    try {
                        String data = clientController1.readData();
                        // add to queue
                        queue.add(data);
//                        System.out.println(data);
                        numMessOnSecond++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // read data from server 2
            new Thread(() -> {
                while (true) {
                    try {
                        String data = clientController2.readData();
                        // add to queue
                        queue.add(data);
//                        System.out.println(data);
                        numMessOnSecond++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            new Thread(() -> {
                while (true) {
                    try {
                        System.out.println("numMessOnSecond : " + numMessOnSecond);
                        numMessOnSecond = 0;
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                            // search on trie
                            Node curr = root;
                            String[] dataArr = data.split(",");
//                            String publicIp = dataArr[4];
                            String privateIp = dataArr[2];
                            String[] ipArr = privateIp.split("\\.");
                            for (int i = 0; i < 4; i++) {
                                int tmp = Integer.parseInt(ipArr[i]);
                                if (curr.next[tmp] == null) break;
                                curr = curr.next[tmp];
                            }
                            if (curr.data != null) {
                                // found
                                // insert to db
                                System.out.println(data + " " + curr.data);
                            }
                        } else {
                            // server 1
                            // add to trie
                            Node curr = root;
                            String[] dataArr = data.split("\\|");
//                            String phone = dataArr[3];
                            String ip = dataArr[4];
                            String[] ipArr = ip.split("\\.");
                            for (int i = 0; i < 4; i++) {
                                int tmp = Integer.parseInt(ipArr[i]);
                                if (curr.next[tmp] == null) curr.next[tmp] = new Node();
                                curr = curr.next[tmp];
                            }
//                            curr.phone = phone;
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
//        String phone;
        Node[] next = new Node[256];
    }
}
