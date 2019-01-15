package com.dodoiot.lockapp.controller.fragment;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dodoiot.lockapp.BleSppGattAttributes;
import com.dodoiot.lockapp.R;
import com.dodoiot.lockapp.base.BaseConfig;
import com.dodoiot.lockapp.base.CommonUrl;
import com.dodoiot.lockapp.base.Gloal;
import com.dodoiot.lockapp.controller.activity.BaseFragment;
import com.dodoiot.lockapp.model.DeviceBean;
import com.dodoiot.lockapp.model.OffLinePwdBean;
import com.dodoiot.lockapp.net.IResponseParser;
import com.dodoiot.lockapp.net.RequestManager;
import com.dodoiot.lockapp.util.BleUtils;
import com.dodoiot.lockapp.util.DateUtil;
import com.dodoiot.lockapp.util.JsonUtils;
import com.dodoiot.lockapp.util.TShow;
//import com.dodoiot.lockapp.view.MyTipsDialog;
import com.dodoiot.lockapp.view.ProgressDialog;
import com.dodoiot.lockapp.view.TimeSelector;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.javac.ManyBlue.ManyBlue;
import io.javac.ManyBlue.bean.CharacteristicValues;
import io.javac.ManyBlue.bean.UUIDMessage;
import io.javac.ManyBlue.interfaces.BaseNotifyListener;
import io.javac.ManyBlue.manager.BluetoothGattManager;


