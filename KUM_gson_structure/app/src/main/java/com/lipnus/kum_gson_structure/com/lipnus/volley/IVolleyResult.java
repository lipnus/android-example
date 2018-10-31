package com.lipnus.kum_gson_structure.com.lipnus.volley;

import com.android.volley.VolleyError;

/**
 * Created by Sunpil on 2017-02-26.
 */

//volley의 request
public interface IVolleyResult {
    public void notifySuccess(String response);
    public void notifyError(VolleyError error);
}
