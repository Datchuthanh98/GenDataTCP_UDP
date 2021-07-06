package redis;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author dat.chuthanh
 */
public class RedisSvMain {

    public static Integer numMatching = 0;
    public static long startTime = 0;
    public static int executeCount = 0;
    static Map<String, CustomDataModel> map = new HashMap<>();
    static BlockingQueue<String> queue = new LinkedBlockingDeque<>();
//    public static PriorityBlockingQueue<String> queue = new PriorityBlockingQueue<String>(10000000, new Comparator<String>() {
//        public int compare(String s1, String s2) {
//            for (int i = 0; i < 14; i++) {
//                if (s1.charAt(i) < s2.charAt(i + 8)) return -1;
//                else if (s1.charAt(i) > s2.charAt(i + 8)) return 1;
//            }
//            if (s1.startsWith("NVL01_1")) return 1;
//            else if (s2.startsWith("NVL01_1")) return -1;
//            return 0;
//        }
//    });

    static int compareTime(String s1, String s2) {
        int s1TimeIdx = 0, s2TimeIdx = 0;
        if (s1.startsWith("NVL01_1")) s1TimeIdx = 8;
        else if (s1.startsWith("OK_NVL01_1")) s1TimeIdx = 11;
        if (s2.startsWith("NVL01_1")) s2TimeIdx = 8;
        else if (s2.startsWith("OK_NVL01_1")) s2TimeIdx = 11;
        for (int i = 0; i < 14; i++) {
            if (s1.charAt(i + s1TimeIdx) < s2.charAt(i + s2TimeIdx)) return -1;
            else if (s1.charAt(i + s1TimeIdx) > s2.charAt(i + s2TimeIdx)) return 1;
        }
        if (s1.startsWith("NVL01_1") || s1.startsWith("OK_NVL01_1")) return 1;
        else if (s2.startsWith("NVL01_1") || s2.startsWith("OK_NVL01_1")) return -1;
        return 0;
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket1 = new ServerSocket(11000);
        ServerSocket serverSocket2 = new ServerSocket(11001);

        new Thread(() -> {
            while (true) {
                try {
                    System.out.println("Server TCP with port : " + 11000 + " is running...");
                    Socket clientSocket = serverSocket1.accept();
                    System.out.println("client was accpeted ");
                    System.out.println(clientSocket.getInetAddress());
                    ObjectInputStream os = new ObjectInputStream(clientSocket.getInputStream());
                    try {
                        while (true) {
                            String data = os.readObject().toString().trim();
                            String[] arrayData = data.split("\n");

                            queue.addAll(Arrays.asList(arrayData));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        queue.clear();
                        map.clear();
                        executeCount=0;
                        numMatching=0;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            while (true) {
                try {
                    System.out.println("Server TCP with port : " + 11001 + " is running...");
                    Socket clientSocket = serverSocket2.accept();
                    System.out.println("client was accpeted ");
                    System.out.println(clientSocket.getInetAddress());
                    ObjectInputStream os = new ObjectInputStream(clientSocket.getInputStream());
                    try {
                        while (true) {
                            String data = os.readObject().toString().trim();
                            String[] arrayData = data.split("\n");
                            queue.addAll(Arrays.asList(arrayData));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        queue.clear();
                        map.clear();
                        executeCount=0;
                        numMatching=0;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            try {
                while (true) {
                    String data = queue.take();
                    if (executeCount == 0) startTime = System.currentTimeMillis();
                    String[] dataArr = data.split(",");
                    if (data.startsWith("NVL01") || data.startsWith("OK_NVL01")) {
                        // data từ client 2
                        String ip = dataArr[2];
                        CustomDataModel mapData = map.get(ip);
                        if (mapData == null) {
                            mapData = new CustomDataModel();
                            mapData.data2 = data;
                            map.put(ip, mapData);
                        } else {
                            if (mapData.data1 != null && compareTime(mapData.data1, data) <= 0) {
                                // tìm thấy match
                                numMatching++;
                            } else {
                                mapData.data2 = data;
                            }
                        }
                    } else {
                        // data từ client 1
                        String ip = dataArr[4];
                        // nếu là Stop thì bỏ qua
                        if (dataArr[2].equals("Start")) {
                            CustomDataModel mapData = map.get(ip);
                            if (mapData == null) {
                                mapData = new CustomDataModel();
                                mapData.data1 = data;
                                map.put(ip, mapData);
                            } else {
                                if (mapData.data2 != null && compareTime(data, mapData.data2) <= 0) {
                                    // tìm thấy match
                                    numMatching++;
                                    mapData.data2 = null;
                                }
                                mapData.data1 = data;
                            }
                        }
                    }
                    //execute count
                    executeCount++;
                    if (executeCount == 20000) {
                        long currTime = System.currentTimeMillis();
                        long timeExe = currTime - startTime;

                        System.out.println("time execute 1000 data : " + (timeExe) + "ms");
                        System.out.println("numMatching " + numMatching);
                        System.out.println("--------------------------------------------");

                        numMatching = 0;
                        executeCount = 0;
                        map.clear();
                        queue.clear();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    static class CustomDataModel {
        String data1;
        String data2;
    }
}
