package com.ctl.utils.bean;

import java.util.Date;

public class Person {
    private int id;
    private Integer age;
    private String address;
    private float floatv;
    private Float floatVV;
    private double doublev;
    private Double doubleVV;
    private Date datenow;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getFloatv() {
        return floatv;
    }

    public void setFloatv(float floatv) {
        this.floatv = floatv;
    }

    public Float getFloatVV() {
        return floatVV;
    }

    public void setFloatVV(Float floatVV) {
        this.floatVV = floatVV;
    }

    public double getDoublev() {
        return doublev;
    }

    public void setDoublev(double doublev) {
        this.doublev = doublev;
    }

    public Double getDoubleVV() {
        return doubleVV;
    }

    public void setDoubleVV(Double doubleVV) {
        this.doubleVV = doubleVV;
    }

    public Date getDatenow() {
        return datenow;
    }

    public void setDatenow(Date datenow) {
        this.datenow = datenow;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", floatv=" + floatv +
                ", floatVV=" + floatVV +
                ", doublev=" + doublev +
                ", doubleVV=" + doubleVV +
                ", datenow=" + datenow +
                ", max=" + max +
                ", price=" + price +
                ", priceMax=" + priceMax +
                ", jine=" + jine +
                ", jineMax=" + jineMax +
                ", success=" + success +
                ", result=" + result +
                ", name='" + name + '\'' +
                ", savetime=" + savetime +
                ", content='" + content + '\'' +
                '}';
    }

    Integer max;
    Float price;
    float priceMax;
    Double jine;
    double jineMax;
    boolean success;
    Boolean result;
    String name;
    Date savetime;
    String content;

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public float getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(float priceMax) {
        this.priceMax = priceMax;
    }

    public Double getJine() {
        return jine;
    }

    public void setJine(Double jine) {
        this.jine = jine;
    }

    public double getJineMax() {
        return jineMax;
    }

    public void setJineMax(double jineMax) {
        this.jineMax = jineMax;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getSavetime() {
        return savetime;
    }

    public void setSavetime(Date savetime) {
        this.savetime = savetime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
