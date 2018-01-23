package org.tiger.practice.tigerweather;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.tiger.practice.tigerweather.gson.LifeStyle;
import org.tiger.practice.tigerweather.gson.Weather;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private static final String TAG = "WeatherActivity";
    private static final String TYPE_NOW = "now";
    private static final String TYPE_LIFESTYLE = "lifestyle";
    private static final String URL_WEATHER = "https://free-api.heweather.com/s6/weather/";
    private static final String WEATHER_KEY = "d10eaf52afa440fb877bf25dc5d1b34a";

    private ScrollView mWeatherSV;
    private TextView mCityTV;
    private TextView mTimeTV;
    private TextView mDegreeTV;
    private TextView mInfoTV;
    private LinearLayout mForecastLL;
    private TextView mAqiTV;
    private TextView mPm25TV;
    private TextView mComfortTV;
    private TextView mWashCarTV;
    private TextView mSportTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        // widget
        mWeatherSV = (ScrollView) findViewById(R.id.sv_weather);
        mCityTV = (TextView) findViewById(R.id.tv_city);
        mTimeTV = (TextView) findViewById(R.id.tv_time);
        mDegreeTV = (TextView) findViewById(R.id.tv_degree);
        mInfoTV = (TextView) findViewById(R.id.tv_info);
        mForecastLL = (LinearLayout) findViewById(R.id.ll_forecast);
        mAqiTV = (TextView) findViewById(R.id.tv_aqi);
        mPm25TV = (TextView) findViewById(R.id.tv_pm25);
        mComfortTV = (TextView) findViewById(R.id.tv_comfort);
        mWashCarTV = (TextView) findViewById(R.id.tv_wash_car);
        mSportTV = (TextView) findViewById(R.id.tv_sport);

        // weather
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = null;    // prefs.getString("weather", null);
        if (weatherString == null) {
            String weatherId = getIntent().getStringExtra("weather_id");
//            mWeatherSV.setVisibility(View.INVISIBLE);
            requestWeather(weatherId, TYPE_NOW);
            requestWeather(weatherId, TYPE_LIFESTYLE);
        } else {
            Weather weather = Utility.procWeatherResponse(weatherString);
            showWeatherInfo(weather);
        }
    }


    // function
    private void requestWeather(String weatherId, final String type) {
        String nowUrl = URL_WEATHER + type + "?location=" + weatherId + "&key=" + WEATHER_KEY;
        Utility.requestHttp(nowUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "request weather fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String text = response.body().string();
                final Weather weather = Utility.procWeatherResponse(text);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && weather.status.equals("ok")) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", text);
                            editor.apply();
                            showWeatherInfo(weather);
                        }
                    }
                });
            }
        });
    }

    private void showWeatherInfo(Weather weather) {
//        mWeatherSV
        mCityTV.setText(weather.basic.city);
        if (weather.now != null) {
            mDegreeTV.setText(weather.now.temperature + "℃");
            mTimeTV.setText(weather.update.loc.split(" ")[1]);
        }
        if (weather.lifestyles != null) {
            TextView tv;
            for (LifeStyle lifeStyle : weather.lifestyles) {
                tv = null;
                if (lifeStyle.type.equals("comf")) {
                    tv = mComfortTV;
                } else if (lifeStyle.equals("cw")) {
                    tv = mWashCarTV;
                } else if (lifeStyle.equals("sport")) {
                    tv = mSportTV;
                }

                if (tv != null) {
                    tv.setText(lifeStyle.brief + "  " + lifeStyle.info);
                }
            }
        }



        mInfoTV.setText("暂无");
//        mForecastLL
        mAqiTV.setText("暂无");
        mPm25TV.setText("暂无");

    }
}
