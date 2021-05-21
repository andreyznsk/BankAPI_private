package ru.sber.bootcamp.service;

import org.json.JSONObject;
import ru.sber.bootcamp.model.entity.Account;

import java.util.ArrayList;
import java.util.List;

public class GsonConverterImpl implements GsonConverter {

    @Override
    public List<JSONObject> convertListToGson(List objects){
        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (Object object : objects) {
            JSONObject jsonObject = new JSONObject(object);
            jsonObjectList.add(jsonObject);
        }
        return jsonObjectList;
    }

}
