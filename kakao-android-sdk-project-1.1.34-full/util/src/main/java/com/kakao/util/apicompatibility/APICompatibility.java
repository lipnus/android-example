package com.kakao.util.apicompatibility;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.kakao.util.helper.log.Logger;

/**
 * @author leoshin on 15. 11. 4.
 */
public abstract class APICompatibility {
    static final String COOKIE_SEPERATOR = ";";
    static final String COOKIE_NAME_VALUE_DELIMITER = "=";

    private static volatile APICompatibility instance;

    public final static APICompatibility getInstance() {
        if (instance == null) {
            synchronized (APICompatibility.class) {
                if (instance == null) {
                    String className;
                    int sdkVersion = Build.VERSION.SDK_INT;

                    if (sdkVersion > Build.VERSION_CODES.KITKAT_WATCH) {
                        className = "APILevel21Compatibility";
                    } else if (sdkVersion > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        className = "APILevel19Compatibility";
                    } else {
                        className = "APILevel9Compatibility";
                    }

                    Class<? extends APICompatibility> clazz;
                    try {
                        clazz = Class.forName(APICompatibility.class.getPackage().getName() + "." + className).asSubclass(APICompatibility.class);
                        instance = clazz.newInstance();
                    } catch (ClassNotFoundException e) {
                        Logger.e(e);
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        Logger.e(e);
                        throw new RuntimeException(e);
                    } catch (InstantiationException e) {
                        Logger.e(e);
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        return instance;
    }

    public abstract void removeCookie(Context context, String domain);

    public abstract void removeCookie(Context context, String domain, String name);

    public abstract String getSmsMessage(Intent intent);
}
