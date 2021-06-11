package test;

import main.TCPCLientController;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TCPClientViewM {
    static boolean lock = false;

    public static void main(String[] args) throws UnknownHostException {
        final Node root = new Node();

        try {
            final TCPCLientController clientController1 = new TCPCLientController(InetAddress.getByName("localhost"), 11000);
            final TCPCLientController clientController2 = new TCPCLientController(InetAddress.getByName("localhost"), 11001);
            new Thread(new Runnable() {
                public void run() {
                    try {
                        while (true) {
//                            System.out.println(clientController1.readData());
                            String data = clientController1.readData();
                            String[] itemData = data.split("\\|");
                            String phone = itemData[3];
                            String ipPrivate = itemData[4];
                            String[] ipArrStr = ipPrivate.split("\\.");
                            Node curr = root;

                            while (lock) ;
                            lock = true; // 10.12.30.123

                            for (int i = 0; i < 4; i++) {
                                int tmp = Integer.parseInt(ipArrStr[i]);
                                if (curr.next[tmp] == null) curr.next[tmp] = new Node();
                                curr = curr.next[tmp];
                            }
                            curr.phone = phone;
                            lock = false;
//                            System.out.println(phone);
//                            System.out.println("Server 1: " + ipPrivate);

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
//                            System.out.println(clientController2.readData() );
                            String data = clientController2.readData();
                            String[] itemData = data.split(",");
                            String ipPublic = itemData[2];
                            String ipPrivate = itemData[4];
                            String[] ipArrPrivateStr = ipPrivate.split("\\.");

                            while (lock) ;
                            lock = true;
                            boolean notFound = false;
                            Node curr = root;// 10.12.30.123
                            for (int i = 0; i < 4; i++) {
                                int tmp = Integer.parseInt(ipArrPrivateStr[i]);
                                if (curr.next[tmp] == null) {
//                                    System.out.println("Not found");
                                    notFound = true;
                                    break;
                                }
                                curr = curr.next[tmp];
                            }
                            if (!notFound && curr.phone != null) System.out.println(ipPublic + " " + curr.phone);
                            lock = false;
//                            System.out.println("Server 2: "+ipPrivate);
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

    //CSDL
    static class Node {
        String phone;
        Node[] next = new Node[256];
    }
}
