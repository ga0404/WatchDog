package com.dodoiot.lockapp.controller.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.dodoiot.lockapp.BleSppGattAttributes;
import com.dodoiot.lockapp.R;
import com.dodoiot.lockapp.base.BaseConfig;
import com.dodoiot.lockapp.base.CommonUrl;
import com.dodoiot.lockapp.base.Gloal;
import com.dodoiot.lockapp.model.BaseBean;
import com.dodoiot.lockapp.model.DeviceBean;
import com.dodoiot.lockapp.net.IResponseParser;
import com.dodoiot.lockapp.net.RequestManager;
import com.dodoiot.lockapp.util.BleUtils;
import com.dodoiot.lockapp.util.JsonUtils;
import com.dodoiot.lockapp.util.TShow;
import com.dodoiot.lockapp.view.ProgressDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.javac.ManyBlue.ManyBlue;
import io.javac.ManyBlue.bean.CharacteristicValues;
import io.javac.ManyBlue.bean.UUIDMessage;
import io.javac.ManyBlue.interfaces.BaseNotifyListener;
import io.javac.ManyBlue.manager.BluetoothGattManager;

public class LockDetailsActivity extends BaseActivity implements BaseNotifyListener.DeviceListener, BaseNotifyListener.DeviceDataListener {


    @InjectView(R.id.btnleft)
    Button btnleft;
    @InjectView(R.id.btnopertaion)
    Button btnopertaion;
    @InjectView(R.id.leftlayout)
    LinearLayout leftlayout;
    @InjectView(R.id.btnright)
    Button btnright;
    @InjectView(R.id.btnRight)
    TextView btnRight;
    @InjectView(R.id.rightlayout)
    LinearLayout rightlayout;
    @InjectView(R.id.tvtitle)
    TextView tvtitle;
    @InjectView(R.id.titlelayout)
    RelativeLayout titlelayout;
    @InjectView(R.id.bluetooth)
    TextView bluetooth;
    @InjectView(R.id.power)
    TextView power;
    @InjectView(R.id.opendoor)
    TextView opendoor;
    @InjectView(R.id.sendpwd)
    TextView sendpwd;
    @InjectView(R.id.shouquan)
    TextView shouquan;
    @InjectView(R.id.managecard)
    TextView managecard;
    @InjectView(R.id.managepwd)
    TextView managepwd;
    @InjectView(R.id.shouquanrz)
    TextView shouquanrz;
    @InjectView(R.id.operationrz)
    TextView operationrz;
    @InjectView(R.id.seehousepwd)
    TextView seehousepwd;
    @InjectView(R.id.lockset)
    TextView lockset;

    DeviceBean bean;
    String branchMsg;
    @InjectView(R.id.linearlayout1)
    LinearLayout linearlayout1;

    @InjectView(R.id.authloglayout)
    RelativeLayout authloglayout;

    @InjectView(R.id.managepwdlayout)
    RelativeLayout managepwdlayout;

    private BluetoothAdapter mBluetoothAdapter;
    private boolean isAuth;

