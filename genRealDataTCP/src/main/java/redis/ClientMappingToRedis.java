package redis;

import main.TCPCLientController;
import redis.MsgQueueRedis;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors

public class ClientMappingToRedis {
    private static Node root = new Node();
    private static MsgQueueRedis msgQueueRedis = new MsgQueueRedis();
    public static Integer numMatching= 0;


    public static PriorityBlockingQueue<String> queue = new PriorityBlockingQueue<String>(10000000, new Comparator<String>() {
        public int compare(String s1, String s2) {
            for (int i = 0; i < 14; i++) {
                if (s1.charAt(i) < s2.charAt(i + 8)) return -1;
                else if (s1.charAt(i) > s2.charAt(i + 8)) return 1;
            }
            if (s1.startsWith("NVL01")) return 1;
            else if (s2.startsWith("NVL01")) return -1;
            return 0;
        }
    });

    public ClientMappingToRedis() throws IOException {
    }

    public static void main(String[] args) throws UnknownHostException {
        try {
            final TCPCLientController clientController1 = new TCPCLientController(InetAddress.getByName("localhost"), 11000);
            final TCPCLientController clientController2 = new TCPCLientController(InetAddress.getByName("localhost"), 11001);

            // read data from server 1
            new Thread(new Runnable() {
                public void run() {
                    try {
                        while (true) {
                            String data = clientController1.readData();
                            queue.add(data);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // read data from server 2
            new Thread(new Runnable() {
                public void run() {
                    try {
                        while (true) {
                            String data = clientController2.readData();
                            queue.add(data);

                        }
                    } catch (Exception e) {
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


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static class Node {
        String data;
        Node[] next = new Node[256];
    }
}
