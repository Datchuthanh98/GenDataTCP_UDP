/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SvPython;

import main.TCPServerController;

/**
 *
 * @author dat.chuthanh
 */
public class TCPServerViewPy {
    public static void main(String[] args) {
        try {

        new Thread(new Runnable() {
            public void run() {
                TCPServerControllerPy server1 = new TCPServerControllerPy(11000, 1);
            }
        }).start();
        

        new Thread(new Runnable() {
            public void run() {
                TCPServerControllerPy server2 = new TCPServerControllerPy(11001, 2);

            }
        }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
