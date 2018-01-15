package org.tiger.practice.tigerweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by tiger on 2018/1/11.
 */

public class Province extends DataSupport {
    public int uid;
    public String name;

    public Province() {
        this(-1, "");
    }

    public Province(int uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    /*
    public int getId() {
        return uid;
    }

    public void setId(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    */
}
