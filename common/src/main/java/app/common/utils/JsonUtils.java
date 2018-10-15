package app.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Json parsing helper.
 * Created by bedprakash.r on 07/01/18.
 */

public class JsonUtils {
    private static Gson gson = new GsonBuilder().serializeNulls().create();

    private JsonUtils() {
    }

    /**
     * To Json Converter using Goolge's Gson Package       * this method converts a simple object to a json string       *       * @param obj      * @return a json string
     */
    public static <T> String toJsonObj(T obj) {
        return gson.toJson(obj);
    }

    /**
     * Converts a collection of objects using Google's Gson Package      *       * @param objCol      * @return a json string array
     */
    public static <T> String toJsonList(List<T> objCol) {
        return gson.toJson(objCol);
    }

    /**
     * Returns the specific object given the Json String      * @param <T>      * @param jsonString      * @param obj      * @return a specific object as defined by the user calling the method
     */
    public static <T> T fromJsonToObj(String jsonString, Class<T> obj) {
        return gson.fromJson(jsonString, obj);
    }

    public static <T> T fromJsonToObj(String jsonString, Type t) {
        return gson.fromJson(jsonString, t);
    }

    /**
     * Returns a list of specified object from the given json array      * @param <T>      * @param jsonString      * @param t the type defined by the user      * @return a list of specified objects as given in the json array
     */
    public static <T> List<T> fromJsonToList(String jsonString, Type t) {
        return gson.fromJson(jsonString, t);
    }
}
