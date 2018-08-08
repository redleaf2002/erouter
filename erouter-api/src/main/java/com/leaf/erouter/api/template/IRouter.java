package com.leaf.erouter.api.template;

import java.util.Map;

import com.leaf.erouter.annotation.bean.RouterBean;

/**
 * Created by suhong01 on 2018/8/3.
 */

public interface IRouter {
    void loadInfo(Map<String, RouterBean> routerMap);
}
