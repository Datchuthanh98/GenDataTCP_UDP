package main2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.PriorityBlockingQueue;

public class Main {
    public static void main(String[] args) {
        PriorityQueue<String> queue = new PriorityQueue<String>(10000000, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
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
        });

        try {
            File myObj = new File("data1_10000.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (!data.isEmpty()) queue.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            File myObj = new File("data2_10000.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (!data.isEmpty()) queue.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        while (true) {
            Queue<String> myQueue = new PriorityQueue<String>(queue);
            HashMap<String, String> map = new HashMap<String, String>();
            int count = 0;
            while (!myQueue.isEmpty()) {
                String data = myQueue.poll();
                String[] dataArr = data.split(",");
                if (data.startsWith("NVL01") || data.startsWith("OK_NVL01")) {
                    // data từ client 2
                    String ip = dataArr[2];
                    if (map.get(ip) != null) count++;
                } else {
                    // data từ client 1
                    String ip = dataArr[4];
                    // nếu là Stop thì bỏ qua
                    if (dataArr[2].equals("Start")) {
                        map.put(ip, data);
                    }
                }
            }
            map.clear();
            System.out.println(count);
        }
    }
}
