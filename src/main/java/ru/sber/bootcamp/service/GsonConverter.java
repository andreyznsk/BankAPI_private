package ru.sber.bootcamp.service;

import org.json.JSONObject;

import java.util.List;

public interface GsonConverter {
    List<JSONObject> convertListToGson(List objects);

    JSONObject convertObjectToJson(Object client);
}
