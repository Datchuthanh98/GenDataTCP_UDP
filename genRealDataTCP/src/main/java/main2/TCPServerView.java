/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main2;

import java.io.IOException;

/**
 *
 * @author dat.chuthanh
 */
public class TCPServerView {
    public static void main(String[] args) {

        new Thread(new Runnable() {
            public void run() {
                try {
                    final TCPServerController server1 = new TCPServerController(11000);
                    while (true) {
                        String data = server1.readData();
                        System.out.println(data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        

        new Thread(new Runnable() {
            public void run() {
                try {
                    final TCPServerController server2 = new TCPServerController(11001);
                    while (true) {
                        String data = server2.readData();
                        System.out.println(data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }
}
