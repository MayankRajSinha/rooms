package com.example.mayank.rooms.House_owner;

/**
 * Created by mayank on 12/25/2017.
 */

public class roomStatusClass {

    private String roomId;
    private String forGender;
    private String rent;
    private String accomodated;
    private String guest;
    private String picPath;

    public roomStatusClass(String roomId, String forGender, String rent, String accomodated, String guest, String picPath){
        this.roomId = roomId;
        this.forGender = forGender;
        this.rent = rent;
        this.accomodated = accomodated;
        this.guest = guest;
        this.picPath = picPath;
    }

    public String getAccomodated() {
        return accomodated;
    }

    public String getForGender() {
        return forGender;
    }

    public String getGuest() {
        return guest;
    }

    public String getRent() {
        return rent;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getPicPath() {
        return picPath;
    }
}
