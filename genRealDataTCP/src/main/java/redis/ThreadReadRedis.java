package redis;

import redis.clients.jedis.Jedis;

public class ThreadReadRedis extends Thread{

    private MsgQueueRedis msgQueueRedis;

    public ThreadReadRedis (MsgQueueRedis msgQueueRedis){
        this.msgQueueRedis = msgQueueRedis;
    }

    @Override
    public void run(){

        while (true){

          String mes =    msgQueueRedis.pollString();

            System.out.println("read : " + mes);
        }

    }

}
