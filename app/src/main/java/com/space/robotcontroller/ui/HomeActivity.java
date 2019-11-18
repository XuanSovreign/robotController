package com.space.robotcontroller.ui;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.space.robotcontroller.R;
import com.space.robotcontroller.api.UserRequest;
import com.space.robotcontroller.base.Config;
import com.space.robotcontroller.dataBean.Configuration;
import com.space.robotcontroller.utils.RetrofitUtil;
import com.space.robotcontroller.utils.SendDataOrder;
import com.space.robotcontroller.task.WorkTask;
import com.space.robotcontroller.utils.SoftTcpUtil;
import com.space.robotcontroller.utils.TcpUtil;
import com.space.robotcontroller.utils.XMLUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by licht on 2019/9/18.
 */

public class HomeActivity extends FragmentActivity implements View.OnClickListener {

    private ImageView mRbOpen;
    private ImageView mRbClose;
    private RadioGroup mRgMain;
    private LinearLayout mFlMain;
    private OneFragment mFlyFragment;
    private OneFragment mImmerseFragment;
    private TwoFragment mMeetFragment;
    private TwoFragment mOfficeFragment;
    private RelativeLayout mRvHome;
    private SharedPreferences mPreferences;
    public static ExecutorService mExecutor;
    public static String mIpAddress;
    private ImageView mIvHome;
    private RadioButton mRbSoft;
    private RadioButton mRbHardware;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        EventBus.getDefault().register(this);
        mRvHome = findViewById(R.id.rv_home);
        mRbOpen = findViewById(R.id.rb_open);
        mRbClose = findViewById(R.id.rb_close);
        mFlMain = findViewById(R.id.ll_home);
        mRgMain = findViewById(R.id.rg_main);
        mIvHome = findViewById(R.id.iv_home);
        mRbSoft = findViewById(R.id.rb_soft);
        mRbHardware = findViewById(R.id.rb_hardware);
//        mFlMain.setVisibility(View.INVISIBLE);
        initData();
        initListener();
    }

    private void initData() {
        mRbSoft.setChecked(true);
        mPreferences = getSharedPreferences("robot_controller_ip", MODE_PRIVATE);
        mIpAddress = mPreferences.getString("ip_address", "");
        final int ipPort = mPreferences.getInt("ip_port", 2000);
        if (mExecutor == null) {
            mExecutor = Executors.newSingleThreadExecutor();
        }
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                TcpUtil.getSocketInstance(mIpAddress, ipPort);
            }
        });
        Retrofit instance = RetrofitUtil.getInstance();
        UserRequest userRequest = instance.create(UserRequest.class);
        Call<ResponseBody> configuration = userRequest.getConfiguration(Config.GET_CONTROL, "1,1,cfg");
        configuration.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    final Configuration xml = XMLUtil.parseXml(inputStream, Configuration.class);
                    mExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            SoftTcpUtil.getSocketInstance(xml.getIp(), Integer.parseInt(xml.getPort()));
                        }
                    });
                    Log.e("HomeActivity", "onResponse: ip=" + xml.getIp() + "port=" + xml.getPort());
                }
                Log.e("========", "onResponse: ");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("========", "onFailure: " + t.getMessage());
            }
        });
    }


    private void initListener() {
        mRbOpen.setOnClickListener(this);
        mRbClose.setOnClickListener(this);
        mIvHome.setOnClickListener(this);
        mRbSoft.setOnClickListener(this);
        mRbHardware.setOnClickListener(this);
        mRgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mFlMain.getVisibility() == View.INVISIBLE) {
                    mFlMain.setVisibility(View.VISIBLE);
                    mRvHome.setVisibility(View.INVISIBLE);
                }
                switch (checkedId) {
                    case R.id.rb_fly:
                        if (mFlyFragment == null) {
                            mFlyFragment = new OneFragment();
                        }
                        Bundle bundle = new Bundle();
                        bundle.putInt(Config.ONE_STATE, 1);
                        bundle.putBoolean(Config.IS_SOFT, mRbSoft.isChecked());
                        mFlyFragment.setArguments(bundle);
                        showFragment(mFlyFragment);
                        break;
                    case R.id.rb_immerse:
                        if (mImmerseFragment == null) {
                            mImmerseFragment = new OneFragment();
                        }
                        Bundle bundle2 = new Bundle();
                        bundle2.putInt(Config.ONE_STATE, 2);
                        bundle2.putBoolean(Config.IS_SOFT, mRbSoft.isChecked());
                        mImmerseFragment.setArguments(bundle2);
                        showFragment(mImmerseFragment);

                        break;
                    case R.id.rb_meet:
                        if (mMeetFragment == null) {
                            mMeetFragment = new TwoFragment();
                        }
                        Bundle bundle3 = new Bundle();
                        bundle3.putInt(Config.TWO_STATE, 1);
                        bundle3.putBoolean(Config.IS_SOFT, mRbSoft.isChecked());
                        mMeetFragment.setArguments(bundle3);
                        showFragment(mMeetFragment);
                        break;
                    case R.id.rb_office:
                        if (mOfficeFragment == null) {
                            mOfficeFragment = new TwoFragment();
                        }
                        Bundle bundle4 = new Bundle();
                        bundle4.putInt(Config.TWO_STATE, 2);
                        bundle4.putBoolean(Config.IS_SOFT, mRbSoft.isChecked());
                        mOfficeFragment.setArguments(bundle4);
                        showFragment(mOfficeFragment);
                        break;
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_close:
                showDialog(false);
                break;
            case R.id.rb_open:
                showDialog(true);
                break;
            case R.id.iv_home:
                restorePage();
                break;
            case R.id.rb_soft:
                mRbHardware.setChecked(false);
                EventBus.getDefault().post("soft");
                break;
            case R.id.rb_hardware:
                mRbSoft.setChecked(false);
                EventBus.getDefault().post("hardware");
                break;
        }
    }

    private void restorePage() {
        mRgMain.clearCheck();
        mRvHome.setVisibility(View.VISIBLE);
        mFlMain.setVisibility(View.INVISIBLE);
    }


    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_main, fragment);
        transaction.commitAllowingStateLoss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String event) {
        if ("home".equals(event)) {
            restorePage();
        }
    }

    @Override
    protected void onStop() {
        if (isFinishing()) {
            TcpUtil.closeMySocket();
            SoftTcpUtil.closeMySocket();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void showDialog(final boolean isOpen) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_account, null);
        builder.setView(view);
//        builder.setCancelable(false);
        final AlertDialog show = builder.show();
        final EditText edtUsername = view.findViewById(R.id.edt_username);
        final EditText edtPassword = view.findViewById(R.id.edt_password);
        Button btnLogin = view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                    Toast.makeText(HomeActivity.this, "请输入账号或者密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!"admin".equals(userName)) {
                    Toast.makeText(HomeActivity.this, "账号不存在", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!"admin123".equals(password)) {
                    Toast.makeText(HomeActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isOpen) {
                    mExecutor.execute(new WorkTask(SendDataOrder.appendArgs(Config.FUNCTION_ALL, "0,0,0,on")));
                } else {
                    mExecutor.execute(new WorkTask(SendDataOrder.appendArgs(Config.FUNCTION_ALL, "0,0,0,off")));
                }
                show.dismiss();
            }
        });
    }
}
