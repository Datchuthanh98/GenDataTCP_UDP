package com.example.demo.Model;

//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponeOKv1 {
    String status;
    String time;
    String data;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



//    public ResponeModel(String status, String time, String data) {
//        this.time = time;
//        this.status = status;
//        this.data = data;
//    }

    public ResponeOKv1(){

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
