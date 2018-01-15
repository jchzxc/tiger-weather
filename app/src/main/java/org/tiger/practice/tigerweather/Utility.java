package org.tiger.practice.tigerweather;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tiger.practice.tigerweather.db.City;
import org.tiger.practice.tigerweather.db.Country;
import org.tiger.practice.tigerweather.db.Province;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by tiger on 2018/1/11.
 */

public class Utility {
    private static final String TAG = "Utility";

    private static final String RESPONSE_ID = "id";
    private static final String RESPONSE_NAME = "name";
    private static final String RESPONSE_WEATHER_ID = "weather_id";

    public static void requestHttp(String address, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    public static boolean procProvinceResponse(String response) {
        if (TextUtils.isEmpty(response)) {
            return false;
        }

        try {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                Province province = new Province(object.getInt(RESPONSE_ID), object.getString(RESPONSE_NAME));
                Log.d(TAG, "proc Province: id = " + object.getInt(RESPONSE_ID) + ", name = " + object.getString(RESPONSE_NAME));
                province.save();
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean procCityResponse(String response, int provinceId) {
        if (TextUtils.isEmpty(response)) {
            return false;
        }

        try {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                City city = new City(object.getInt(RESPONSE_ID), object.getString(RESPONSE_NAME), provinceId);
                Log.d(TAG, "proc City: id = " + object.getInt(RESPONSE_ID) + ", name = " + object.getString(RESPONSE_NAME) + ", provinceId = " + provinceId);
                city.save();
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean procCountryResponse(String response, int cityId) {
        if (TextUtils.isEmpty(response)) {
            return false;
        }

        try {
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                Country country = new Country(object.getInt(RESPONSE_ID), object.getString(RESPONSE_NAME), object.getString(RESPONSE_WEATHER_ID), cityId);
                Log.d(TAG, "proc Country: id = " + object.getInt(RESPONSE_ID) + ", name = " + object.getString(RESPONSE_NAME) + ", weatherId = " + object.getString(RESPONSE_WEATHER_ID) + ", uid = " + cityId);
                country.save();
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}
