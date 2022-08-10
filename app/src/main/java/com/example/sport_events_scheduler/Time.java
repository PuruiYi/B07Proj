package com.example.sport_events_scheduler;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Time implements Comparable<Time>{
    private String time;
    private boolean isvalid;

    protected boolean Time_Valid(String time){
        Pattern pattern = Pattern.compile("^[0-2][0-9]:[0-5][0-9]$");
        Matcher matcher = pattern.matcher(time);
        if(!matcher.matches())
            return false;
        String[] time_array = time.split(":");
        int hour = Integer.parseInt(time_array[0]);
        return hour < 24;
    }

    public Time(){

    }

    public Time(String time){
        if(this.Time_Valid(time)){
            this.time = time;
            this.isvalid = true;
        }
        else{
            this.time = null;
            this.isvalid = false;
        }
    }

    public boolean Isvalid(){
        return isvalid;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null)
            return false;
        if(obj == this)
            return false;
        if(!(obj instanceof Time))
            return false;
        Time time = (Time)obj;
        return this.hashCode() == time.hashCode();
    }

    @Override
    public int hashCode(){
        if(!this.isvalid)
            return -1;
        String[] time_array = this.time.split(":");
        int hour = Integer.parseInt(time_array[0]);
        int minute = Integer.parseInt(time_array[1]);
        return hour * 60 + minute;
    }

    @Override
    public int compareTo(Time time) {
        return this.hashCode() - time.hashCode();
    }

    public static boolean Time_Conflict(Time start1, Time end1, Time start2, Time end2){
        if(!(start1.Isvalid() && end1.Isvalid() && start2.Isvalid() && end2.Isvalid()))
            return true;

        TreeSet<Time> treeset = new TreeSet<Time>();
        treeset.add(start1);
        treeset.add(end1);
        treeset.add(start2);
        treeset.add(end2);

        if(treeset.size() != 4)
            return true;

        Time[] time_array = new Time[treeset.size()];
        treeset.toArray(time_array);
        int index = Arrays.binarySearch(time_array, start1);
        if(index == 3)
            return true;
        if(!time_array[index + 1].equals(end1))
            return true;
        return false;
    }
}



