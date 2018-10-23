package com.grasset.tutapp.tutapp_grasset;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by eleves on 2018-10-02.
 */

public class DataManager {
    private String myToken;
    private String myId;
    private String myTutor;
    private Integer myTutorId;
    private String myDataInterval;
    private String myDate;
    private String myHour;
    private HashMap<String, Integer> myCours;
    private HashMap<String, Integer> myTutorList;
    private Integer myCoursSelected;
    private String myCoursID;
    private Boolean myTutoratStatus;
    private ArrayList<String[]> myDatesAndHours = new ArrayList<>();
    private ArrayList<String[]> myTutoratList = new ArrayList<>();
    private String myCoursLabel;
    private Integer myCalendarId;

    public ArrayList<String[]> getMyTutoratList() {
        return myTutoratList;
    }

    public void setMyTutoratList(ArrayList<String[]> myTutoratList) {
        this.myTutoratList = myTutoratList;
    }

    public Integer getMyCalendarId() {
        return myCalendarId;
    }

    public void setMyCalendarId(Integer myCalendarId) {
        this.myCalendarId = myCalendarId;
    }

    public String getMyCoursLabel() {
        return myCoursLabel;
    }

    public void setMyCoursLabel(String myCoursLabel) {
        this.myCoursLabel = myCoursLabel;
    }

    public ArrayList<String[]> getMyDatesAndHours() {
        return myDatesAndHours;
    }

    public void setMyDatesAndHours(ArrayList<String[]> myDatesAndHours) {
        this.myDatesAndHours = myDatesAndHours;
    }

    public Integer getMyTutorId() {
        return myTutorId;
    }

    public void setMyTutorId(Integer myTutorId) {
        this.myTutorId = myTutorId;
    }

    public HashMap<String, Integer> getMyTutorList() {
        return myTutorList;
    }

    public void setMyTutorList(HashMap<String, Integer> myTutorList) {
        this.myTutorList = myTutorList;
    }

    public Integer getMyCoursSelected() {
        return myCoursSelected;
    }

    public void setMyCoursSelected(Integer myCoursSelected) {
        this.myCoursSelected = myCoursSelected;
    }

    public HashMap<String, Integer> getMyCours() {
        return myCours;
    }

    public void setMyCours(HashMap<String, Integer> myCours) {
        this.myCours = myCours;
    }

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

    public ArrayList<String> myDataIntervalToList() {
        ArrayList<String> myListHours = new ArrayList<>();
        myListHours.add("10h30 - 11h30");
        myListHours.add("10h45 - 11h45");
        myListHours.add("11h00 - 12h00");
        return myListHours;
    }
}