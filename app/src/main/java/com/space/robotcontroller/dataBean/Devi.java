package com.space.robotcontroller.dataBean;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by licht on 2019/11/6.
 */
@Root
public class Devi {

    @Attribute
    private String name;
    @Attribute
    private String id;
    @Attribute
    private String status;
    @Attribute
    private String node;
    @Attribute
    private String zone;
    @Attribute
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
