package com.example.mayank.rooms.Student;

/**
 * Created by mayan on 1/12/2018.
 */

public class searchClass {

    private String hid;
    private String roomId;
    private String address;
    private String mob;
    private String rent;
    private String pic;

    public searchClass(String hid, String roomId, String address, String mob, String rent, String pic){

        this.hid = hid;
        this.roomId = roomId;
        this.address = address;
        this.mob = mob;
        this.rent = rent;
        this.pic = pic;
    }

    public String getAddress() {
        return address;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRent() {
        return rent;
    }

    public String getHid() {
        return hid;
    }

    public String getMob() {
        return mob;
    }

    public String getPic() {
        return pic;
    }
}
