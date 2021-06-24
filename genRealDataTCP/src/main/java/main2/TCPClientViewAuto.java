package main2;

import java.net.InetAddress;
import java.net.UnknownHostException;

//de t demo cho xem
public class TCPClientViewAuto {

    public static void main(String[] args) throws UnknownHostException {
        try {
            final TCPCLientControllerAuto clientController1 = new TCPCLientControllerAuto(InetAddress.getByName("10.4.200.61"), 11000,1,1000);
            final TCPCLientControllerAuto clientController2 = new TCPCLientControllerAuto(InetAddress.getByName("10.4.200.61"), 11001,2,1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
