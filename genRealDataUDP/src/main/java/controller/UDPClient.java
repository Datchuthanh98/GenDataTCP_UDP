/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Ryan
 */
public class UDPClient {

    DatagramSocket socket = null;
    int port = 1002;

    public UDPClient() {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException ex) {
            Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void send( InetAddress serverIP, int serverPort) {
        if (socket != null) {
            byte[] sendData = new byte[1024];
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                //Write object to byte array output stream through object output stream
//                oos.writeObject(new Object());
                oos.writeObject("");
                oos.flush();
                //Get the written byte array
                sendData = bos.toByteArray();
                
                DatagramPacket packet = new DatagramPacket(sendData, sendData.length, serverIP, serverPort);
                socket.send(packet);
            } catch (IOException ex) {
                Logger.getLogger(UDPClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Object receive() {
        if (socket != null) {
            byte[] receiveData = new byte[1024];
            try {
                DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(packet);
                ByteArrayInputStream bis = new ByteArrayInputStream(receiveData);
                ObjectInputStream ois = new ObjectInputStream(bis);
                return  ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "undefined";
    }

    public void close() {
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
