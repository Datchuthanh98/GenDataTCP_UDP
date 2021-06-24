package main2;

import java.net.InetAddress;
import java.net.UnknownHostException;

//de t demo cho xem
public class TCPClientViewFile {

    public static void main(String[] args) throws UnknownHostException {
        try {
            final TCPCLientControllerFile clientController1 = new TCPCLientControllerFile(InetAddress.getByName("localhost"), 11000,1,1000);
            final TCPCLientControllerFile clientController2 = new TCPCLientControllerFile(InetAddress.getByName("localhost"), 11001,2,1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
