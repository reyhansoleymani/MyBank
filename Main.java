package com.company;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.*;


public class Main {

    public static String data = new String();

    public static void main(String[] args) {

        System.out.println("خوش آمدید.");
        System.out.println("توجه، سیستم هش کردن رمز باعث شد شرط // در ذخیره ی داده ها در جیسون ولیو در نظر گرفته نشود.");
        vorod();
    }

    public static void vorod(){
        Scanner input = new Scanner(System.in);

        while (true) {

            readDataBank();
            System.out.println("در صورت وجود اکانت با دستور login، وارد شوید. سپس شماره حساب!! و پسورد خود را وارد کنید.");
            System.out.println("برای ساخت اکانت با دستور signup، وارد شوید. سپس نام کاربری و پسورد خود را وارد کنید.");
            System.out.println("برای خروج از برنامه، دستور exit را وارد کنید.");

            String operation = input.next();

            /////////////////////////////////////////////////////////////////////////////////signup:
            if (operation.equals("signup")) {

                String accountName = input.next();
                String pass = input.next();
                /////////////////////////////////////////////////////////////////signup errors:
                if (!checkname(accountName))
                {
                    System.out.println("اکانت شما ساخته شد.");
                    addAccount(accountName,pass);
                }
                else System.out.println("این نام کاربری از قبل موجود است.");
            }
            /////////////////////////////////////////////////////////////////////////////////////////login:

            else if (operation.equals("login")) {

                String accountNumber = input.next();
                String pass = input.next();

                ///////////////////////////////////////////////////////////////////////////////login errors:

                if(!checkname(accountNumber)) System.out.println("شماره حساب وجود ندارد.");
                else if(!checkPass(accountNumber, pass)) System.out.println("پسورد نادرست است.");
                else{
                    Operations operations = new Operations(accountNumber,data);
                    data = operations.getData();
                }
            }
            else if(operation.equals("exit"))
            {
                System.out.println("شما از بانک خارج شدید.");
                return;
            }
            else System.out.println("این عملیات وجود ندارد. لطفا دوباره امتحان کنید.");
        }
    }







//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void addAccount(String accountName, String pass2)
    {

        JsonObject accountinformatin = new JsonObject("{}");


        // الگوریتم رمزگذاری دوطرفه AES://////////////////////بخش هش کردن از چی پی تی گرفتم

        String encryptionKey = "1234567890123456";  // کلید برای رمزگذاری و رمزگشایی
        try {

            String encryptedPassword = encrypt(pass2, encryptionKey);

            JsonValue pass = new JsonValue(encryptedPassword);

            //////////////
            JsonValue money = new JsonValue("0");
            JsonObject history = new JsonObject("{}");

            JsonValue jname = new JsonValue(accountName);


            Random random = new Random();
            int randomn = 10000000 + random.nextInt(90000000);
            System.out.println("این شماره حساب شمااست. لطفا به خاطر بسپارید! : " + String.valueOf(randomn));

            accountinformatin.jsonAdd("accountName" , jname);
            accountinformatin.jsonAdd("Pass" , pass);
            accountinformatin.jsonAdd("Money" , money);
            accountinformatin.jsonAdd("History" ,history);


            JsonValue jaccount = new JsonValue(accountinformatin.getData());


            JsonObject dataUpdate = new JsonObject(data);
            dataUpdate.jsonAdd(String.valueOf(randomn),jaccount);


            data = dataUpdate.getData();
            makeDataBank();

            Operations operations = new Operations(String.valueOf(randomn),data);

            //////////////////////////
        }
        catch (Exception a)
        {
            System.out.println("خطا در رمزگذاری پسورد: " + a.getMessage());
            return;
        }


    }


///////////////////////////////////////////////////////////////////////check parts
    public static boolean checkname(String accountName)
    {
        JsonObject datas = new JsonObject(data);
        HashMap<String,JsonValue> names = datas.readJson();

        if(names.containsKey(accountName)) return true;
        else return false;
    }

    public static boolean checkPass(String accountName, String Mypass)
    {
        JsonObject datas = new JsonObject(data);
        HashMap<String,JsonValue> names = datas.readJson();

        JsonObject Myaccount = new JsonObject(names.get(accountName).getData());
        HashMap<String,JsonValue> information = Myaccount.readJson();
        try {
            // رمزگشایی پسورد
            String Password = decrypt(information.get("Pass").getData(), "1234567890123456");
            if (Password.equals(Mypass)) return true;
            else return false;
        } catch (Exception a) {
            System.out.println("خطا در رمزگشایی پسورد: " + a.getMessage());
            return false;
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////file part
    public static void readDataBank() {
        String filePath = "BankData.txt";

        try (BufferedReader read = new BufferedReader(new FileReader(filePath))) {
            data = read.readLine();

        } catch (IOException e) {

            data = "{}";

            try (BufferedWriter filedata = new BufferedWriter(new FileWriter(filePath))) {
                filedata.write(data);

            } catch (IOException a) {
                a.printStackTrace();
            }
        }

    }
    public static void makeDataBank()
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
    ///////////////////////////////////////////////////////////////////////////////////////////////hashing
    public static SecretKeySpec createKey(String password) throws Exception {
        byte[] key = password.getBytes("UTF-8");
        return new SecretKeySpec(key, "AES");
    }

    // متد برای رمزگذاری داده با استفاده از AES
    public static String encrypt(String data, String password) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec key = createKey(password);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    // متد برای رمزگشایی داده با استفاده از AES
    public static String decrypt(String encryptedData, String password) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec key = createKey(password);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedData);
    }

}


