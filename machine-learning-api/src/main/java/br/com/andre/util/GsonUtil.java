package br.com.andre.util;

public class GsonUtil {

    public static String toJson(Object object) {
        return new com.google.gson.Gson().toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return new com.google.gson.Gson().fromJson(json, classOfT);
    }

}
