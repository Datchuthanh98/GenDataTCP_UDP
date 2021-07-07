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
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author dat.chuthanh
 */
public class RedisSvMain {
    public static Integer doubleLine = 20000;
    public static Integer numMatching = 0;
    public static long startTime = 0;
    public static int executeCount = 0;
    static Map<String, CustomDataModel> map = new HashMap<String, CustomDataModel>();
    static BlockingQueue<String> queue = new LinkedBlockingDeque<String>();
    private static MsgQueueRedis msgQueueRedis = new MsgQueueRedis();

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
        return 0;
    }

    public static void main(String[] args) throws IOException {
        final ServerSocket serverSocket1 = new ServerSocket(11000);
        System.out.println("Server TCP with port : " + 11000 + " is running...");

        final ServerSocket serverSocket2 = new ServerSocket(11001);
        System.out.println("Server TCP with port : " + 11001 + " is running...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Socket clientSocket = serverSocket1.accept();
                        System.out.println("client was accpeted ");
                        System.out.println(clientSocket.getInetAddress());
                        ObjectInputStream os = new ObjectInputStream(clientSocket.getInputStream());
                        while (true) {
                            String data = os.readObject().toString().trim();
                            String[] arrayData = data.split("\n");

                            queue.addAll(Arrays.asList(arrayData));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        map.clear();
                        queue.clear();
                        numMatching = 0;
                        executeCount = 0;
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Socket clientSocket = serverSocket2.accept();
                        System.out.println("client was accpeted ");
                        System.out.println(clientSocket.getInetAddress());
                        ObjectInputStream os = new ObjectInputStream(clientSocket.getInputStream());
                        while (true) {
                            String data = os.readObject().toString().trim();
                            String[] arrayData = data.split("\n");
                            queue.addAll(Arrays.asList(arrayData));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        map.clear();
                        queue.clear();
                        numMatching = 0;
                        executeCount = 0;
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String data = queue.take();
                        if (executeCount == 0)
                            startTime = System.currentTimeMillis();
                        String[] dataArr = data.split(",");
                        if (data.startsWith("NVL01") || data.startsWith("OK_NVL01")) {
                            // data từ client 2
                            String ip = dataArr[2];
                            CustomDataModel mapData = map.get(ip);
                            if (mapData == null) {
                                mapData = new CustomDataModel();
                                map.put(ip, mapData);
                            } else {
                                for (int i = mapData.data1.size() - 1; i >= 0; i--) {
                                    String d = mapData.data1.get(i);
                                    if (compareTime(d, data) <= 0) {
                                        // tìm thấy match
                                        String[] arrData_1 = d.split(",");
                                        String[] arrData_2 = data.split(",");
                                        String key = arrData_1[3] + "_" + arrData_2[4]+"_"+arrData_2[5]+"_"+arrData_2[6];
                                         String value = data + "," + d;
                                            msgQueueRedis.addByKeyVlue(key, value);
                                        numMatching++;
                                        break;
                                    }
                                }
                            }
                            mapData.data2.add(data);
                        } else {
                            // data từ client 1
                            String ip = dataArr[4];
                            // nếu là Stop thì bỏ qua
                            if (dataArr[2].equals("Start")) {
                                CustomDataModel mapData = map.get(ip);
                                if (mapData == null) {
                                    mapData = new CustomDataModel();
                                    map.put(ip, mapData);
                                } else {
                                    for (int i = mapData.data2.size() - 1; i >= 0; i--) {
                                        String d = mapData.data2.get(i);
                                        if (compareTime(data, d) <= 0) {
                                            // tìm thấy match
                                            String[] arrData_2= d.split(",");
                                            String[] arrData_1 = data.split(",");
                                            String key = arrData_1[3] + "_" + arrData_2[4]+"_"+arrData_2[5]+"_"+arrData_2[6];
                                            String value = data + "," + d;
                                            msgQueueRedis.addByKeyVlue(key, value);
                                            numMatching++;
                                            mapData.data2.remove(i);
                                        }
                                    }
                                }
                                mapData.data1.add(data);
                            }
                        }
                        //execute count
                        executeCount++;
                        if (executeCount == 20000) {
                            long currTime = System.currentTimeMillis();
                            long timeExe = currTime - startTime;

                            System.out.println("time execute "+(doubleLine/2)+" data : " + (timeExe) + "ms");
                            System.out.println("numMatching " + numMatching);
                            System.out.println("--------------------------------------------");

                            numMatching = 0;
                            executeCount = 0;
                            map.clear();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    static class CustomDataModel {
        ArrayList<String> data1 = new ArrayList<String>();
        ArrayList<String> data2 = new ArrayList<String>();
    }
}
