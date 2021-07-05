package main2;

import java.net.InetAddress;
import java.net.UnknownHostException;

//de t demo cho xem
public class TCPClientViewAuto {

    public static void main(String[] args) throws UnknownHostException {
        try {
            final TCPCLientControllerAuto clientController1 = new TCPCLientControllerAuto(InetAddress.getByName("localhost"), 11000,1,10000);
            final TCPCLientControllerAuto clientController2 = new TCPCLientControllerAuto(InetAddress.getByName("localhost"), 11001,2,10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
