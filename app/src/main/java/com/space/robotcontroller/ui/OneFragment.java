package com.space.robotcontroller.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.space.robotcontroller.R;
import com.space.robotcontroller.api.UserRequest;
import com.space.robotcontroller.base.Config;
import com.space.robotcontroller.dataBean.Devi;
import com.space.robotcontroller.dataBean.Status;
import com.space.robotcontroller.utils.RetrofitUtil;
import com.space.robotcontroller.utils.SendDataOrder;
import com.space.robotcontroller.task.SoftWorkTask;
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

public class OneFragment extends Fragment implements View.OnClickListener {
    private ImageView mIvflyTitle;
    private ImageView mIvflyUp;
    private ImageView mIvflyBack;
    private ImageView mIvflyPlay;
    private ImageView mIvflyStop;
    private ImageView mIvflyDown;
    private CheckBox mCbVoice;
    private SeekBar mSeekbar;

    private ImageView mSwitchMain;
    private ImageView mMSwitchProjector;
    private ImageView mSwitchLight;
    private ImageView mSwitchLeep;
    private LinearLayout mLlOne;
    private LinearLayout mLlTwo;
    private ImageView mIvSwitch;
    private int mState;
    private boolean isOpen = false;
    private boolean isOpen2 = false;
    private boolean isOpen3 = false;
    private boolean isOpen4 = false;
    private boolean isOpen5 = false;
    private ImageView mIvUp;
    private ImageView mIvDown;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fly_soft, null);
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
        mLlOne = view.findViewById(R.id.ll_one);
        mIvflyTitle = view.findViewById(R.id.iv_fly_title);
        mIvflyUp = view.findViewById(R.id.iv_up);
        mIvflyBack = view.findViewById(R.id.iv_back);
        mIvflyPlay = view.findViewById(R.id.iv_play);
        mIvflyStop = view.findViewById(R.id.iv_stop);
        mIvflyDown = view.findViewById(R.id.iv_down);
        mCbVoice = view.findViewById(R.id.cb_voice);
        mSeekbar = view.findViewById(R.id.seekbar_voice);
        mIvUp = view.findViewById(R.id.iv_voice_up);
        mIvDown = view.findViewById(R.id.iv_voice_down);

        mLlTwo = view.findViewById(R.id.ll_two);
        mIvSwitch = view.findViewById(R.id.switch_fly_control);


        //沉浸的硬件控制
        mSwitchMain = view.findViewById(R.id.switch_main_machine);
        mMSwitchProjector = view.findViewById(R.id.switch_projector);
        mSwitchLight = view.findViewById(R.id.switch_light);
        mSwitchLeep = view.findViewById(R.id.switch_leep);
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        mState = bundle.getInt(Config.ONE_STATE);
        if (mState == 1) {
            mIvflyTitle.setImageResource(R.mipmap.iv_fly_title);
        } else {
            mIvflyTitle.setImageResource(R.mipmap.text_immerse);
        }
        boolean switchState = bundle.getBoolean(Config.IS_SOFT);
        if (switchState) {
            if (mState == 1) {
                mLlOne.setVisibility(View.VISIBLE);
            } else {
                mLlOne.setVisibility(View.VISIBLE);
                mLlTwo.setVisibility(View.VISIBLE);
            }
        } else {
            if (mState == 1) {
                mLlOne.setVisibility(View.GONE);
            } else {
                mLlOne.setVisibility(View.GONE);
                mLlTwo.setVisibility(View.GONE);
            }
        }

        Retrofit instance = RetrofitUtil.getInstance();
        UserRequest userRequest = instance.create(UserRequest.class);
        Call<ResponseBody> call = userRequest.getHardwareStatus(Config.GET_STATUS, "0,0,0");
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
        int mNum=0,pNum=0,lNum=0;
        for (int i = 0; i < list.size(); i++) {
            String type = list.get(i).getType();
            Devi devi = list.get(i);
            if ("uno".equals(devi.getStatus()) || "uno".equals(devi.getStatus())) {
                if ("1".equals(type)) {
                   mNum++;
                }
                if ("2".equals(type)) {
                    lNum++;
                }
                if ("4".equals(type)) {
                    pNum++;
                }
            }
        }

        if (mNum <= 0) {
            isOpen2=true;
            switchState(isOpen2, mSwitchMain);
        }

        if (lNum <= 0) {
            isOpen4=true;
            switchState(isOpen4,mSwitchLight);
        }
        if (pNum <= 0) {
            isOpen3=true;
            switchState(isOpen3,mMSwitchProjector);
        }
    }

    private void initListener() {
        mIvflyUp.setOnClickListener(this);
        mIvflyBack.setOnClickListener(this);
        mIvflyPlay.setOnClickListener(this);
        mIvflyStop.setOnClickListener(this);
        mIvflyDown.setOnClickListener(this);
        mIvSwitch.setOnClickListener(this);
        mSwitchMain.setOnClickListener(this);
        mMSwitchProjector.setOnClickListener(this);
        mSwitchLight.setOnClickListener(this);
        mSwitchLeep.setOnClickListener(this);
        mIvUp.setOnClickListener(this);
        mIvDown.setOnClickListener(this);
        mCbVoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HomeActivity.mExecutor.execute(new SoftWorkTask(SendDataOrder.appendArgs(Config.VOLUME, "0")));
                } else {
                    HomeActivity.mExecutor.execute(new SoftWorkTask(SendDataOrder.appendArgs(Config.VOLUME, "70")));
                }
            }
        });

        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_up:
                HomeActivity.mExecutor.execute(new SoftWorkTask(SendDataOrder.appendArgs(Config.VIDEO_PREV)));
                break;
            case R.id.iv_back:
                HomeActivity.mExecutor.execute(new SoftWorkTask(SendDataOrder.appendArgs(Config.VIDEO_PREV, "0")));
                break;
            case R.id.iv_play:
                HomeActivity.mExecutor.execute(new SoftWorkTask(SendDataOrder.appendArgs(Config.VIDEO_PLAY)));
                break;
            case R.id.iv_stop:
                HomeActivity.mExecutor.execute(new SoftWorkTask(SendDataOrder.appendArgs(Config.VIDEO_PAUSE)));
                break;
            case R.id.iv_down:
                HomeActivity.mExecutor.execute(new SoftWorkTask(SendDataOrder.appendArgs(Config.VIDEO_NEXT)));
                break;
            case R.id.iv_voice_up:
                HomeActivity.mExecutor.execute(new SoftWorkTask(SendDataOrder.appendArgs(Config.VOLUME_UP)));
                break;
            case R.id.iv_voice_down:
                HomeActivity.mExecutor.execute(new SoftWorkTask(SendDataOrder.appendArgs(Config.VOLUME_DOWN)));
                break;
            case R.id.switch_fly_control:
                isOpen = !isOpen;
                if (isOpen) {
                    mIvSwitch.setImageResource(R.mipmap.switch_open);
//                    HomeActivity.mExecutor.execute(new WorkTask(SendDataOrder.appendArgs(Config.FUNCTION_ALL,"0,0,0,on")));
                } else {
                    mIvSwitch.setImageResource(R.mipmap.switch_close);
//                    HomeActivity.mExecutor.execute(new WorkTask(SendDataOrder.appendArgs(Config.FUNCTION_ALL,"0,0,0,on")));
                }
                break;
            case R.id.switch_main_machine:
                isOpen2 = !isOpen2;
                switchState(isOpen2, mSwitchMain);
                if (isOpen2) {
                    HomeActivity.mExecutor.execute(new WorkTask(SendDataOrder.appendArgs(Config.FUNCTION_MACHINE, "1,0,0,on")));
                } else {
                    HomeActivity.mExecutor.execute(new WorkTask(SendDataOrder.appendArgs(Config.FUNCTION_MACHINE, "1,0,0,off")));
                }

                break;
            case R.id.switch_projector:
                isOpen3 = !isOpen3;
                switchState(isOpen3, mMSwitchProjector);
                if (isOpen3) {
                    HomeActivity.mExecutor.execute(new WorkTask(SendDataOrder.appendArgs(Config.FUNCTION_PROJECTL, "1,0,0,on")));
                } else {
                    HomeActivity.mExecutor.execute(new WorkTask(SendDataOrder.appendArgs(Config.FUNCTION_PROJECTL, "1,0,0,off")));
                }
                break;
            case R.id.switch_light:
                isOpen4 = !isOpen4;
                switchState(isOpen4, mSwitchLight);
                if (isOpen4) {
                    HomeActivity.mExecutor.execute(new WorkTask(SendDataOrder.appendArgs(Config.FUNCTION_LIGHT, "1,0,0,on")));
                } else {
                    HomeActivity.mExecutor.execute(new WorkTask(SendDataOrder.appendArgs(Config.FUNCTION_LIGHT, "1,0,0,off")));
                }
                break;
            case R.id.switch_leep:
                isOpen5 = !isOpen5;
                switchState(isOpen5, mSwitchLeep);
                if (isOpen5) {

                } else {

                }
                break;

        }
    }

    private void switchState(boolean state, ImageView iv) {
        if (state) {
            iv.setImageResource(R.mipmap.switch_on);
        } else {
            iv.setImageResource(R.mipmap.switch_off);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String event) {
        if ("soft".equals(event)) {
            if (mState == 1) {
                mLlOne.setVisibility(View.VISIBLE);
            } else {
                mLlOne.setVisibility(View.VISIBLE);
                mLlTwo.setVisibility(View.VISIBLE);
            }
        }
        if ("hardware".equals(event)) {
            if (mState == 1) {
                mLlOne.setVisibility(View.GONE);
            } else {
                mLlOne.setVisibility(View.GONE);
                mLlTwo.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
