package com.kakao.kakaonavi.options;

/** 경로 옵션을 나타내는 enum class.
 * @author kevin.kang
 * Created by kevin.kang on 2016. 8. 30..
 */
public enum RpOption {
    FAST(1), // 빠른길
    FREE(2), // 무료도로
    SHORTEST(3), // 최단거리
    NO_AUTO(4), // 자동차전용제외
    WIDE(5), // 큰길우선
    HIGHWAY(6), // 고속도로우선
    NORMAL(8); // 일반도로우선

    private int option;

    RpOption(int option) {
        this.option = option;
    }

    public int getOption() {
        return this.option;
    }
}