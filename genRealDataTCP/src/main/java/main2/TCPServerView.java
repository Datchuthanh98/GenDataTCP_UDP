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
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
        

        new Thread(new Runnable() {
            public void run() {
                try {
                    final TCPServerController server2 = new TCPServerController(11001);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
