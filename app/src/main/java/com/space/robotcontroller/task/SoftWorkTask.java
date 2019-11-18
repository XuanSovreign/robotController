package com.space.robotcontroller.task;

import com.space.robotcontroller.utils.SendDataOrder;
import com.space.robotcontroller.utils.SoftTcpUtil;

/**
 * Created by licht on 2019/11/1.
 */

public class SoftWorkTask implements Runnable {
    private String cmd;

    public SoftWorkTask(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public void run() {
        SoftTcpUtil.sendDataOrder(SendDataOrder.enCloseData(cmd.getBytes()));
    }
}
