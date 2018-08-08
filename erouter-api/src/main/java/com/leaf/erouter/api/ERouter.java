package com.leaf.erouter.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.leaf.erouter.annotation.bean.RegisterBean;
import com.leaf.erouter.annotation.bean.RouterBean;
import com.leaf.erouter.api.template.IRegister;
import com.leaf.erouter.api.template.IRouter;
import com.leaf.erouter.api.utils.ClassUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by suhong01 on 2018/7/5.
 */

public class ERouter {
    private static volatile ERouter mRouter;
    private static Object object = new Object();
    private static final Map<String, RouterBean> mInfoMap = new HashMap<>();
    private Map<String, Map<String, RegisterBean>> mEventMap = new HashMap<>();
    private volatile static boolean isinitialized = false;
    private EasyRegisterUtils registerUtils;
    public static final String EROUTER_PKG_NAME_ROUTER = "com.leaf.erouter.router";
    public static final String EROUTER_PKG_NAME_REGISTER = "com.leaf.erouter.register";

    private ERouter() {
    }

    public static ERouter getInstance() {
        if (mRouter == null) {
            synchronized(object) {
                if (mRouter == null) {
                    mRouter = new ERouter();
                }
            }
        }
        return mRouter;
    }

    public void init(Context context) {
        isinitialized = true;
        initRouterModule(context);
        initRouterRegister(context);
    }

    public void initRouterModule(Context context) {
        Set<String> routerMap = null;
        try {
            routerMap = ClassUtils.getFileNameByPackageName(context.getApplicationContext(), EROUTER_PKG_NAME_ROUTER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (routerMap != null) {
            Iterator<String> iterable = routerMap.iterator();
            while (iterable.hasNext()) {
                String routerClass = iterable.next();
                Log.i("hong", "routerClass= " + routerClass);
                try {
                    if (!TextUtils.isEmpty(routerClass)) {
                        Class<?> clazz = Class.forName(routerClass);
                        IRouter mRouter = (IRouter) clazz.newInstance();
                        if (mRouter != null) {
                            mRouter.loadInfo(mInfoMap);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void initRouterRegister(Context context) {
        Set<String> registerMap = null;
        try {
            registerMap =
                    ClassUtils.getFileNameByPackageName(context.getApplicationContext(), EROUTER_PKG_NAME_REGISTER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (registerMap != null) {
            Iterator<String> iterable = registerMap.iterator();
            while (iterable.hasNext()) {
                String routerClass = iterable.next();
                Log.i("hong", "routerClass initRouterRegister= " + routerClass);
                try {
                    if (!TextUtils.isEmpty(routerClass)) {
                        Class<?> clazz = Class.forName(routerClass);
                        IRegister mRouter = (IRegister) clazz.newInstance();
                        if (mRouter != null) {
                            mRouter.loadInfo(mEventMap);
                        }
                    }
                    registerUtils = new EasyRegisterUtils(mEventMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void post(Object object) {
        if (!isinitialized) {
            throw new IllegalStateException(" The method \"init()\" Should be called firstly !");
        }
        if (registerUtils != null) {
            registerUtils.post(object);
        }
    }

    public void register(Object object) {
        if (registerUtils != null) {
            registerUtils.register(object);
        }
    }

    public void unregister(Object object) {
        if (registerUtils != null) {
            registerUtils.unregister(object);
        }
    }

    public Build build(Context mContext) {
        if (!isinitialized) {
            throw new IllegalStateException(" The method \"init()\" Should be called firstly !");
        }
        if (mContext == null) {
            throw new NullPointerException("context should not be null!");
        }
        return new Build(mContext);
    }

    public static class Build {
        private Bundle mBundle;
        private Context mContext;
        private Intent mIntent;

        public Build(Context mContext) {
            this.mContext = mContext;
        }

        public Build with(Bundle mBundle) {
            this.mBundle = mBundle;
            return this;
        }

        public Build setIntent(Intent mIntent) {
            this.mIntent = mIntent;
            return this;
        }

        public void skipTo(String path) {
            if (TextUtils.isEmpty(path)) {
                throw new NullPointerException("Path should not be null!");
            }
            if (mIntent == null) {
                mIntent = new Intent();
            }
            if (mInfoMap.containsKey(path)) {
                RouterBean routerBean = mInfoMap.get(path);
                if (routerBean != null) {
                    mIntent.setClass(mContext, routerBean.clazz);
                    if (mBundle != null) {
                        mIntent.putExtras(mBundle);
                    }
                    mContext.getApplicationContext().startActivity(mIntent);
                }
            }
        }

    }

}
