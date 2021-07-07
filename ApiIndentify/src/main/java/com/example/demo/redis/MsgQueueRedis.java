package com.example.demo.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.Calendar;
import java.util.Set;


public class MsgQueueRedis
{

    private String queueName;
    Jedis jedis;
    
    public MsgQueueRedis() {
        this.jedis = JedisFactory.getInstance().getJedisPool().getResource();
        if (this.jedis == null) {
            this.jedis = JedisFactory.getInstance().getJedisPool().getResource();
        }
    }
    
    public MsgQueueRedis(final String queueName) {
        this.jedis = JedisFactory.getInstance().getJedisPool().getResource();
        if (this.jedis == null) {
            this.jedis = JedisFactory.getInstance().getJedisPool().getResource();
        }
        this.queueName = queueName;
    }
    
    public void add(final String messages) {
        Jedis jedis = null;
        try {
            jedis = JedisFactory.getInstance().getJedisPool().getResource();
            if (jedis != null) {

                final Calendar rightNow = Calendar.getInstance();
                final int hour = rightNow.get(11);
                final int hourRemain = 24 - hour;
                final int expire = hourRemain * 60 * 60;
                jedis.rpush(this.queueName, new String[] { messages });
                jedis.expire(this.queueName, expire);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        if (jedis != null) {
            jedis.close();
        }
    }

    public void addByKeyVlue(final String key,final String value) {
        try {
            jedis.set(key,value);
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public String getByKeyValue(final String key) {
        try {
            String data = jedis.get(key);
            System.out.println(data);
            return data;
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String searhcKey(final String key) {
        try {
            System.out.println("keyyyyyy: "+key);
            Set<String> names=jedis.keys("*"+key+"*");
            System.out.println("names"+ names.toString());
            if(names!= null){
                String data = jedis.get((String) names.toArray()[0]);
                System.out.println("data "+ data);
                return  data;

            }
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
//    public String pollString() {
//        Jedis jedis = null;
//        String str = "";
//        try {
//            if (jedis == null) {
//                jedis = JedisFactory.getInstance().getJedisPool().getResource();
//            }
//            if (jedis != null) {
//                str = jedis.rpop(this.queueName);
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            return str;
//        }
//        finally {
//            if (jedis != null) {
//                jedis.close();
//            }
//        }
//        if (jedis != null) {
//            jedis.close();
//        }
//        return str;
//    }
    
//    public long getSize() {
//        Jedis jedis = null;
//        Long result = 0L;
//        try {
//            jedis = JedisFactory.getInstance().getJedisPool().getResource();
//            result = jedis.llen(this.queueName);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            return result;
//        }
//        finally {
//            if (jedis != null) {
//                jedis.close();
//            }
//        }
//        if (jedis != null) {
//            jedis.close();
//        }
//        return result;
//    }
//
//    public boolean isEmpty() {
//        Jedis jedis = null;
//        boolean result = false;
//        try {
//            jedis = JedisFactory.getInstance().getJedisPool().getResource();
//            if (jedis.llen(this.queueName) == 0L) {
//                result = true;
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            return result;
//        }
//        finally {
//            if (jedis != null) {
//                jedis.close();
//            }
//        }
//        if (jedis != null) {
//            jedis.close();
//        }
//        return result;
//    }
}