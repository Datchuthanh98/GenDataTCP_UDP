package com.example.demo;

import com.example.demo.Model.*;
import com.example.demo.redis.MsgQueueRedis;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class Api {
    private  Boolean connectDB;
    private static MsgQueueRedis msgQueueRedis = new MsgQueueRedis();

    @GetMapping("/v1/identification")
    public ResponseEntity<?> getResult(Model model){
        return ResponseEntity.ok("Hello");
    }

    @PostMapping("/v1/identification")
    @ResponseBody
    public ResponseEntity<?> postResultV1(@RequestBody RequestModelv1 requestModelv1){
        connectDB = true;
//        if(!connectDB){
//            ResponeNotMatchv1 responeModel = new ResponeNotMatchv1();
//            responeModel.setStatus("ERROR");
//            responeModel.setMessgage("error connect to database");
//            return ResponseEntity.ok(responeModel);
//        }

        System.out.println("ahihi");
        msgQueueRedis.searhcKey("abc");
     if(requestModelv1.getMsisdn() == null){
         return ResponseEntity.ok("Empty body sent in response.");
     }

     String phone = requestModelv1.getMsisdn();
     String address = requestModelv1.getAddress();
     String port = requestModelv1.getPort();

     String key = phone+"_"+address;
        System.out.println("key: "+key);
     String value = msgQueueRedis.searhcKey(key);
        System.out.println("value: "+value);
      if(value == null){
          ResponeNotMatchv1 responeModel = new ResponeNotMatchv1();
          responeModel.setStatus("NOT_MATCHED");
          responeModel.setData(null);
          return ResponseEntity.ok(responeModel);
      }else{
          System.out.println("uchiha comehere");
          String[] dataItem = value.split(",");
          ResponeOKv1 responeModel = new ResponeOKv1();
          responeModel.setStatus("OK");
          responeModel.setTime(dataItem[1]);
          responeModel.setData(null);
          return ResponseEntity.ok(responeModel);
      }
    }


    @PostMapping("/v2/identification")
    @ResponseBody
    public ResponseEntity<?> postResultV2(@RequestBody RequestModelv2 requestModelv2){
        connectDB = true;
//        if(!connectDB){
//            ResponeOK responeModel = new ResponeOK();
//            responeModel.setStatus("ERROR");
//            responeModel.setMessgage("error connect to database");
//            return ResponseEntity.ok(responeModel);
//        }

        if(requestModelv2.getDestination_address() == null){
            return ResponseEntity.ok("Empty body sent in response.");
        }

        String sub_ip_address = requestModelv2.getSub_ip_address();
        String sub_ip_port = requestModelv2.getSub_ip_port();
        String destination_address = requestModelv2.getDestination_address();

        String key = sub_ip_address+"_"+sub_ip_port;
//        if(destination_address != null){
//            key = key+"_"+sub_ip_address;
//        }
        String value = msgQueueRedis.searhcKey(key);

        if(value == null){
            ResponeNotMatchv2 responeModel = new ResponeNotMatchv2();
            responeModel.setStatus("NOT_MATCHED");
            responeModel.setData(null);
            return ResponseEntity.ok(responeModel);
        }else{
            String[] dataItem = value.split(",");
            ResponeOKv2 responeModel = new ResponeOKv2();
            responeModel.setStatus("OK");
            responeModel.setMSISDN(dataItem[11]);
            return ResponseEntity.ok(responeModel);
        }
    }

}
