package com.space.robotcontroller.dataBean;

import com.space.robotcontroller.dataBean.Zone;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by licht on 2019/10/29.
 */
@Root
public class Status {

    @Element
    private Zone zone;

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }
}
