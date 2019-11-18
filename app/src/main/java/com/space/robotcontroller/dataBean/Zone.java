package com.space.robotcontroller.dataBean;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by licht on 2019/11/6.
 */
@Root
public class Zone {
    @Attribute
    private String id;
    @Attribute
    private String name;
    @ElementList(inline = true)
    private List<Devi> list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Devi> getList() {
        return list;
    }

    public void setList(List<Devi> list) {
        this.list = list;
    }
}
