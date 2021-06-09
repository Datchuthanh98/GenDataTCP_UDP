/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.TCPCLient;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 *
 * @author Ryan
 */
public class TCPClientView {
    
    
          
    public static void main(String[] args) throws UnknownHostException {
          try {
        TCPCLient   clientController = new TCPCLient(InetAddress.getByName("localhost"), 11003);
            new Thread(()->{
                try{
                    while (true){
                        System.out.println(clientController.readData()+"\n");
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }).start();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
