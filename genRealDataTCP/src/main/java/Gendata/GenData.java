package Gendata;

import net.andreinc.mockneat.MockNeat;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class GenData {


    public static String ipPrivateMatching = "10.100.14.16";
    public static void resetIpPrivateMatching() {
        MockNeat mock = MockNeat.threadLocal();
        ipPrivateMatching = mock.ipv4s().val();
    }

    public static void main(String[] args) {

        new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        resetIpPrivateMatching();
                        Thread.sleep(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


        try {
            final FileWriter myWriterData1 = new FileWriter("data1_10000.txt");
            final FileWriter myWriterData2 = new FileWriter("data2_10000.txt");

            new Thread(new Runnable() {
                public void run() {
                    int i = 1 ;
                    Random rn = new Random();
                    try {
                        while (i <=10000) {
                            if(rn.nextInt(3) == 2){
                                myWriterData1.append(genDataMatchFile1()+"\n");
                                myWriterData2.append(genDataMatchFile2()+"\n");
                            }else{
                                myWriterData1.append(genDataFakeFile1()+"\n");
                                myWriterData2.append(genDataFakeFile2()+"\n");
                            };
                            i++;
                            Thread.sleep(1);
                        }

                        myWriterData1.close();
                        myWriterData2.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            System.out.println("Successfully wrote to the file.");
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        System.out.println("Write data Sucessfully");
    }

    public static String genDataFakeFile1() {
        String data = "";
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String strDate = dateFormat.format(date);
        data += strDate + ",RadiusMessage";

        if (Math.random() < 0.5) {
            data += ",Start";
        } else {
            data += ",Stop";
        }

        Random rand = new Random();
        String phone = "84";
        for (int i = 0; i < 9; i++) {
            phone += rand.nextInt(10);
        }

        data += "," + phone;

        MockNeat mock = MockNeat.threadLocal();

        String ipv4 = mock.ipv4s().val();
        data += "," + ipv4;
        return data;
    }


    public static String genDataFakeFile2() {
        String data = "NVL01_1";
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String strDate = dateFormat.format(date);
        data += "," + strDate;

        MockNeat mock = MockNeat.threadLocal();
        Random rand = new Random();
        for (int i = 0; i < 3; i++) {
            String ipv4 = mock.ipv4s().val();
            data += "," + ipv4;
            data += "," + rand.nextInt(10000);
        }
        return data;
    }

    public static String genDataMatchFile1() {
        String data = "";
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String strDate = dateFormat.format(date);
        data += strDate + ",RadiusMessage";

        if (Math.random() < 0.5) {
            data += ",Start";
        } else {
            data += ",Stop";
        }

        Random rand = new Random();
        String phone = "84";
        for (int i = 0; i < 9; i++) {
            phone += rand.nextInt(10);
        }
        data += "," + phone;
        data += "," + ipPrivateMatching;
        return data;
    }

    public static String genDataMatchFile2() {
        String data = "NVL01_1";
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String strDate = dateFormat.format(date);
        data += "," + strDate;
        Random rand = new Random();

        data += "," + ipPrivateMatching;
        data += "," + rand.nextInt(10000);

        MockNeat mock = MockNeat.threadLocal();

        for (int i = 0; i < 2; i++) {
            String ipv4 = mock.ipv4s().val();
            data += "," + ipv4;
            data += "," + rand.nextInt(10000);
        }
        return data;
    }

}
