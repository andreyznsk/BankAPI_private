package ru.sber.bootcamp;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClientServerUtilTest {

    @Test
    public void parseQuery() {
        JSONObject jsonObject = ClientServerUtil.parseQuery("parmam=1&one=2");
        System.out.println(jsonObject);
        Assert.assertEquals("{\"parmam\":1,\"one\":2}", jsonObject.toString());
    }
}