package com.space.robotcontroller.utils;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.InputStream;

/**
 * Created by licht on 2019/11/7.
 */

public class XMLUtil {
    public static <T> T parseXml(InputStream src, Class<T> type) {
        Serializer serializer = new Persister();
        T instance=null;
        try {
            instance = serializer.read(type, src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }
}
