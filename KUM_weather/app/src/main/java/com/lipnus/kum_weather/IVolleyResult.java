package com.lipnus.kum_weather;

import com.android.volley.VolleyError;

/**
 * Created by Sunpil on 2017-02-26.
 */

//volley의 request
public interface IVolleyResult {
    void notifySuccess(String response);
    void notifyError(VolleyError error);
}
