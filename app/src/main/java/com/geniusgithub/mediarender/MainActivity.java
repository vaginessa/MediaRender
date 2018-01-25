package com.geniusgithub.mediarender;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.geniusgithub.mediarender.center.MediaRenderProxy;
import com.geniusgithub.mediarender.util.CommonLog;
import com.geniusgithub.mediarender.util.DlnaUtils;
import com.geniusgithub.mediarender.util.LogFactory;


/**
 * @author lance
 * @csdn http://blog.csdn.net/geniuseoe2012
 * @github https://github.com/geniusgithub
 */
public class MainActivity extends BaseActivity implements OnClickListener, DeviceUpdateBrocastFactory.IDevUpdateListener {

    private static final CommonLog log = LogFactory.createLog();

    private Button mBtnStart;
    private Button mBtnRestart;
    private Button mBtnStop;

    private Button mBtnEditName;
    private EditText mETName;
    private TextView mTVDevInfo;


    private MediaRenderProxy mRenderProxy;
    private RenderApplication mApplication;
    private DeviceUpdateBrocastFactory mBrocastFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupView();
        initData();
    }


    @Override
    protected void onDestroy() {
        unInitData();
        super.onDestroy();
    }


    private void setupView() {
        mBtnStart = (Button) findViewById(R.id.btn_start);
        mBtnRestart = (Button) findViewById(R.id.btn_restart);
        mBtnStop = (Button) findViewById(R.id.btn_stop);
        mBtnEditName = (Button) findViewById(R.id.bt_dev_name);
        mBtnStart.setOnClickListener(this);
        mBtnRestart.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);
        mBtnEditName.setOnClickListener(this);

        mTVDevInfo = (TextView) findViewById(R.id.tv_dev_info);
        mETName = (EditText) findViewById(R.id.et_dev_name);

        mBtnStart.setEnabled(true);
        mBtnRestart.setEnabled(false);
        mBtnStop.setEnabled(false);
    }

    private void initData() {
        mApplication = RenderApplication.getInstance();
        mRenderProxy = MediaRenderProxy.getInstance();
        mBrocastFactory = new DeviceUpdateBrocastFactory(this);

        String dev_name = DlnaUtils.getDevName(this);
        mETName.setText(dev_name);
        mETName.setEnabled(false);

        updateDevInfo(mApplication.getDevInfo());
        mBrocastFactory.register(this);
    }

    private void unInitData() {
        mBrocastFactory.unregister();
    }

    private void updateDevInfo(DeviceInfo object) {
        String status = object.status ? getString(R.string.status_open) : getString(R.string.status_close);
        String text = getString(R.string.status) + status + "\n" +
                getString(R.string.dev_name) + object.dev_name + "\n" +
                getString(R.string.uuid) + object.uuid;

        mTVDevInfo.setText(text);

        if(object.status) {
            mBtnStart.setEnabled(false);
            mBtnRestart.setEnabled(true);
            mBtnStop.setEnabled(true);
        } else {
            mBtnStart.setEnabled(true);
            mBtnRestart.setEnabled(false);
            mBtnStop.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                start();
                break;
            case R.id.btn_restart:
                reset();
                break;
            case R.id.btn_stop:
                stop();
                break;
            case R.id.bt_dev_name:
                change();
                break;
        }
    }

    private void start() {
        mRenderProxy.startEngine();
    }

    private void reset() {
        mRenderProxy.restartEngine();
    }

    private void stop() {
        mRenderProxy.stopEngine();
    }

    private void change() {
        if (mETName.isEnabled()) {
            mETName.setEnabled(false);
            DlnaUtils.setDevName(this, mETName.getText().toString());
        } else {
            mETName.setEnabled(true);
        }
    }


    @Override
    public void onUpdate() {
        updateDevInfo(mApplication.getDevInfo());
    }

}
