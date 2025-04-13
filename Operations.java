package com.company;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Operations {

    public String data ;
    public String accountName;


    public HashMap<String,JsonValue> names;
    public HashMap<String,JsonValue> Myinformation = new HashMap<>();

    public Operations(String accountName,String data)
    {
        Scanner input = new Scanner(System.in);
        this.data = data;
        this.accountName = accountName;

        JsonObject datas = new JsonObject(this.data);
        names = datas.readJson();
        if(names.containsKey(accountName))
        {
            JsonObject Myaccount = new JsonObject(names.get(accountName).getData());
            Myinformation = Myaccount.readJson();
        }
        System.out.println("به اکانت خود خوش آمدید.");

        while (true) {
            ////////////////////////////////////////////????????????????????????????????//
            JsonObject jsonObject = new JsonObject(Myinformation.get("History").getData());
            History MyHistory = new History(jsonObject);
            print();
            String operation = input.next();

            if(operation.equals("balance")) {
                balance();
            }
            else if(operation.equals("history")) {
                MyHistory.showHistory();
            }
            else if(operation.equals("deposit")) {
                int plus = input.nextInt();
                deposite(plus);
                MyHistory.addToHistory("deposit" , plus);
            }
            else if(operation.equals("withdraw")) {

                int min = input.nextInt();
                if(withdraw(min)) MyHistory.addToHistory("withdraw",min);
            }
            else if(operation.equals("account-name")) {

                accountName();
            }
            else if(operation.equals("transfer")) {

                String accountnum = input.next();
                int m = input.nextInt();

                Random random = new Random();
                int id = 10000000 + random.nextInt(90000000);
                String ID =String.valueOf(id);

                if(transfer(m , accountnum,ID)) MyHistory.addToHistory("transfer", accountnum, m,ID);
            }
            else if(operation.equals("undo")) {
                String id = input.next();
                if(undo(id)) MyHistory.delete(id);
            }
            else if(operation.equals("account-number")) {
                accountNumber();
            }
            else if(operation.equals("back")) {
                return;
            }
            else System.out.println("این عملیات وجود ندارد، لطفا دوباره امتحان کنید.");

            JsonValue myUpdateHistory = new JsonValue(MyHistory.getMyHistory().getData());

            Myinformation.put("History" ,myUpdateHistory);
            updateData(Myinformation,accountName);
            makeDataBank();
        }
    }



    ////////////////////////////////////////////////////////////////operations
    public boolean transfer(int min,String maghsad,String ID)
    {
        if(names.containsKey(maghsad))
        {
            String money = Myinformation.get("Money").getData();
            int myMoney = Integer.parseInt(money);

            if(myMoney >= min){

                myMoney = myMoney - min;
                money = String.valueOf(myMoney);
                JsonValue jmoney = new JsonValue(money);
                Myinformation.put("Money", jmoney);


                JsonObject maghsadaccount = new JsonObject(names.get(maghsad).getData());
                HashMap<String,JsonValue> maghinformation = maghsadaccount.readJson();


                String maghmoney = maghinformation.get("Money").getData();
                int maghsMoney = Integer.parseInt(maghmoney);

                maghsMoney = maghsMoney + min;

                maghmoney = String.valueOf(maghsMoney);
                JsonValue jmaghmoney = new JsonValue(maghmoney);
                maghinformation.put("Money",jmaghmoney);

                ///////////////////////////////History update for maghsad


                History maghHistory = new History(maghinformation.get("History").readJsonValueObject());
                maghHistory.addToHistory("transferFrom" ,accountName,min,ID);
                maghinformation.put("History" ,maghHistory.getMyHistory());
                updateData(maghinformation, maghsad);

                System.out.println("انتقال وجه با موفقیت انجام شد.");
                return true;
            }
            else System.out.println("موجودی کافی نیست، عملیات انجام نمی شود. دوباره امتحان کنید.");
        }
        else System.out.println("چنین کاربری وجود ندارد.");
        return false;
    }
    public void deposite(int plus)
    {
        String money = Myinformation.get("Money").getData();
        int myMoney = Integer.parseInt(money);
        myMoney = myMoney + plus;
        money = String.valueOf(myMoney);
        JsonValue jmoney = new JsonValue(money);
        Myinformation.put("Money", jmoney);
        System.out.println("وجه با موفقیت واریز شد.");
    }
    public boolean withdraw(int min)
    {
        String money = Myinformation.get("Money").getData();
        int myMoney = Integer.parseInt(money);
        if(myMoney >= min){
            myMoney = myMoney - min;
            money = String.valueOf(myMoney);
            JsonValue jmoney = new JsonValue(money);
            Myinformation.put("Money", jmoney);
            System.out.println("وجه با موفقیت برداشت شد.");
            return true;
        }
        else System.out.println("موجودی کافی نیست، عملیات انجام نمی شود. دوباره امتحان کنید.");
        return false;
    }
    public boolean undo(String id)
    {
        HashMap<String,JsonValue> his;
        his = Myinformation.get("History").readJsonValueObject().readJson();
        if(his.containsKey(id))
        {


            HashMap<String,JsonValue> operations = his.get(id).readJsonValueObject().readJson();

            String Oper = operations.get("Operation").getData();

            String meghdar = operations.get("Meghdar").getData();
            int meghdars = Integer.parseInt(meghdar);

            if(Oper.equals("transfer"))
            {
                String account = operations.get("account").getData();
                if(transferUndo(meghdars,account,id))return true;
            }
            else if(Oper.equals("transferFrom"))
            {
                System.out.println("شما نمی توانید این عملیات را لغو کنید! لطفا با اکانت دیگر وارد شوید.");
            }
            else if(Oper.equals("withdraw"))
            {
                deposite(meghdars);
                return true;
            }
            else if(Oper.equals("deposit"))
            {
                if(withdraw(meghdars))return true;
            }
        }
        else System.out.println("این id موجود نمی باشد.");
        return false;

    }

    public boolean transferUndo(int min,String maghsad, String maghID)
    {
        if(names.containsKey(maghsad))
        {
            String money = Myinformation.get("Money").getData();
            int myMoney = Integer.parseInt(money);

            JsonObject maghsadaccount = new JsonObject(names.get(maghsad).getData());
            HashMap<String,JsonValue> maghinformation = maghsadaccount.readJson();

            String maghmoney = maghinformation.get("Money").getData();
            int maghsMoney = Integer.parseInt(maghmoney);
            if(maghsMoney >= min){

                myMoney = myMoney + min;
                money = String.valueOf(myMoney);
                JsonValue jmoney = new JsonValue(money);
                Myinformation.put("Money", jmoney);

                maghsMoney = maghsMoney - min;
                maghmoney = String.valueOf(maghsMoney);
                JsonValue jmaghmoney = new JsonValue(maghmoney);
                maghinformation.put("Money",jmaghmoney);

                ///////////////////////////////History update for maghsad
                JsonObject maghH = new JsonObject(maghinformation.get("History").getData());
                History maghHistory = new History(maghH);
                maghHistory.delete(maghID);
                maghinformation.put("History" , maghHistory.getMyHistory());
                updateData(maghinformation, maghsad);

                System.out.println("بازگشت وجه با موفقیت انجام شد.");
                return true;
            }
            else System.out.println("موجودی اکانت دیگز کافی نیست، عملیات نجام نمی شود. دوباره امتحان کنید.");
        }
        else System.out.println("چنین کاربری دیگر وجود ندارد.");
        return false;
    }

    public void accountName()
    {
        System.out.println("نام کاربری شما : "+ Myinformation.get("accountName").getData());
    }
    public void accountNumber()
    {
        System.out.println("شماره حساب شما : "+ accountName );
    }
    public void balance()
    {
        System.out.println("موجودی شما : "+Myinformation.get("Money").getData());
    }

    ////////////////////////////////////////////
    public void makeDataBank()
    {
        File filedata = new File("BankData.txt");


        if(filedata.delete()) {
            String filePath = "BankData.txt";

            try (BufferedWriter filedata1 = new BufferedWriter(new FileWriter(filePath))) {
                filedata1.write(data);
            } catch (IOException c) {
                c.printStackTrace();
            }
        }
        else System.out.println("تغییرات اعمال نشد، دوباره امتحان کنید.");
    }
    ///////////////////////////////////////////////////////////////////////////////////////////update jsonData not hash

    public String getData()
    {
        return data;
    }

    public void updateData(HashMap<String,JsonValue> information, String accountName)
    {
        JsonValue jsonValue =new JsonValue("{}");
        names.put(accountName , jsonValue.beJson(information));
        data = jsonValue.beJson(names).getData();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////history
    public void print()
    {
        System.out.print("1.گرفتن موجودی : ");
        System.out.println("balance");
        System.out.print("2.نمایش تاریخچه ی حساب : ");
        System.out.println("history");
        System.out.print("3.واریز پول به حساب خود : ");
        System.out.print("deposit ");
        System.out.println("مقدار");
        System.out.print("4.برداشت وجه : ");
        System.out.print("withdraw ");
        System.out.println("مقدار");
        System.out.println("5.انتقال وجه : ");
        System.out.print("transfer ");
        System.out.println("to_accountNumber money");
        System.out.print("6.لغو عملیات با ای دی عملیات : ");
        System.out.print("undo ");
        System.out.println("ID");
        System.out.print("7.نمایش شماره حساب : ");
        System.out.println("account-number");
        System.out.print("8.نمایش نام کاربری : ");
        System.out.println("account-name");
        System.out.print("9.خروج از حساب کاربری : ");
        System.out.println("back");
    }
}
