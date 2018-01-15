package org.tiger.practice.tigerweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by tiger on 2018/1/11.
 */

public class Country extends DataSupport {
    public int uid;
    public String name;
    public String weatherId;
    public int cityId;

    public Country() {
        this(-1, "", "", -1);
    }

    public Country(int uid, String name, String weatherId, int cityId) {
        this.uid = uid;
        this.name = name;
        this.weatherId = weatherId;
        this.cityId = cityId;
    }

    /*
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
    */
}
