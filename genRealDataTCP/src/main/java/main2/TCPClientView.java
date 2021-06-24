package main2;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

//de t demo cho xem
public class TCPClientView {

    public static void main(String[] args) throws UnknownHostException {
        try {
            final TCPCLientController clientController1 = new TCPCLientController(InetAddress.getByName("10.4.200.61"), 11000,1,1000);
            final TCPCLientController clientController2 = new TCPCLientController(InetAddress.getByName("10.4.200.61"), 11001,2,1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
