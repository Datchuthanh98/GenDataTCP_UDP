package com.example.demo;

import com.example.demo.Model.RequestModel;
import com.example.demo.Model.ResponeNotMatch;
import com.example.demo.Model.ResponeOK;
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
    public ResponseEntity<?> postResultV1(@RequestBody RequestModel requestModel){
        connectDB = true;
//        if(!connectDB){
//            ResponeOK responeModel = new ResponeOK();
//            responeModel.setStatus("ERROR");
//            responeModel.setMessgage("error connect to database");
//            return ResponseEntity.ok(responeModel);
//        }

     if(requestModel.getMsisdn() == null){
         return ResponseEntity.ok("Empty body sent in response.");
     }

     String phone = requestModel.getMsisdn();
     String address = requestModel.getAddress();
     String port = requestModel.getPort();

     String key = phone+"_"+address;
     String value = msgQueueRedis.getByKeyValue(key);
      if(value == null){
          ResponeNotMatch responeModel = new ResponeNotMatch();
          responeModel.setStatus("NOT_MATCHED");
          responeModel.setData(null);
          return ResponseEntity.ok(responeModel);
      }else{
          System.out.println("uchiha comehere");
          String[] dataItem = value.split(",");
          ResponeOK responeModel = new ResponeOK();
          responeModel.setStatus("OK");
          responeModel.setTime(dataItem[1]);
          responeModel.setData(null);
          return ResponseEntity.ok(responeModel);
      }
    }


    @PostMapping("/v2/identification")
    @ResponseBody
    public ResponseEntity<?> postResultV2(@RequestBody RequestModel requestModel){
        connectDB = true;
//        if(!connectDB){
//            ResponeOK responeModel = new ResponeOK();
//            responeModel.setStatus("ERROR");
//            responeModel.setMessgage("error connect to database");
//            return ResponseEntity.ok(responeModel);
//        }

        if(requestModel.getMsisdn() == null){
            return ResponseEntity.ok("Empty body sent in response.");
        }

        String phone = requestModel.getMsisdn();
        String address = requestModel.getAddress();
        String port = requestModel.getPort();

        String key = phone+"_"+address;
        String value = msgQueueRedis.getByKeyValue(key);
        if(value == null){
            ResponeNotMatch responeModel = new ResponeNotMatch();
            responeModel.setStatus("NOT_MATCHED");
            responeModel.setData(null);
            return ResponseEntity.ok(responeModel);
        }else{
            System.out.println("uchiha comehere");
            String[] dataItem = value.split(",");
            ResponeOK responeModel = new ResponeOK();
            responeModel.setStatus("OK");
            responeModel.setTime(dataItem[1]);
            responeModel.setData(null);
            return ResponseEntity.ok(responeModel);
        }
    }

}
