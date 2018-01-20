package com.example.mayank.rooms;

/**
 * Created by mayank on 1/15/2018.
 */

public class searchStudentClass {
    private String name;
    private String rsid;
    private String fname;
    private String address;
    private String smob_no;
    private String pmob_no;
    private String pic_path;

    public searchStudentClass(String pic_path, String name, String rsid, String fname, String address, String smob_no, String pmob_no){

        this.pic_path = pic_path;
        this.name = name;
        this.rsid = rsid;
        this.fname = fname;
        this.address = address;
        this.smob_no = smob_no;
        this.pmob_no = pmob_no;
    }

    public String getPic_path() {
        return pic_path;
    }

    public String getAddress() {
        return address;
    }

    public String getFname() {
        return fname;
    }

    public String getName() {
        return name;
    }

    public String getPmob_no() {
        return pmob_no;
    }

    public String getRsid() {
        return rsid;
    }

    public String getSmob_no() {
        return smob_no;
    }
}

