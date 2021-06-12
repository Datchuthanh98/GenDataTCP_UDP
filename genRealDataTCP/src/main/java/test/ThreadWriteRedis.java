package test;

import redis.MsgQueueRedis;

public class ThreadWriteRedis extends Thread{

    private MsgQueueRedis msgQueueRedis;

    public ThreadWriteRedis (MsgQueueRedis msgQueueRedis){
        this.msgQueueRedis = msgQueueRedis;
    }

    @Override
    public void run(){
        int i = 0;

        while (true){
            i++;
            String mes = "redis : " + i;
            msgQueueRedis.add(mes);
            System.out.println("mes : " + mes);
        }

    }
}
