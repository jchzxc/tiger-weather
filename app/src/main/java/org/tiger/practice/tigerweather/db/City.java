package org.tiger.practice.tigerweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by tiger on 2018/1/11.
 */

public class City extends DataSupport {
    public int uid;
    public String name;
    public int provinceId;

    public City() {
        this(-1, "", -1);
    }

    public City(int uid, String name, int provinceId) {
        this.uid = uid;
        this.name = name;
        this.provinceId = provinceId;
    }

    /*
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
    */
}
