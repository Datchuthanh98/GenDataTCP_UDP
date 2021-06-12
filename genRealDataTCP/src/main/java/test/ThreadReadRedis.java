package test;

import redis.MsgQueueRedis;

public class ThreadReadRedis extends Thread{

    private MsgQueueRedis msgQueueRedis;

    public ThreadReadRedis (MsgQueueRedis msgQueueRedis){
        this.msgQueueRedis = msgQueueRedis;
    }

    @Override
    public void run(){
        while (true){
          String mes =    msgQueueRedis.pollString();
          if (mes != null)
              System.out.println("read : " + mes);
        }

    }

}
