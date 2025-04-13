package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JsonValue {

    public StringBuilder data = new StringBuilder();
    public JsonValue(String data)
    {
        this.data.append(data);
    }


    public String getJsonType()
    {
        char first = data.charAt(0);
        if(first == '{') {
            JsonObject jsonObject = new JsonObject(data.toString());
            jsonObject.readJson();
            return "JsonObject";
        }
        else if(first == '[') {

            JsonArray jsonArray = new JsonArray(data.toString());
            jsonArray.readJson();
            return "JsonArray";
        }
        else if(first == '"') {
            readJsonValue();
            return "StringValue";
        }
        else {
            readOtherJsonValue();
            return "elseValue";
        }
    }
    public JsonArray readJsonValueArray()
    {
        JsonArray b= new JsonArray(data.toString());
        return b;

    }
    public JsonObject readJsonValueObject() {
        JsonObject a = new JsonObject(data.toString());
        return a;
    }
    public String readJsonValue()
    {
        StringBuilder value = new StringBuilder();

        if(data.charAt(data.length()-1) != '"') System.out.println("نامی که ثبت شده از قواعد نامگذاری پیروی نمیکند.3");
        for(int j=1;j<data.length()-1;j++)
        {

            if(data.charAt(j) == '\\')
            {
                if(data.charAt(j+1) == '\\'){
                    value.append(data.charAt(j));
                    j++;
                }
                if(data.charAt(j+1) == '"'){
                    value.append(data.charAt(j));
                    j++;
                }
                else System.out.println("نامی که ثبت شده از قواعد نامگذاری پیروی نمیکند.1");
            }
            else if(data.charAt(j) == '"' && j!=data.length()-1)
            {
                System.out.println("نامی که ثبت شده از قواعد نامگذاری پیروی نمیکند.2");
            }
            value.append(data.charAt(j));


        }
        return value.toString();
    }
    public void readOtherJsonValue()
    {
        if(!data.equals("null") &&!data.equals("true") && !data.equals("false")&& !readtimeJson()) System.out.println("این فایل در قالب جیسون قرار ندارد.");
    }
    public boolean readtimeJson()
    {
        ////yyyy-MM-dd-HH:mm:ss
        String[] time = new String[4];
        time = data.toString().split("-");
        int year = Integer.parseInt(time[0]);
        int month = Integer.parseInt(time[1]);
        int day = Integer.parseInt(time[2]);
        String[] hour = new String[3];
        hour = time[3].split(":");
        int hours = Integer.parseInt(hour[0]);
        int min = Integer.parseInt(hour[1]);
        int second = Integer.parseInt(hour[2]);


        if((year >10000 && year <1000)||(month > 12 && month<1)||(day>30 && day<1) || (hours>24 && hours<1 )|| (min>60 && min <1) || (second>60 && second<1)) return false;
        return true;

    }
    public JsonObject beJson(HashMap<String,JsonValue> a )
    {
        JsonObject aa = new JsonObject("{}");

        for(Map.Entry<String,JsonValue> entery : a.entrySet())
        {
            aa.jsonAdd(entery.getKey() , entery.getValue());
        }
        return aa;
    }
    public String getData()
    {
        return data.toString();
    }


}

