package com.leaf.erouter.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.leaf.erouter.annotation.bean.RegisterBean;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by suhong01 on 2018/7/5.
 */

public class EasyRegisterUtils {
    private Map<String, Map<String, RegisterBean>> mEventMap;
    private CopyOnWriteArrayList<Object> mRegisterList = new CopyOnWriteArrayList();

    public EasyRegisterUtils(Map<String, Map<String, RegisterBean>> mEventMap) {
        this.mEventMap = mEventMap;
    }

    //    private void init() {
    //        EasyRegisterManager.loadInfo(mEventMap);
    //    }

    public void post(Object object) {
        String keyClazz = object.getClass().getName();
        if (!TextUtils.isEmpty(keyClazz)) {
            if (mEventMap.containsKey(keyClazz)) {
                Map<String, RegisterBean> map = mEventMap.get(keyClazz);
                for (int i = 0; i < mRegisterList.size(); i++) {
                    Object objectInfo = mRegisterList.get(i);
                    if (objectInfo != null && map.containsKey(objectInfo.getClass().getName())) {
                        RegisterBean bookBean = map.get(objectInfo.getClass().getName());
                        Log.i("EasyRegisterUtils",
                                "clazz bookBean=" + bookBean.method + " " + bookBean.paraType.getName());
                        try {
                            Method method = objectInfo.getClass().getDeclaredMethod(bookBean.method, bookBean.paraType);
                            if (method != null) {
                                method.invoke(objectInfo, object);
                            }
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

        }

    }

    public void register(Object object) {
        if (object == null) {
            throw new NullPointerException("register should not be null ");
        }
        mRegisterList.add(object);
    }

    public void unregister(Object object) {
        if (object == null) {
            throw new NullPointerException("unregister should not be null ");
        }
        mRegisterList.remove(object);
    }

}
