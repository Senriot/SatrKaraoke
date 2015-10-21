package com.ktvdb.allen.satrok.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Allen on 15/9/29.
 */
public class GsonUtil
{
    private static Gson gson;

    public static Gson gson()
    {
        if (gson == null)
        {
            gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,
                                                         new TimestampTypeAdapter())
                                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                                    .create();
        }
        return gson;
    }

    private static class TimestampTypeAdapter implements JsonSerializer<Timestamp>,
                                                         JsonDeserializer<Timestamp>
    {
        @Override
        public Timestamp deserialize(JsonElement json,
                                     Type typeOfT,
                                     JsonDeserializationContext context) throws JsonParseException
        {
            if (!(json instanceof JsonPrimitive))
            {
                throw new JsonParseException("The date should be a string value");
            }
            try
            {
                return new Timestamp(json.getAsLong());
            }
            catch (Exception e)
            {
                throw new JsonParseException(e);
            }
        }

        @Override
        public JsonElement serialize(Timestamp src,
                                     Type typeOfSrc,
                                     JsonSerializationContext context)
        {
            return new JsonPrimitive(src.getTime());
        }
    }
}
