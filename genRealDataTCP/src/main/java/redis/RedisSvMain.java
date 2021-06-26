/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author dat.chuthanh
 */
public class RedisSvMain {

    private static Node root = new Node();
    private static MsgQueueRedis msgQueueRedis = new MsgQueueRedis();
    public static Integer numMatching = 0;
    public static long startTime = 0;
    public static long totalTimefor1kMess = 0;
    public static int executeCount = 0;
    public static PriorityBlockingQueue<String> queue = new PriorityBlockingQueue<String>(10000000, new Comparator<String>() {
        public int compare(String s1, String s2) {
            for (int i = 0; i < 14; i++) {
                if (s1.charAt(i) < s2.charAt(i + 8)) return -1;
                else if (s1.charAt(i) > s2.charAt(i + 8)) return 1;
            }
            if (s1.startsWith("NVL01_1")) return 1;
            else if (s2.startsWith("NVL01_1")) return -1;
            return 0;
        }
    });

    public static void main(String[] args) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(11000);
                    System.out.println("Server TCP with port : " + 11000 + " is running...");
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("client was accpeted ");
                    System.out.println(clientSocket.getInetAddress());
                    ObjectInputStream os = new ObjectInputStream(clientSocket.getInputStream());
                    try {
                        while (true) {
                            String data = os.readObject().toString().trim();
                            String[] arrayData = data.split("\n");

                            for (int i = 0; i < arrayData.length; i++) {
                                queue.add(arrayData[i]);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(11001);
                    System.out.println("Server TCP with port : " + 11001 + " is running...");
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("client was accpeted ");
                    System.out.println(clientSocket.getInetAddress());
                    ObjectInputStream os = new ObjectInputStream(clientSocket.getInputStream());
                    try {
                        while (true) {
                            String data = os.readObject().toString().trim();
                            String[] arrayData = data.split("\n");
                            for (int i = 0; i < arrayData.length; i++) {
                                queue.add(arrayData[i]);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {
                    startTime = System.currentTimeMillis();
                    while (true) {
                        String data = queue.poll();
                        if (data == null) continue;
                        if (data.startsWith("NVL01")) {
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
                                // FOUND PHONE NUMBER MATCHING AND INSERT TO DATABASE
                                String[] arrData1 = data.split(",");
                                String[] arrData2 = curr.data.split(",");
                                if (arrData2[2].equals("Start")) {
                                    String key = arrData2[3] + "_" + arrData1[4];
                                    String value = data + "," + curr.data;
                                    msgQueueRedis.addByKeyVlue(key, value);
                                    numMatching++;
                                }
                            } else {
//                                System.out.println("ko match");
                            }

                        } else {
                            Node curr = root;
                            String[] dataArr = data.split(",");
                            String ip = dataArr[4];
                            String[] ipArr = ip.split("\\.");
                            for (int i = 0; i < 4; i++) {
                                int tmp = Integer.parseInt(ipArr[i]);
                                if (curr.next[tmp] == null) curr.next[tmp] = new Node();
                                curr = curr.next[tmp];
                            }
                            curr.data = data;
                        }
                        //execute count
                        executeCount++;
                        if (executeCount == 1000) {
                            long currTime = System.currentTimeMillis();
                            long timeExe = currTime - startTime;
                            System.out.println("time execute 1000 data : " + timeExe + "ms");
                            System.out.println("numMatching " + numMatching);
                            System.out.println("--------------------------------------------");
                            numMatching = 0;
                            executeCount = 0;
                            startTime = System.currentTimeMillis();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    static class Node {
        String data;
        Node[] next = new Node[256];
    }
}
