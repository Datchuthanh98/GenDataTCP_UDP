package redis;

import redis.clients.jedis.Jedis;

public class ThreadWriteRedis extends Thread{

    private MsgQueueRedis msgQueueRedis;

    public ThreadWriteRedis (MsgQueueRedis msgQueueRedis){
        this.msgQueueRedis = msgQueueRedis;
    }

    @Override
    public void run(){



        int i = 1;

        while (true){

            String mes = "redis : " + i;
            msgQueueRedis.add(mes);
            i++;

        }

    }
}
