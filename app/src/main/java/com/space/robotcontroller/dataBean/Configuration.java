package com.space.robotcontroller.dataBean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by licht on 2019/11/7.
 */
@Root
public class Configuration {
    @Element
    private String ip;
    @Element
    private String  port;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
