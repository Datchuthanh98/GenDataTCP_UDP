package com.example.demo.Model;

//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponeNotMatchv1 {
    String status;
    String data;

    public ResponeNotMatchv1(){

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
