/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import main.TCPServerController;

/**
 *
 * @author dat.chuthanh
 */
public class TCPServerViewM {
    public static void main(String[] args) {
        try {

        new Thread(new Runnable() {
            public void run() {
                TCPServerController server1 = new TCPServerController(11000,1);
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                TCPServerController server2 = new TCPServerController(11001,2);

            }
        }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
