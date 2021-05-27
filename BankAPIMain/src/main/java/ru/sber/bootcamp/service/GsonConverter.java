package ru.sber.bootcamp.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public interface GsonConverter {
    JSONArray convertListToGson(List objects);

    JSONObject convertObjectToJson(Object client);
}
