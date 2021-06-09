/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.UDPClient;
import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 *
 * @author Ryan
 */
public class UDPClientView {
    public static void main(String[] args) throws UnknownHostException {
        UDPClient client = new UDPClient();
        client.send(InetAddress.getByName("localhost"), 1001);
        new Thread(()->{
            while(true){
                System.out.println(client.receive());
            }
        }).start();
    }
}
