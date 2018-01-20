package com.example.mayank.rooms.House_owner;

/**
 * Created by mayank on 1/11/2018.
 */

public class renterClass {

    private String stuPic;
    private String name;
    private String rid;
    private String fname;
    private String address;
    private String snumber;
    private String fnumber;

    public renterClass(String stuPic, String name,String rid, String fname, String address, String snumber, String fnumber){
        this.stuPic = stuPic;
        this.name = name;
        this.rid = rid;
        this.fname = fname;
        this.address = address;
        this.snumber = snumber;
        this.fnumber = fnumber;
    }

    public String getRid() {
        return rid;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getFname() {
        return fname;
    }

    public String getFnumber() {
        return fnumber;
    }

    public String getSnumber() {
        return snumber;
    }

    public String getStuPic() {
        return stuPic;
    }
}

