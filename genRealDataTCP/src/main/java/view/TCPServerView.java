/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.TCPServer;

/**
 *
 * @author dat.chuthanh
 */
public class TCPServerView {
    public static void main(String[] args) {
        try {

        new Thread(new Runnable() {
            public void run() {
                TCPServer server1 = new TCPServer(11000,1);
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                TCPServer server2 = new TCPServer(11001,2);

            }
        }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
