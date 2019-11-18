package com.space.robotcontroller.utils;

import android.util.Log;

/**
 * Created by licht on 2019/10/31.
 */

public class SendDataOrder {
    public static String appendArgs(String func, String args) {
        StringBuilder builder = new StringBuilder();
        builder.append("demoName=skcgcc");
        builder.append("&func=");
        builder.append(func);
        builder.append("&args=");
        builder.append(args);
        return builder.toString();
    }

    public static String appendArgs(String func) {
        StringBuilder builder = new StringBuilder();
        builder.append("demoName=skcgcc");
        builder.append("&func=");
        builder.append(func);
        return builder.toString();
    }

    public static byte[] enCloseData(byte[] data) {
        int length = data.length;
        byte lenByte1 = (byte) ((length >> 8) & 0xff);
        byte lenByte2 = (byte) (length & 0xff);
        byte[] buffer = new byte[data.length + 7];
        buffer[0] = 0x21;
        buffer[1] = 0x02;
        buffer[2] = lenByte1;
        buffer[3] = lenByte2;
        buffer[4] = 0;
        for (int i = 0; i < data.length; i++) {
            buffer[5 + i] = data[i];
        }
        buffer[data.length + 7 - 2] = 0x0D;
        buffer[data.length + 7 - 1] = 0x0A;
        Log.e("SendDataOrder", "enCloseData: length=" + buffer.length + "\ncontent==" + buffer.toString());
        return buffer;
    }
}
