package org.tiger.practice.tigerweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tiger on 2018/1/16.
 */

public class Basic {
    @SerializedName("location")
    public String location;

    @SerializedName("cid")
    public String weatherId;

    @SerializedName("parent_city")
    public String city;
}