public class CustomFragment extends BaseFragment implements BaseNotifyListener.DeviceListener,BaseNotifyListener.DeviceDataListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @InjectView(R.id.editpwd)
    EditText editpwd;
    @InjectView(R.id.getCode)
    Button getCode;
    @InjectView(R.id.tvstarttime)
    TextView tvstarttime;
    @InjectView(R.id.starttimelayout)
    LinearLayout starttimelayout;
    @InjectView(R.id.tvendtime)
    TextView tvendtime;
    @InjectView(R.id.endtimelayout)
    LinearLayout endtimelayout;
    @InjectView(R.id.btnsetpwd)
    Button btnsetpwd;

    ProgressDialog mTipDlg;

    private String mParam1;
    private String mParam2;
    boolean isName = false;
    boolean isStart = false;
    boolean isEnd = false;

    DeviceBean bean;
    public static CustomFragment newInstance(String param1, DeviceBean bean) {
        CustomFragment fragment = new CustomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, bean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            bean = (DeviceBean) getArguments().getSerializable(ARG_PARAM2);
        }
    }


    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_custom;
    }

    @Override
    protected void initView(View view) {
        mTipDlg = new ProgressDialog(getActivity(),getResources().getString(R.string.tips_load_message));
        mTipDlg.setCancelable(false);



    }

    @Override
    protected void initListener() {
        editpwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    isName = true;
                }else{
                    isName = false;
                }
                show();
            }
        });
        tvstarttime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    isStart = true;
                }else{
                    isStart = false;
                }
                show();
            }
        });
        tvendtime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    isEnd = true;
                }else{
                    isEnd = false;
                }
                show();
            }
        });

    }

    private void show(){

        if(isStart&& isEnd && isName){
            btnsetpwd.setBackgroundResource(R.drawable.btnback_selector);
        }else{
            btnsetpwd.setBackgroundResource(R.drawable.btn_register_background1);
        }

    }

    int showType = 0;
    @OnClick({R.id.starttimelayout,R.id.endtimelayout,R.id.btnsetpwd,R.id.getCode})
    protected void onClickView(View v){
        switch (v.getId()){
            case R.id.starttimelayout:
                showType = 0;
                showTime();
                break;
            case R.id.endtimelayout:
                showType= 1;
                showTime();
                break;
            case R.id.btnsetpwd:
                connect();
                break;
            case R.id.getCode:
                toGetCode();
                break;
        }
    }

    private void toGetCode(){
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<6; i++){
            //sb.append(Math.floor(Math.random()*10));
            int number = new Random().nextInt(10);
            sb.append(number);
        }
        editpwd.setText(sb.toString());
    }

    private TimeSelector timeSelector;
    private void showTime() {
        if (timeSelector == null)
            timeSelector = new TimeSelector(getActivity(), new TimeSelector.ResultHandler() {
                @Override
                public void handle(String time) {
                    Log.e("dfc","TimeSelector-------"+time);
                    if (showType == 0) {
                        tvstarttime.setText(time);
                    } else {
                        tvendtime.setText(time);


                    }
                }
            }, tvstarttime.getText().toString(), "2030-12-31 23:59","yyyy-MM-dd HH:mm");
        timeSelector.setMode(TimeSelector.MODE.YMDHM);
        if(showType == 0){
            timeSelector.setTitle(getResources().getString(R.string.starttime));
        }else{
            timeSelector.setTitle(getResources().getString(R.string.endtime));
        }
        timeSelector.show();
    }



    private void addPassWord(String innerId){

        mTipDlg.show();
        Map<String,String> params = new HashMap<>();
        params.put("token", Gloal.m_spu_token.loadStringSharedPreference(BaseConfig.TOKEN));
        params.put("sn",bean.getSn());
        params.put("type",4+"");
        params.put("password", editpwd.getText().toString().trim());
        params.put("deviceid",bean.getId());
        params.put("innerId",""+innerId);
        params.put("timeout","5000");
        params.put("from",""+DateUtil.getTimeMillis(tvstarttime.getText().toString().trim(),"yyyy-MM-dd HH:mm:ss"));
        params.put("to",""+DateUtil.getTimeMillis(tvendtime.getText().toString().trim(),"yyyy-MM-dd HH:mm:ss"));

        RequestManager.requestString(getActivity(), CommonUrl.AddBleLockerPassword,params,parser,getActivity(),true);
    }

    IResponseParser parser = new IResponseParser() {
        @Override
        public void parseResponse(JSONObject response) {

        }

        @Override
        public void parseResponse(String string) {
            setDefault();
            if(null != string && !string.equals("")){
                Log.e("dfc","<string------->"+string);
                OffLinePwdBean bean = JsonUtils.getGson().fromJson(string,OffLinePwdBean.class);
                if(bean.getCode() == 0){
                    //editpwd.setText(bean.getPassword());
                    StringBuffer sb = new StringBuffer();
                    sb.append(getResources().getString(R.string.tips_allpwd));
                    sb.append(editpwd.getText().toString().trim()+"\r\n");
                    sb.append(getResources().getString(R.string.validtime)+tvstarttime.getText().toString()+"\r\n");
                    sb.append(getResources().getString(R.string.overtime)+tvstarttime.getText().toString()+"\r\n");
                    sb.append(getResources().getString(R.string.tips_allpwd2));
                    final  String msg = sb.toString();
//                    MyTipsDialog.showMyDailog(getActivity(),sb.toString(),"", new MyTipsDialog.IDialogMethod() {
////                        @Override
////                        public void sure() {
////                            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"));
////                            intent.putExtra("sms_body", msg);
////                            startActivity(intent);
////                        }
////
////                    });

                }else{
                    TShow.showToast(getActivity(),R.string.tips_getoffline_failed);
                }
            }
        }

        @Override
        public void parseError(VolleyError error) {
            TShow.showToast(getActivity(),R.string.connect_failed_tips);
            setDefault();
        }

        @Override
        public void parseDialog(int type) {
            if(type == 1){
                setDefault();
            }

        }
    };

    private void setDefault(){
        if(mTipDlg != null)
            mTipDlg.dismiss();
    }




    boolean isAdd = false;
    private void displayData(byte[] buf){
        Log.e("dfc","<--buflength--->"+BleUtils.bytesToString(buf));
        if(buf[5] == (byte)0x02){
            if(buf[6] == (byte)0x44){
                Log.e("dfc","<------------->"+buf.length);
                {
                    if(!isAdd){
                        isAdd = true;
                        addPassWord(((int)buf[8])+"");
                    }

                }
            }else if(buf[6] == (byte)0xff){
                TShow.showToast(getActivity(),"密码数量已到上限，请先删除密码后再重试");
            }
        }else if(buf[5] == (byte)0x10){
            if(buf[6] == (byte)0x41){
                if(buf[1] ==(byte)0x01){
                    setDefault();
                    TShow.showToast(getActivity(),R.string.tips_add_failed);
                }else{
                    if(isTop)
                        toWrite();

                }

            }
        }
    }

    boolean isTop = false;

    private void connect(){
        Log.e("dfc","connect22222222222");
        mTipDlg.show();
        //ManyBlue.getConnDevice()
        if(BluetoothGattManager.gattMap.containsKey(bean.getSn())){
            ManyBlue.blueWriteDataByteArray(BleUtils.setConnectPwdByte(bean.getCommKey()),bean.getSn());
        }else{
            ManyBlue.blueConnectDevice(bean.getSn(), bean.getSn());//连接该设备  并且以该设备的mac地址作为标识
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        isTop = true;
        long startTime  = System.currentTimeMillis();
        tvstarttime.setText(DateUtil.getDateStringByTimeMillis1(startTime));
        tvendtime.setText(DateUtil.getDateStringByTimeMillis1(startTime+24*60*60*1000));
    }

    @Override
    public void onPause() {
        super.onPause();
        isTop = false;
    }

    private void toWrite(){
        {
            long start = DateUtil.getTimeMillis2(tvstarttime.getText().toString().trim());
            long end = DateUtil.getTimeMillis2(tvendtime.getText().toString().trim());
            long btime= DateUtil.getTimeMillis1("2018-01-01 00:00:00");
            long distance = (start - btime)/1000/60;
            long distance1 = (end - btime)/1000/60;
            //mBluetoothLeService.writeData(BleUtils.addOnePWd(false,tvpwd.getText().toString().trim(),distance,distance1));
            ManyBlue.blueWriteDataByteArray(BleUtils.addOnePWd(false,editpwd.getText().toString().trim(),distance,distance1),bean.getSn());
        }
        //
    }

    @Override
    public void onDeviceWriteState(boolean writeState, Object tag) {
        Log.e("dfc","指令发送状态:" + writeState);
    }

    @Override
    public void onDeviceReadMessage(CharacteristicValues characteristicValues, Object tag) {

    }

    @Override
    public void onDeviceNotifyMessage(CharacteristicValues characteristicValues, Object tag) {
        byte[] data = characteristicValues.getByArr();
        displayData(data);
    }

    @Override
    public void onDeviceScanner(BluetoothDevice device) {

    }

    @Override
    public void onDeviceScanner(List<BluetoothDevice> device) {

    }

    @Override
    public void onDeviceConnectState(boolean state, Object tag) {
        if (!state) {
            TShow.showToast(getActivity(),R.string.tips_connectblefailed);
            setDefault();
        } else{
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
        /**
         * 这里简单说一下  如果设备返回数据的方式不是Notify的话  那就意味着向设备写出数据之后   再自己去获取数据
         * Notify的话 是如果蓝牙设备有数据传递过来  能接受到通知
         * 使用场景中如果没有notify的话  notify uuid留空即可
         */
        ManyBlue.blueRegisterDevice(uuidMessage, tag);//注册设备
    }

    @Override
    public void onDeviceRegister(boolean state, Object tag) {
//        if (state)
        if(state){
            ManyBlue.setBlueWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT,tag);
            // ManyBlue.blueWriteDataByteArray(BleUtils.setConnectPwdByte(bean.getCommKey()),bean.getSn());
            //mBluetoothLeService.writeData();
            handler.sendEmptyMessageAtTime(0,200);
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("dfc","handler--------->");
            ManyBlue.blueWriteDataByteArray(BleUtils.setConnectPwdByte(bean.getCommKey()),bean.getSn());
            if(!BluetoothGattManager.getGatt(bean.getSn()).getWriteOrNot()){
                handler.sendEmptyMessageAtTime(0,10000);
            }
        }
    };

}
