package com.leaf.erouter.api.template;

import java.util.Map;

import com.leaf.erouter.annotation.bean.RegisterBean;

/**
 * Created by suhong01 on 2018/8/3.
 */

public interface IRegister {
    void loadInfo(Map<String, Map<String, RegisterBean>> routerMap);
}
