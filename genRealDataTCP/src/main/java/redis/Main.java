package redis;

public class Main extends Thread{

    private MsgQueueRedis msgQueueRedis = new MsgQueueRedis("hau");

    @Override
    public void run(){

        ThreadWriteRedis threadWriteRedis = new ThreadWriteRedis(msgQueueRedis);
        threadWriteRedis.setPriority(NORM_PRIORITY);
        threadWriteRedis.start();

        ThreadReadRedis threadReadRedis = new ThreadReadRedis(msgQueueRedis);
        threadReadRedis.setPriority(NORM_PRIORITY);
        threadReadRedis.start();

    }


    public static void main(String[] args) {



        Main main = new Main();
        main.start();

    }

}
