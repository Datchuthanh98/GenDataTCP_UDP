package test;

import main.TCPServerController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class testFile {
    private static volatile AtomicInteger numMessOnSecond = new AtomicInteger(0);
    public static void main(String[] args) {
      //create File
//        try {
//            File myObj = new File("uchiha.txt");
//            if (myObj.createNewFile()) {
//                System.out.println("File created: " + myObj.getName());
//            } else {
//                System.out.println("File already exists.");
//            }
//        } catch (IOException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }

        //write File
//        try {
//            final FileWriter myWriter = new FileWriter("uchiha.txt");
//
//            new Thread(new Runnable() {
//                public void run() {
//                    while (true) {
//                        try {
//                            System.out.println("Number: "+numMessOnSecond);
//                            numMessOnSecond.getAndIncrement() ;
//                            myWriter.write("number "+ numMessOnSecond+ "\n");
//                            Thread.sleep(1);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }).start();
//
//
//            System.out.println("Successfully wrote to the file.");
//        } catch (Exception e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }


        //Read File
        String dataFile = "";
        try {
            File myObj = new File("data1_1000.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                dataFile = dataFile +data +"\n";
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        System.out.println("Datafileeeeeeee");
        System.out.println(dataFile);

        //Delete File
//        try {
//            File myObj = new File("uchiha.txt");
//            if (myObj.delete()) {
//                System.out.println("Deleted the file: " + myObj.getName());
//            } else {
//                System.out.println("Failed to delete the file.");
//            }
//
//        }catch (Exception e){
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }



    }
}
