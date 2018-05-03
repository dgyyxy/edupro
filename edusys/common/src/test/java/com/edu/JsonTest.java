package com.edu;

import com.edu.common.dao.pojo.QueryTypeRate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Administrator on 2018/4/22 0022.
 */
public class JsonTest {
    public static void main(String[] args) {
        String json = "[{\"questionTypeId\": 13,\"questionTypeNum\": {\"1\": 5,\"3\": 5},\"questionTypePoint\": {\"1\": 1,\"3\": 5}},{\"questionTypeId\": 16,\"questionTypeNum\": {\"3\": 5},\"questionTypePoint\": {\"3\": 1}}]";

        Type type = new TypeToken<List<QueryTypeRate>>() {
        }.getType();
        Gson gson = new Gson();
        //List<QueryTypeRate> list = gson.fromJson(json, type);
        List<QueryTypeRate> list = gson.fromJson(json, new TypeToken<List<QueryTypeRate>>() {}.getType());
        System.out.println(list.size());
    }
}
