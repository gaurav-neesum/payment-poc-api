package com.cybersource;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.util.Iterator;
import java.util.Map.Entry;

public class Masking {
    private static final JsonElement mask = (new JsonParser()).parse("XXXXXXXXX");

    public Masking() {
    }

    public static String prepareMasking(String json) {
        String[] list = new String[]{"email", "cardNumber", "expirationDate", "expirationMonth", "cardType", "cardCode", "nameOnAccount", "currency"};

        try {
            JsonElement object = (new JsonParser()).parse(json);
            getChild(object, list);
            return object.toString();
        } catch (JsonSyntaxException var3) {
            return json;
        }
    }

    private static void getChild(JsonElement json, String[] list) {
        if (json.isJsonObject()) {
            JsonObject obj = (JsonObject)json;
            mask(obj, list);
            Iterator var3 = obj.entrySet().iterator();

            while(var3.hasNext()) {
                Entry<String, JsonElement> entry = (Entry)var3.next();
                getChild((JsonElement)entry.getValue(), list);
            }
        }

    }

    private static void mask(JsonObject obj, String[] list) {
        String[] var2 = list;
        int var3 = list.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String current = var2[var4];
            if (obj.has(current)) {
                obj.add(current, mask);
            }
        }

    }
}
