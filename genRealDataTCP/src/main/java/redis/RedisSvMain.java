/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

/**
 *
 * @author dat.chuthanh
 */
public class RedisSvMain {

    private static Node root = new Node();
    private static MsgQueueRedis msgQueueRedis = new MsgQueueRedis();
    public static Integer numMatching= 0;


    public static PriorityBlockingQueue<String> queue = new PriorityBlockingQueue<String>(10000000, new Comparator<String>() {
        public int compare(String s1, String s2) {
            for (int i = 0; i < 14; i++) {
                if (s1.charAt(i) < s2.charAt(i + 8)) return -1;
                else if (s1.charAt(i) > s2.charAt(i + 8)) return 1;
            }
            if (s1.startsWith("OK_NVL01_1")) return 1;
            else if (s2.startsWith("OK_NVL01_1")) return -1;
            return 0;
        }
    });



    public static void main(String[] args) {

        new Thread(new Runnable() {
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(11000);
                    System.out.println("Server TCP with port : " + 11000 + " is running...");
                    while (true) {
                        try {
                            System.out.println("accept wait accept");
                            Socket clientSocket = serverSocket.accept();
                            System.out.println("client was accpeted ");
                            System.out.println(clientSocket.getInetAddress());
                            BufferedReader os = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                            new Thread(() -> {
                                try {
                                    while (true) {
                                        String data = os.readLine();
                                        System.out.println(data);
                                        queue.add(data);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                    while (true) {
                        try {
                            System.out.println("accept wait accept");
                            Socket clientSocket = serverSocket.accept();
                            System.out.println("client was accpeted ");
                            System.out.println(clientSocket.getInetAddress());
                            BufferedReader os = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                            new Thread(() -> {
                                try {
                                    while (true) {
                                        String data = os.readLine();
                                        System.out.println(data);
                                        queue.add(data);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                boolean match = false;
                long startTime = 0;
                try {
                    while (true) {
                        // get data from queue if queue is not empty
                        if(match == true){
                            match = false;
                            startTime = System.currentTimeMillis();
                        }
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
                                // FOUND PHONE NUMBER MATCHING AND INSERT TO DATABASE
                                System.out.println("data1 :"+data);
                                String [] arrData1 = data.split(",");
                                String [] arrData2 = curr.data.split(",");
                                if(arrData2[2].equals("Start")){
                                    String key = arrData2[0]+"_"+arrData2[3]+"_"+arrData1[4]+"_"+arrData1[5];
                                    String value = data +"_"+curr.data;
                                    msgQueueRedis.addByKeyVlue(key,value);
                                    long currTime = System.currentTimeMillis();
                                    System.out.println("time execute : " + (currTime-startTime) +" ms");
                                    match = true;
                                    numMatching++;
                                    System.out.println("numMatching " + numMatching);
                                }
                            }
                        } else {
                            // server 1
                            // ADD TO TRIE
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
