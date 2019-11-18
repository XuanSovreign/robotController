package com.space.robotcontroller.base;

/**
 * Created by licht on 2019/9/20.
 */

public interface Config {
    String ONE_STATE="ONE_STATE";
    String TWO_STATE="TWO_STATE";
    String IS_SOFT="is_soft";

    /**
     * 硬件控制
     */
    String FUNCTION_ALL="allDevice";
    String FUNCTION_LIGHT="light";
    String FUNCTION_MACHINE="machine";
    String FUNCTION_PROJECTL="project";
    String FUNCTION_SCREEN="screen";

    /**
     * 软件控制
     */
    String VIDEO_PREV="videoPrev";
    String VIDEO_NEXT="videoNext";
    String VIDEO_PLAY="videoPlay";
    String VIDEO_PAUSE="videoPause";
    String VOLUME_DOWN="volumeDown";
    String VOLUME_UP="volumeUp";
    String VIDEO_SEEK="videoSeek";
    String VOLUME="volume";


    /**
     * http请求参数
     */
    String GET_CONTROL="getControl";
    String GET_STATUS="getStatus";
}
