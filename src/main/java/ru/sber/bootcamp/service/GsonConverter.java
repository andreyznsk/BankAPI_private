package ru.sber.bootcamp.service;

import org.json.JSONObject;
import ru.sber.bootcamp.model.entity.Account;

import java.util.List;

public interface GsonConverter {
    List<JSONObject> convertListToGson(List objects);
}
