package ru.sber.bootcamp.service;

import org.json.JSONObject;
import ru.sber.bootcamp.model.entity.Account;
import ru.sber.bootcamp.model.entity.Client;

import java.util.List;

public interface GsonConverter {
    List<JSONObject> convertListToGson(List objects);

    JSONObject convertObjectToJson(Object client);
}
