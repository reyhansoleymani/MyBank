package com.company;

import java.util.ArrayList;

public class JsonArray extends JsonValue{
    public StringBuilder data = new StringBuilder();
    public ArrayList<JsonValue> arrayList = new ArrayList<>();
    public JsonArray(String data)
    {
        super(data);
        this.data.append(data);
        readJson();
    }
    public ArrayList<JsonValue> readJson()
    {
        if(data.charAt(0) != '[' || data.charAt(data.length()-1) != ']') System.out.println("json error");


        for(int i=1;i<data.length()-1;i++)
        {
            String value = findvalue(i);
            if(data.charAt(i) =='"') i+=2;

            i+=value.length();

            System.out.println(value);
            JsonValue jsonValue = new JsonValue(value);

            arrayList.add(jsonValue);
        }
        return arrayList;
    }
    public String findvalue(int i)
    {

        StringBuilder value = new StringBuilder();
        if(data.charAt(i) == '{'){
            int h=-1;
            for(int j=i; j<data.length()-1;j++)
            {
                if(data.charAt(j) =='{' ) h++;
                value.append(data.charAt(j));

                if(h==0 && data.charAt(j) == '}') {

                    break;
                }
                if(data.charAt(j) == '}') h--;

            }
        }
        else if(data.charAt(i) == '[') {
            int h=-1;
            for(int j=i; j<data.length()-1;j++)
            {
                if(data.charAt(j) =='[' ) h++;
                value.append(data.charAt(j));

                if(h==0 && data.charAt(j) == ']') {

                    break;
                }
                if(data.charAt(j) == ']') h--;

            }
        }
        else if(data.charAt(i) == '"') {
            for(int j=i+1;j<data.length()-1;j++)
            {
                if(data.charAt(j) == '/')
                {
                    if(data.charAt(j+1) == '/'){
                        value.append(data.charAt(j));
                        j++;
                    }
                    else if(data.charAt(j+1) == '"'){
                        value.append(data.charAt(j));
                        j++;
                    }
                    else System.out.println("نامی که ثبت شده از قواعد نامگذاری پیروی نمیکند8.");
                }
                else if(data.charAt(j) == '"')
                {
                    i=j;
                    break;
                }
                value.append(data.charAt(j));
            }
        }
        else if(data.charAt(i) - '0' > -1 && data.charAt(i) -'0' < 10 ) {
            for(int j=i;j<data.length()-1;j++)
            {
                if((data.charAt(j) - '0' > -1 && data.charAt(j) -'0' < 10) || data.charAt(j) == '-' || data.charAt(j)  == ':') value.append(data.charAt(j));
                else {
                    break;
                }
            }
        }
        return value.toString();

    }
    public String getData()
    {
        return data.toString();
    }
    public void jsonAdd(JsonValue value)
    {
        String value1 = value.getData();

        data.deleteCharAt(data.length()-1);

        if(data.charAt(data.length()-1) != '[')  data.append(',');
        data.append(value1);
        data.append(']');
    }
}
