package com.zzw.beans;

public class Hearts {
    private int id;
    private String name;
    private String mobile;
    private int heart;
    private String dtime;
    private String status;

    public Hearts() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Hearts(int id, String name, String mobile, int heart, String dtime, String status) {
        super();
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.heart = heart;
        this.dtime = dtime;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public String getDtime() {
        return dtime;
    }

    public void setDtime(String dtime) {
        this.dtime = dtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
