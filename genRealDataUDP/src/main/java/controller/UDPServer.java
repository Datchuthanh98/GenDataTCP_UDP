/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Ryan
 */
public class UDPServer {
    DatagramSocket server = null;
    int port = 1001;
    public UDPServer(){
        try {
            server = new DatagramSocket(port);
        } catch (SocketException ex) {
            Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void listening(){
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        while(true){
            try{
                final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                server.receive(receivePacket);
                //Receive object
                ByteArrayInputStream bis = new ByteArrayInputStream(receivePacket.getData());
                ObjectInputStream ois = new ObjectInputStream(bis);
                ois.readObject();
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            while (true) {
                                ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
                                ObjectOutputStream oos = new ObjectOutputStream(bos);
                                oos.writeObject(new Random().nextInt());
                                oos.flush();
                                byte[] buffer = new byte[1024];
                                buffer = bos.toByteArray();
                                DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, receivePacket.getAddress(), receivePacket.getPort());
                                server.send(sendPacket);
                                Thread.sleep(1000);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
