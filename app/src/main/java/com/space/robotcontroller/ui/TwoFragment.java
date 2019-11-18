package com.space.robotcontroller.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.space.robotcontroller.R;
import com.space.robotcontroller.api.UserRequest;
import com.space.robotcontroller.base.Config;
import com.space.robotcontroller.dataBean.Devi;
import com.space.robotcontroller.dataBean.Status;
import com.space.robotcontroller.utils.RetrofitUtil;
import com.space.robotcontroller.utils.SendDataOrder;
import com.space.robotcontroller.task.WorkTask;
import com.space.robotcontroller.utils.XMLUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.InputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by licht on 2019/9/19.
 */

public class TwoFragment extends Fragment implements View.OnClickListener {
    private ImageView mSwitchControl;
    private boolean isOpen = false;
    private int mState;
    private ImageView mIvIcon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fly_hardware, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        initView(view);
        initData();
        initListener();
    }


    private void initView(View view) {
        mIvIcon = view.findViewById(R.id.iv_icon);
        mSwitchControl = view.findViewById(R.id.switch_control);
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }

        mState = bundle.getInt(Config.TWO_STATE);
        boolean bState = bundle.getBoolean(Config.IS_SOFT);
        if (bState) {
            mIvIcon.setVisibility(View.INVISIBLE);
            mSwitchControl.setVisibility(View.INVISIBLE);
        } else {
            mIvIcon.setVisibility(View.VISIBLE);
            mSwitchControl.setVisibility(View.VISIBLE);
        }

        Retrofit instance = RetrofitUtil.getInstance();
        UserRequest userRequest = instance.create(UserRequest.class);
        Call<ResponseBody> call = null;
        if (mState == 1) {
            call = userRequest.getHardwareStatus(Config.GET_STATUS, "1,0,1");
        } else {
            call = userRequest.getHardwareStatus(Config.GET_STATUS, "1,0,2");
        }
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    parseResp(response);
                }
                Log.e("=========", "onResponse: ");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("=========", "onFailure: ");
            }
        });
    }

    private void parseResp(Response<ResponseBody> response) {
        InputStream inputStream = response.body().byteStream();
        Status status = XMLUtil.parseXml(inputStream, Status.class);
        List<Devi> list = status.getZone().getList();
        if (list.get(0).getStatus().equals("on")) {
            isOpen = true;
            mSwitchControl.setImageResource(R.mipmap.switch_on);
        } else {
            isOpen = false;
            mSwitchControl.setImageResource(R.mipmap.switch_off);
        }
    }

    private void initListener() {
        mSwitchControl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_control:
                isOpen = !isOpen;
                if (isOpen) {
                    mSwitchControl.setImageResource(R.mipmap.switch_on);
                } else {
                    mSwitchControl.setImageResource(R.mipmap.switch_off);
                }
                if (isOpen) {
                    if (mState == 1) {
                        HomeActivity.mExecutor.execute(new WorkTask(SendDataOrder.appendArgs(Config.FUNCTION_SCREEN, "1,0,1,on")));
                    } else {
                        HomeActivity.mExecutor.execute(new WorkTask(SendDataOrder.appendArgs(Config.FUNCTION_SCREEN, "1,0,2,on")));
                    }
                } else {
                    if (mState == 1) {
                        HomeActivity.mExecutor.execute(new WorkTask(SendDataOrder.appendArgs(Config.FUNCTION_SCREEN, "1,0,1,off")));
                    } else {
                        HomeActivity.mExecutor.execute(new WorkTask(SendDataOrder.appendArgs(Config.FUNCTION_SCREEN, "1,0,2,off")));
                    }
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String event) {
        if ("soft".equals(event)) {
            mIvIcon.setVisibility(View.INVISIBLE);
            mSwitchControl.setVisibility(View.INVISIBLE);
        }
        if ("hardware".equals(event)) {
            mIvIcon.setVisibility(View.VISIBLE);
            mSwitchControl.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
