package com.example.demo;

import org.springframework.lang.NonNull;

public class RequestModel {
    private String  msisdn;
    private String  address;
    private String  port;
    private String timestamp;



    public RequestModel(String msisdn, String address, String port) {
        this.msisdn = msisdn;
        this.address = address;
        this.port = port;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
