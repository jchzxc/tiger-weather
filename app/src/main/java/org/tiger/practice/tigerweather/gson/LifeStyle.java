package org.tiger.practice.tigerweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tiger on 2018/1/18.
 */

public class LifeStyle {
    @SerializedName("type")
    public String type;

    @SerializedName("brf")
    public String brief;

    @SerializedName("txt")
    public String info;
}
