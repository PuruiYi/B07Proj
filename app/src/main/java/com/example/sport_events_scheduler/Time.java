package com.example.sport_events_scheduler;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Time extends Date1 implements Comparable<Time>{
    private String time;
    private boolean isvalid;

    protected boolean Time_Valid(String time){
        Pattern pattern = Pattern.compile("^([0-9]|([0-2][0-9])):[0-5][0-9]$");
        Matcher matcher = pattern.matcher(time);
        if(!matcher.matches())
            return false;
        String[] time_array = time.split(":");
        int hour = Integer.parseInt(time_array[0]);
        return hour < 24;
    }

    public Time(){
        this.time = null;
        this.isvalid = false;
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

    public Time(String time, String date){
        super(date);
        if(this.Time_Valid(time)){
            this.time = time;
            this.isvalid = true;
        }
        else{
            this.time = null;
            this.isvalid = false;
        }
    }

    public boolean Time_Valid(){
        return this.isvalid;
    }

    public boolean Isvalid(){
        if(!this.Date_Valid())
            return false;
        return Time_Valid();
    }

    public String Get_Time(){
        return this.time;
    }


    @Override
    public boolean equals(Object obj){
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(!(obj instanceof Time))
            return false;
        Time time = (Time)obj;
        return this.hashCode() == time.hashCode() && super.equals(obj);
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
    public int compareTo(Time time1) {
        if(super.compare(time1) != 0)
            return super.compare(time1);
        return this.hashCode() - time1.hashCode();
    }

    public static boolean Time_Conflict(Time event1_start, Time event1_end, Time event2_start, Time event2_end){
        if(!(event1_start.Time_Valid() && event1_end.Time_Valid() &&
                event2_start.Time_Valid() && event2_end.Time_Valid()))
            return false;
        Time start1;
        Time end1;
        Time start2;
        Time end2;
        if(!(event1_start.Date_Valid() && event1_end.Date_Valid() &&
                event2_start.Date_Valid() && event2_end.Date_Valid())){
            start1 = new Time(event1_start.Get_Time());
            end1 = new Time(event1_end.Get_Time());
            start2 = new Time(event2_start.Get_Time());
            end2 = new Time(event2_end.Get_Time());
        }
        else{
            start1 = event1_start;
            end1 = event1_end;
            start2 = event2_start;
            end2 = event2_end;
        }



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

        return !time_array[index + 1].equals(end1);
    }
}



