package org.tiger.practice.tigerweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tiger on 2018/1/16.
 */

public class Now {
    @SerializedName("cloud")
    public String cloud;

    @SerializedName("cond_txt")
    public String condition;

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("wind_dir")
    public String windDir;

    @SerializedName("wind_sc")
    public String windSc;
}
