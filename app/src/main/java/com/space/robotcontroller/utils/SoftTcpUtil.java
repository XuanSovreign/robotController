package com.space.robotcontroller.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by licht on 2019/10/30.
 */

public class SoftTcpUtil {
    private static Socket sSocket = null;
    private static String sIp;
    private static int sPort;
    private static byte[] sBuffer;
    private static OutputStream mOutputStream;

    public static Socket getSocketInstance(String ip, int port) {
        if (sSocket == null) {
            synchronized (SoftTcpUtil.class) {
                if (sSocket == null) {
                    try {
                        sSocket = new Socket(ip, port);
                        sIp=ip;
                        sPort=port;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return sSocket;
    }

    public static void sendDataOrder(byte[] buffer) {
        if (sSocket == null) {
            getSocketInstance(sIp, sPort);
        }
        sBuffer=buffer;
        try {
            mOutputStream = sSocket.getOutputStream();
            if (mOutputStream != null) {
                mOutputStream.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
            closeMySocket();
            sendDataOrder(sBuffer);
        }
    }

    public static void closeMySocket() {

        try {
            if (mOutputStream != null) {
                mOutputStream.close();
                mOutputStream=null;
            }
            if (sSocket != null) {
                sSocket.close();
                sSocket=null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
