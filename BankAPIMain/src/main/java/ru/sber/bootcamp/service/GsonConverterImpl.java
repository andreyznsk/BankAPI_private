package ru.sber.bootcamp.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GsonConverterImpl implements GsonConverter {

    @Override
    public JSONArray convertListToGson(List objects){
        JSONArray jsonArray = new JSONArray(objects);
        return jsonArray;
    }

    @Override
    public JSONObject convertObjectToJson(Object client) {
        System.out.println(client);
        JSONObject jsonObject = new JSONObject(client);
        return jsonObject;
    }
}
