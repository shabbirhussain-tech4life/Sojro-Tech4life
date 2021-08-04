package com.mdcbeta.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import com.mdcbeta.data.remote.model.DayTime;

import java.lang.reflect.Type;

/**
 * Created by Shakil Karim on 4/16/17.
 */

public class DayTimeAdapter implements JsonSerializer<DayTime> {

    @Override
    public JsonElement serialize(DayTime src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("open", src.getOpen());
        jsonObject.addProperty("start_time", src.getStart_time());
        jsonObject.addProperty("end_time", src.getEnd_time());
        return jsonObject;
    }
}
