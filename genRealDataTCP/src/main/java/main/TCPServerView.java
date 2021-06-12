/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author dat.chuthanh
 */
public class TCPServerView {
    public static void main(String[] args) {
        try {

        new Thread(() -> {
            TCPServerController server1 = new TCPServerController(11000,1);
        }).start();

        new Thread(() -> {
            TCPServerController server2 = new TCPServerController(11001,2);

        }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
