package com.example.demo;

import com.example.demo.redis.MsgQueueRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Array;
import java.util.List;

@RestController
public class Api {
    private  Boolean connectDB;
    private static MsgQueueRedis msgQueueRedis = new MsgQueueRedis();

    @GetMapping("/identification")
    public ResponseEntity<?> getResult(Model model){
        return ResponseEntity.ok("Hello");
    }

    @PostMapping("/identification")
    @ResponseBody
    public ResponseEntity<?> postResult(@RequestBody RequestModel requestModel){
        connectDB = true;
        if(!connectDB){
            ResponeModel requestModel1 = new ResponeModel("ERROR","error connect to database");
            return ResponseEntity.ok(requestModel1);
        }

     if(requestModel.getMsisdn() == null){
         return ResponseEntity.ok("Empty body sent in response.");
     }

     String phone = requestModel.getMsisdn();
     String address = requestModel.getAddress();
     String port = requestModel.getPort();

     String key = phone+"_"+address;
     String value = msgQueueRedis.getByKeyValue(key);
      if(value == null){
          ResponeModel requestModel1 = new ResponeModel("NOT_MATCHED",null);
          return ResponseEntity.ok(requestModel1);
      }else{
          String[] dataItem = value.split(",");
          ResponeModel requestModel1 = new ResponeModel("OK",dataItem[1]);
          return ResponseEntity.ok(requestModel1);
      }
    }
}
