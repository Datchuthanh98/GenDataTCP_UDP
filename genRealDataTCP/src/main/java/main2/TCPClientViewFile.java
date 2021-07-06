package main2;

import java.net.InetAddress;
import java.net.UnknownHostException;

//de t demo cho xem
public class TCPClientViewFile {

    public static void main(String[] args) throws UnknownHostException {
        try {
            final TCPCLientControllerFile clientController1 =
                    new TCPCLientControllerFile(10000);
            final TCPCLientControllerFile clientController2 =
                    new TCPCLientControllerFile(10000);

            clientController1.start(InetAddress.getByName("localhost"), 11000,1);
            clientController2.start(InetAddress.getByName("localhost"), 11001,2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
