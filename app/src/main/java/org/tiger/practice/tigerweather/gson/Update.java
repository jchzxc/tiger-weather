package org.tiger.practice.tigerweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tiger on 2018/1/16.
 */

public class Update {
    @SerializedName("loc")
    public String loc;

    @SerializedName("utc")
    public String utc;
}
