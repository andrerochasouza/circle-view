package br.com.andre.util;

public class GsonUtil {

    public static String toJson(Object object) {
        try {
            return new com.google.gson.Gson().toJson(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        try {
            return new com.google.gson.Gson().fromJson(json, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
