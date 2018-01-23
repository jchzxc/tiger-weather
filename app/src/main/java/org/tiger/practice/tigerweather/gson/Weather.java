package org.tiger.practice.tigerweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tiger on 2018/1/18.
 */

public class Weather {
    @SerializedName("status")
    public String status;

    @SerializedName("basic")
    public Basic basic;

    @SerializedName("update")
    public Update update;

    // types
    @SerializedName("now")
    public Now now;

    @SerializedName("lifestyle")
    public List<LifeStyle> lifestyles;
}
