package com.leaf.erouter.annotation.bean;

/**
 * Created by suhong01 on 2018/7/4.
 */

public class RouterBean {
    public String path;
    public Class clazz;

    public RouterBean(String path, Class<?> clazz) {
        this.clazz = clazz;
        this.path = path;
    }

}