    @Override
    protected void initLayoutId() {
        isFull = true;
        layoutId = R.layout.activity_lock_details;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void initView() {
        mTipDlg = new ProgressDialog(LockDetailsActivity.this);
        mTipDlg.setCancelable(false);
        branchMsg = getIntent().getStringExtra("msg");
        bean = (DeviceBean) getIntent().getSerializableExtra("bean");
        tvtitle.setText(bean.getName());
        power.setText(getResources().getString(R.string.power) + 100 + "%");

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            //finish();
            return;
        }

        mayRequestLocation();
        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(this, R.string.ble_close, Toast.LENGTH_SHORT).show();
            //finish();
            return;
        } else {
            Drawable drawable = getResources().getDrawable(R.mipmap
                    .ble2);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            bluetooth.setCompoundDrawables(null, drawable, null, null);
        }
        isAuth = getIntent().getBooleanExtra("auth", false);
        if (isAuth) {
            linearlayout1.setVisibility(View.GONE);
            authloglayout.setVisibility(View.GONE);
            managepwdlayout.setVisibility(View.GONE);
        }

    }

    boolean isTop = false;
    boolean isDel = false;
    boolean isPower = false;

    @Override
    protected void onResume() {
        super.onResume();
        isTop = true;

    }

    private void getPower() {
        mTipDlg.show();
        if (BluetoothGattManager.gattMap.size() >= 6) {
            isOpen = true;
            ManyBlue.blueDisconnectedDeviceAll();
        }
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isPower = true;
        type = 1;
        if (BluetoothGattManager.gattMap.containsKey(bean.getSn())) {
            ManyBlue.blueWriteDataByteArray(BleUtils.getPower(), bean.getSn());
        } else {
            ManyBlue.blueConnectDevice(bean.getSn(), bean.getSn());//连接该设备  并且以该设备的mac地址作为标识
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        isTop = false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bean != null)
            ManyBlue.blueDisconnectedDevice(bean.getSn());
        isTop = false;
    }

    private void setDefault() {
        if (mTipDlg != null)
            mTipDlg.dismiss();
    }

    boolean isOpen = false;
    int type = 0;

    private void open() {
        mTipDlg.show();
        if (BluetoothGattManager.gattMap.size() >= 6) {
            isOpen = true;
            ManyBlue.blueDisconnectedDeviceAll();
        }
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isOpen = false;
        type = 0;
        if (BluetoothGattManager.gattMap.containsKey(bean.getSn())) {
            ManyBlue.blueWriteDataByteArray(BleUtils.setConnectPwdByte(bean.getCommKey()), bean.getSn());
        } else {
            ManyBlue.blueConnectDevice(bean.getSn(), bean.getSn());//连接该设备  并且以该设备的mac地址作为标识
        }
        // listBean.add(bean);
    }

    private void displayData(byte[] buf) {
        Log.e("dfc", "<--buflength--->" + BleUtils.bytesToString(buf));
        if (buf[5] == (byte) 0x10) {
            if (buf[6] == (byte) 0x55) {//提交记录
                addLog();
                setDefault();
                Log.e("dfc", "<------------dispalay--------->");
            } else if (buf[6] == (byte) 0x41) {
                if (buf[1] == (byte) 0x01) {
                    setDefault();
                } else {
                    if (type == 0) {
                        openDoor();
                    } else if (type == 1) {
                        ManyBlue.blueWriteDataByteArray(BleUtils.getPower(), bean.getSn());
                        isPower = false;
                    }
                }
            }
        } else if (buf[5] == (byte) 0x02) {
            if (buf[6] == (byte) 0x58) {
                Log.e("dfc", "<--powerbuflength--->" + BleUtils.bytesToString(buf));
                if (buf.length >= 9) {
                    int value = ((int) buf[8]);
                    toShowValue(value);
                }
                //Gloal.m_spu_power.saveSharedPreferences(bean.getSn(),""+((int)buf[8]));
                setDefault();
            }
        }
    }

    private void toShowValue(int value) {
        if (value < 20) {
            Drawable drawable = getResources().getDrawable(R.mipmap
                    .power0);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            power.setCompoundDrawables(null, drawable, null, null);
        } else if (value >= 20 && value < 40) {
            Drawable drawable = getResources().getDrawable(R.mipmap
                    .power1);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            power.setCompoundDrawables(null, drawable, null, null);
        } else if (value >= 40 && value < 60) {
            Drawable drawable = getResources().getDrawable(R.mipmap
                    .power2);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            power.setCompoundDrawables(null, drawable, null, null);
        } else if (value >= 60 && value < 80) {
            Drawable drawable = getResources().getDrawable(R.mipmap
                    .power3);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            power.setCompoundDrawables(null, drawable, null, null);
        } else {
            Drawable drawable = getResources().getDrawable(R.mipmap
                    .power4);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            power.setCompoundDrawables(null, drawable, null, null);
        }
        power.setText(getResources().getString(R.string.power) + value + "%");
        //power.setText(value);
    }


    private void openDoor() {
        ManyBlue.blueWriteDataByteArray(BleUtils.openDoor(), bean.getSn());
    }


    private void addLog() {

        Map<String, String> params = new HashMap<>();
        params.put("token", Gloal.m_spu_token.loadStringSharedPreference(BaseConfig.TOKEN));
        params.put("type", "1");
        params.put("deviceid", bean.getId());
        RequestManager.requestString(LockDetailsActivity.this, CommonUrl.AddOpLog, params, parser, LockDetailsActivity.this, true);
    }

    IResponseParser parser = new IResponseParser() {
        @Override
        public void parseResponse(JSONObject response) {

        }

        @Override
        public void parseResponse(String string) {
            setDefault();
            if (null != string && !string.equals("")) {
                BaseBean bean = JsonUtils.getGson().fromJson(string, BaseBean.class);
                if (bean.getCode() == 0) {
                    TShow.showToast(LockDetailsActivity.this, R.string.tips_opendoor_success);
                }
            }

        }

        @Override
        public void parseError(VolleyError error) {

        }

        @Override
        public void parseDialog(int type) {
            if (type == 1) {
                setDefault();
            }

        }
    };

    @Override
    public void onDeviceScanner(BluetoothDevice device) {

    }

    @Override
    public void onDeviceScanner(List<BluetoothDevice> device) {

    }

    @Override
    public void onDeviceConnectState(boolean state, Object tag) {
        Log.e("dfc", "<---------->" + state);
        if (!state) {
            if (!isOpen)
                TShow.showToast(LockDetailsActivity.this, R.string.tips_connectblefailed);
            setDefault();
        } else {
            //mTipDlg.setTitle(R.string.connect_failed_tips);//连接成功 正在发现服务
            //setDialog("");
        }

    }

    @Override
    public void onDeviceServiceDiscover(List<BluetoothGattService> services, Object tag) {
        UUIDMessage uuidMessage = new UUIDMessage();//创建UUID的配置类

        uuidMessage.setCharac_uuid_service(BleSppGattAttributes.BLE_SPP_Service);
        uuidMessage.setCharac_uuid_write(BleSppGattAttributes.BLE_SPP_Write_Characteristic);
        uuidMessage.setCharac_uuid_read(BleSppGattAttributes.BLE_SPP_Notify_Characteristic);
        uuidMessage.setDescriptor_uuid_notify(BleSppGattAttributes.CLIENT_CHARACTERISTIC_CONFIG);
        ManyBlue.blueRegisterDevice(uuidMessage, tag);//注册设备
    }

    @Override
    public void onDeviceRegister(boolean state, Object tag) {
        if (state) {
            ManyBlue.setBlueWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT, tag);
            handler.sendEmptyMessageAtTime(0, 200);
            //mBluetoothLeService.writeData();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("dfc", "handler--------->");
            ManyBlue.blueWriteDataByteArray(BleUtils.setConnectPwdByte(bean.getCommKey()), bean.getSn());
            if (!BluetoothGattManager.getGatt(bean.getSn()).getWriteOrNot()) {
                handler.sendEmptyMessageAtTime(0, 5000);
            }
        }
    };

    @Override
    public void onDeviceWriteState(boolean writeState, Object tag) {
        Log.e("dfc", "指令发送状态:" + writeState);
    }

    @Override
    public void onDeviceReadMessage(CharacteristicValues characteristicValues, Object tag) {

    }

    @Override
    public void onDeviceNotifyMessage(CharacteristicValues characteristicValues, Object tag) {
        //appToast("指令发送状态:" + state);
        byte[] data = characteristicValues.getByArr();
        displayData(data);

    }


    @OnClick({R.id.btnleft, R.id.leftlayout, R.id.power, R.id.opendoor,
            R.id.sendpwd, R.id.shouquan, R.id.shouquanrz, R.id.managecard, R.id.managepwd,
            R.id.operationrz, R.id.seehousepwd, R.id.lockset})
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.btnleft:
            case R.id.leftlayout:
                finish();
                break;
            case R.id.power:
                getPower();
                break;
            case R.id.opendoor:
                open();
                break;
            case R.id.sendpwd:
                toSendPWd();
                break;
            case R.id.shouquan:
                toAuth();
                break;
            case R.id.shouquanrz:
                toAuthLog();
                break;
            case R.id.managecard:
                toManageCard();
                break;
            case R.id.managepwd:
                toManagePWd();
                break;
            case R.id.operationrz:
                toOperate();
                break;
            case R.id.seehousepwd:
                toSeeHousePWd();
                break;
            case R.id.lockset:
                toLockSet();
                break;

        }
    }

    private void toSendPWd() {
        Intent intent = new Intent(LockDetailsActivity.this, SendPwdActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void toManagePWd() {
        Intent intent = new Intent(LockDetailsActivity.this, ManagePwdActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void toAuth() {
        Intent intent = new Intent(LockDetailsActivity.this, AuthActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void toAuthLog() {
        Intent intent = new Intent(LockDetailsActivity.this, AuthLogActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void toManageCard() {
        Intent intent = new Intent(LockDetailsActivity.this, ListCradActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void toOperate() {
        Intent intent = new Intent(LockDetailsActivity.this, OperateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void toSeeHousePWd() {
        Intent intent = new Intent(LockDetailsActivity.this, SeeHousePwdActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void toLockSet() {
        Intent intent = new Intent(LockDetailsActivity.this, LockSetActivity.class);
        intent.putExtra("msg", branchMsg);
        intent.putExtra("auth",isAuth);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        intent.putExtras(bundle);
        startActivityForResult(intent, 101);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == 101) {
            setResult(RESULT_OK);
            finish();
        }
    }

}
