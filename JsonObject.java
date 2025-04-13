package com.company;

import java.util.HashMap;
import java.util.Map;

public class JsonObject extends JsonValue{


    public StringBuilder data = new StringBuilder();
    public HashMap<String,JsonValue> map = new HashMap<>();
    public JsonObject(String data)
    {
        super(data);
        this.data.append(data);
        readJson();

    }
    public String getData()
    {
        return data.toString();
    }
    public HashMap<String,JsonValue> readJson()
    {

        for(int i=1;i<data.length()-1;i++)
        {
            String key = findKey(i);
            i+=(key.length()+3);

            if(i>data.length()-1){
                System.out.println("json error");
                break;
            }

            String value = findvalue(i);
            JsonValue value1 = new JsonValue(value);

            if(data.charAt(i) =='"') i+=2;

            i+=value.length();

            map.put(key , value1);

        }
        return map;
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
    public String findKey(int i){

        StringBuilder key = new StringBuilder();

        if(data.charAt(i) == '"')
        {
            for(int j=i+1;j<data.length()-1;j++)
            {
                if(data.charAt(j) == '/')
                {
                    if(data.charAt(j+1) == '/'){
                        key.append(data.charAt(j));
                        j++;
                    }
                    else if(data.charAt(j+1) == '"'){
                        key.append(data.charAt(j));
                        j++;
                    }
                    else System.out.println("نامی که ثبت شده از قواعد نامگذاری پیروی نمیکند6.");
                }
                else if(data.charAt(j) == '"')
                {
                    break;
                }
                key.append(data.charAt(j));
            }

        }
        return key.toString();
    }

    public void jsonAdd(String key, JsonValue value)
    {
        String value1 = value.getData();
        data.deleteCharAt(data.length()-1);

        if(data.charAt(data.length()-1) != '{')  data.append(',');

        data.append('\"');
        data.append(key);
        data.append('\"');
        data.append(':');
        if(value1.charAt(0) != '{' && value1.charAt(0) != '[' && (value1.charAt(0)-'0' <-1 ||  value1.charAt(0)-'0' >10))
        {
            StringBuilder value2=new StringBuilder();
            value2.append('"');
            value2.append(value1);
            value2.append('"');
            data.append(value2);
        }
        else data.append(value1);
        data.append('}');
        map.put(key,value);
    }
}
