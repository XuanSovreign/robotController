package com.space.robotcontroller.task;

import com.space.robotcontroller.utils.SendDataOrder;
import com.space.robotcontroller.utils.TcpUtil;

/**
 * Created by licht on 2019/11/1.
 */

public class WorkTask implements Runnable {
    private String cmd;

    public WorkTask(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public void run() {
        TcpUtil.sendDataOrder(SendDataOrder.enCloseData(cmd.getBytes()));
    }
}
