package com.example.kingschefs;

public class History {
    private String orderNo;
    private String orderName;
    private String orderAmount;
    private String totalPrice;

    public History(String orderNo, String orderName, String orderAmount, String totalPrice) {
        this.orderNo = orderNo;
        this.orderName= orderName;
        this.orderAmount = orderAmount;
        this.totalPrice = totalPrice;
    }
    public History(){

    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
