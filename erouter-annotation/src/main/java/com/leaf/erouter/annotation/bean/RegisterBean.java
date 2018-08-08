package com.leaf.erouter.annotation.bean;

/**
 * Created by suhong01 on 2018/7/4.
 */

public class RegisterBean {
    public String method;
    public Class clazz;
    public Class paraType;

    public RegisterBean(Class<?> clazz, String method, Class<?> paraType) {
        this.clazz = clazz;
        this.method = method;
        this.paraType = paraType;
    }

}
