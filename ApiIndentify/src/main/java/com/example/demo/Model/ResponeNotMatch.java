package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonInclude;

//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponeNotMatch {
    String status;
    String time;
    String data;
    String messgage;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessgage() {
        return messgage;
    }

    public void setMessgage(String messgage) {
        this.messgage = messgage;
    }

//    public ResponeModel(String status, String time, String data) {
//        this.time = time;
//        this.status = status;
//        this.data = data;
//    }

    public ResponeNotMatch(){

    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
