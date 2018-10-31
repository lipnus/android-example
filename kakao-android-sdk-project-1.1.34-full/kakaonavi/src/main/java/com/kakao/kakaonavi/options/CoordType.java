package com.kakao.kakaonavi.options;

/** 좌표 타입을 나타내는 enum class.
 * @author kevin.kang
 * Created by kevin.kang on 2016. 8. 30..
 */
public enum CoordType {
    WGS84("wgs84"),
    @SuppressWarnings("unused")
    KATEC("katec");

    private String type;

    CoordType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}