package com.lipnus.kum_weather;

import java.util.List;

/**
 * Created by Sunpil on 2017-03-23.
 */

public class Weather {

    List<W_weather> weather = null;
    W_main main = null;

    public Weather(List<W_weather> weather, W_main main) {
        this.weather = weather;
        this.main = main;
    }

    public static class W_weather{
        String main = "Clear";
        String description = "clear sky";
        String icon = null;
    }

    public static class W_main{
        double temp = 10;
        double presure = 1000;
        int humidity = 0;
        double temp_min = 10;
        double temp_max = 10;
    }

}
