package com.grasset.tutapp.tutapp_grasset;

import java.util.ArrayList;

/**
 * Created by eleves on 2018-10-02.
 */

public class DataManager {
    private String myToken;
    private String myId;
    private String myTutor;
    private String myDataInterval;
    private String myDate;
    private String myHour;
    private String myCours;
    private String myCoursID;
    private Boolean myTutoratStatus;

    private static DataManager _instance;

    private DataManager() {

    }

    public static DataManager getInstance() {
        if (_instance == null) {
            _instance = new DataManager();
        }
        return _instance;
    }

    public String getMyCoursID() {
        return myCoursID;
    }

    public void setMyCoursID(String myCoursID) {
        this.myCoursID = myCoursID;
    }

    public String getMyToken() {
        return myToken;
    }

    public void setMyToken(String myToken) {
        this.myToken = myToken;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getMyTutor() {
        return myTutor;
    }

    public void setMyTutor(String myTutor) {
        this.myTutor = myTutor;
    }

    public String getMyDataInterval() {
        return myDataInterval;
    }

    public void setMyDataInterval(String myDataInterval) {
        this.myDataInterval = myDataInterval;
    }

    public String getMyDate() {
        return myDate;
    }

    public void setMyDate(String myDate) {
        this.myDate = myDate;
    }

    public String getMyHour() {
        return myHour;
    }

    public void setMyHour(String myHour) {
        this.myHour = myHour;
    }

    public String getMyCours() {
        return myCours;
    }

    public void setMyCours(String myCours) {
        this.myCours = myCours;
    }

    public ArrayList<String> myDataIntervalToList() {
        ArrayList<String> myListHours = new ArrayList<>();
        myListHours.add("10h30 - 11h30");
        myListHours.add("10h45 - 11h45");
        myListHours.add("11h00 - 12h00");
        return myListHours;
    }
}