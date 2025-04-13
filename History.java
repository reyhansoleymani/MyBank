package com.company;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import java.util.Random;

public class History {

    public HashMap<String,JsonValue> history;


    public History(JsonObject myhistory)
    {
        history = myhistory.readJson();
    }
    public void showHistory()
    {
        System.out.println("تاریخچه ی حساب شما : ");


        for (String key : history.keySet())
        {
            HashMap<String,JsonValue> hnow = new HashMap<>();
            hnow=history.get(key).readJsonValueObject().readJson();
            System.out.println("شماره تراکنش: " + key);
            System.out.println("نوع تراکنش : " + hnow.get("Operation").getData());
            System.out.println("مقدار  : " + hnow.get("Meghdar").getData());
            System.out.println("زمان : " + hnow.get("Time").getData());

            if(hnow.containsKey("account"))System.out.println("با اکانت : " + hnow.get("account").getData());

        }
    }
    public JsonObject getMyHistory()
    {
        JsonValue jsonValue =new JsonValue("{}");
        return jsonValue.beJson(history);
    }
    public void addToHistory(String operation , int m)
    {

        JsonObject operations = new JsonObject("{}");

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
        String time = now.format(format);

        String m1 = String.valueOf(m);
        Random random = new Random();
        int id = 10000000 + random.nextInt(90000000);


        JsonValue jtime = new JsonValue(time);
        JsonValue jm = new JsonValue(m1);
        JsonValue joperation = new JsonValue(operation);



        operations.jsonAdd("Operation" ,joperation);
        operations.jsonAdd("Time" , jtime);
        operations.jsonAdd("Meghdar" , jm);

        JsonValue o = new JsonValue(operations.getData());
        history.put(String.valueOf(id),o);
    }
    public void addToHistory(String operation ,String accountName, int m,String ID)
    {
        JsonObject operations = new JsonObject("{}");

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
        String time = now.format(format);

        String m1 = String.valueOf(m);

        JsonValue jtime = new JsonValue(time);
        JsonValue jm = new JsonValue(m1);
        JsonValue joperation = new JsonValue(operation);
        JsonValue jaccount = new JsonValue(accountName);


        operations.jsonAdd("Operation" ,joperation);
        operations.jsonAdd("account" , jaccount);
        operations.jsonAdd("Time" , jtime);
        operations.jsonAdd("Meghdar" , jm);

        JsonValue o = new JsonValue(operations.getData());
        history.put(String.valueOf(ID),o);
    }

    public void delete(String id) {
        history.remove(id);
    }
}
