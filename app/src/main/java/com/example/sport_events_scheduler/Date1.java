package com.example.sport_events_scheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Date1 {
    String date;
    boolean isvalid;

    public Date1(){
        this.date = null;
        this.isvalid = false;
    }

    public Date1(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try{
            format.setLenient(false);
            format.parse(date);
        }catch(Exception e){
            this.date = null;
            this.isvalid = false;
            return;
        }

        this.date = date;
        this.isvalid = true;

    }

    public String Get_Date(){
        return this.date;
    }

    public boolean Date_Valid(){
        return this.isvalid;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(!(obj instanceof Date1))
            return false;

        Date1 date = (Date1)obj;
        if(this.date == null && date.Get_Date() == null)
            return true;
        else if(this.date == null)
            return false;
        return this.date.equals(date.Get_Date());
    }

    @Override
    public int hashCode(){
        if(this.date == null)
            return -1;
        Pattern pattern = Pattern.compile("^(\\d+)-(\\d+)-(\\d+)$");
        Matcher matcher = pattern.matcher(this.date);

        matcher.find();

        int year = Integer.parseInt(matcher.group(1));
        int month = Integer.parseInt(matcher.group(2));
        int day = Integer.parseInt(matcher.group(3));

        return year * 1000 + month * 31 + day;
    }

    protected int compare(Date1 date1){
        if(this.date == null && date1.Get_Date() == null)
            return 0;
        else if(this.date == null)
            return -1;
        else if(date1.Get_Date() == null)
            return 1;

        Pattern pattern = Pattern.compile("^(\\d+)-(\\d+)-(\\d+)$");
        Matcher matcher = pattern.matcher(this.date);

        matcher.find();

        int year = Integer.parseInt(matcher.group(1));
        int month = Integer.parseInt(matcher.group(2));
        int day = Integer.parseInt(matcher.group(3));

        matcher = pattern.matcher(date1.Get_Date());

        matcher.find();

        int year1 = Integer.parseInt(matcher.group(1));
        int month1 = Integer.parseInt(matcher.group(2));
        int day1 = Integer.parseInt(matcher.group(3));

        if(year > year1){
            return 1;
        }
        else if(year < year1){
            return -1;
        }
        else{
            if(month > month1){
                return 1;
            }
            else if(month < month1) {
                return -1;
            }
            else{
                if(day > day1){
                    return 1;
                }
                else if(day < day1){
                    return -1;
                }
                else{
                    return 0;
                }
            }
        }


    }
}
