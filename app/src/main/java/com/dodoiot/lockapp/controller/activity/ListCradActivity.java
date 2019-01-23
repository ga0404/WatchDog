package com.dodoiot.lockapp.controller.activity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dodoiot.lockapp.BleSppGattAttributes;
import com.dodoiot.lockapp.R;
import com.dodoiot.lockapp.base.BaseConfig;
import com.dodoiot.lockapp.base.CommonUrl;
import com.dodoiot.lockapp.base.Gloal;
//import com.dodoiot.lockapp.controller.adapter.BranchAdapter;
import com.dodoiot.lockapp.controller.adapter.CardAdapter;
import com.dodoiot.lockapp.model.BaseBean;
import com.dodoiot.lockapp.model.CardListBean;
import com.dodoiot.lockapp.model.DeviceBean;
import com.dodoiot.lockapp.model.LockCard;
import com.dodoiot.lockapp.net.IResponseParser;
import com.dodoiot.lockapp.net.RequestManager;
import com.dodoiot.lockapp.util.BleUtils;
import com.dodoiot.lockapp.util.Dp2px;
import com.dodoiot.lockapp.util.JsonUtils;
import com.dodoiot.lockapp.util.TShow;
//import com.dodoiot.lockapp.view.MyTipsDialog;
import com.dodoiot.lockapp.view.ProgressDialog;
import com.dodoiot.lockapp.view.swipe.SwipeMenu;
import com.dodoiot.lockapp.view.swipe.SwipeMenuCreator;
import com.dodoiot.lockapp.view.swipe.SwipeMenuItem;
import com.dodoiot.lockapp.view.swipe.SwipeMenuListView;

import org.json.JSONObject;

import java.util.ArrayList;
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

public class ListCradActivity extends BaseActivity implements BaseNotifyListener.DeviceListener,BaseNotifyListener.DeviceDataListener{


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
    @InjectView(R.id.cardlistview)
    SwipeMenuListView cardlistview;

    DeviceBean bean;
    int type = 0;
    int selectPos = -1;
    CardAdapter adapter;
    List<LockCard> listData = new ArrayList<>();
    @Override
    protected void initLayoutId() {
        isFull = true;
        layoutId = R.layout.activity_list_crad;
    }

    @Override
    protected void initView() {
        mTipDlg = new ProgressDialog(ListCradActivity.this,getResources().getString(R.string.tips_load_message));
        mTipDlg.setCancelable(false);
        bean = (DeviceBean) getIntent().getSerializableExtra("bean");
        tvtitle.setText(R.string.icCard);
        btnright.setVisibility(View.GONE);
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setText(R.string.add);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

//                SwipeMenuItem noteItem = new SwipeMenuItem(
//                        getApplicationContext());
//                // set item background
//                noteItem.setBackground(new ColorDrawable(Color.parseColor("#C7C7C7")));
//                // set item width
//                noteItem.setWidth(Dp2px.dp2px(ListCradActivity.this, 90));
//                // set a icon
////                deleteItem.setIcon(R.mipmap.delete);
//                noteItem.setTitle(getResources().getString(R.string.renote));
//                noteItem.setTitleSize(16);
//                noteItem.setTitleColor(Color.WHITE);
//                menu.addMenuItem(noteItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(Dp2px.dp2px(ListCradActivity.this, 90));
                // set a icon
//                deleteItem.setIcon(R.mipmap.delete);
                deleteItem.setTitle(getResources().getString(R.string.delete));
                deleteItem.setTitleSize(16);
                deleteItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        cardlistview.setMenuCreator(creator);
        adapter = new CardAdapter(this,listData);
        cardlistview.setAdapter(adapter);
        initListener();
        loadData();

    }

    private void initListener(){
        cardlistview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {

                final LockCard message = (LockCard) adapter.getItem(position);
                if (index == 0) {
                    MyTipsDialog.showNickDialog(ListCradActivity.this,R.string.intputcardname,R.string.lockcardname ,message.getName(),false, new MyTipsDialog.IDialogInputNoteMethod() {
                        @Override
                        public void sure(String note) {
                            //toSaveEdit(message,note);
                            message.setName(note);
                            updateType(message);
                        }
                    });
                } else if (index == 1) {
                    selectPos = position;
                    showDialog();

                }
            }
        });
//        cardlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                final LockCard message = (LockCard) adapter.getItem(i);
//                toEdit("edit",message);
//            }
//        });
    }


    @OnClick({R.id.btnleft,R.id.leftlayout,R.id.btnRight})
    protected void onClickView(View v){
        switch (v.getId()){
            case R.id.leftlayout:
            case R.id.btnleft:
                finish();
                break;
            case R.id.btnRight:
                toAdd("add");
                break;

        }
    }





    private void toAdd(String type){
        Intent intent = new Intent(ListCradActivity.this,AddCardActivity.class);
        intent.putExtra("type",type);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean",bean);
        intent.putExtras(bundle);
        startActivityForResult(intent,101);
    }

    private void toEdit(String type,LockCard message){
        Intent intent = new Intent(ListCradActivity.this,AddCardActivity.class);
        intent.putExtra("type",type);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean",bean);
        bundle.putSerializable("bean1",message);
        intent.putExtras(bundle);
        startActivityForResult(intent,101);
    }


    private void delType(String hid){
        mTipDlg.show();
        type = 1;
        Map<String,String> params = new HashMap<>();
        params.put("token", Gloal.m_spu_token.loadStringSharedPreference(BaseConfig.TOKEN));
        params.put("cardid",hid);
        params.put("sn",bean.getSn());
        RequestManager.requestString(ListCradActivity.this, CommonUrl.DeleteLockerCard,params,parser,ListCradActivity.this,true);
    }

    private void updateType(LockCard bean1){//HouseTypeBean bean
        mTipDlg.show();
        type = 3;
        Map<String,String> params = new HashMap<>();
        params.put("token", Gloal.m_spu_token.loadStringSharedPreference(BaseConfig.TOKEN));
        params.put("cardname",bean1.getName());
        params.put("cardid",bean1.getId());
        params.put("from",bean1.getFromTime());
        params.put("to",bean1.getToTime());
        params.put("innerId",bean1.getInnerId());
        params.put("sn",bean.getSn());
        RequestManager.requestString(ListCradActivity.this, CommonUrl.EditBleLockerCard,params,parser,ListCradActivity.this,true);
    }


    private void loadData(){
        mTipDlg.show();
        type = 0;
        Map<String,String> params = new HashMap<>();
        params.put("token", Gloal.m_spu_token.loadStringSharedPreference(BaseConfig.TOKEN));
        params.put("sn",bean.getSn());
        RequestManager.requestString(ListCradActivity.this, CommonUrl.ListLockerCard,params,parser,ListCradActivity.this,true);
    }

    IResponseParser parser = new IResponseParser() {
        @Override
        public void parseResponse(JSONObject response) {

        }

        @Override
        public void parseResponse(String string) {
            setDefault();
            if(null != string && !string.equals("")){
                if(type == 0){
                    CardListBean bean = JsonUtils.getGson().fromJson(string,CardListBean.class);
                    if(bean.getCode() == 0){
                        listData.clear();
                        listData.addAll(bean.getCardlist());
                        adapter.notifyDataSetChanged();
                    }
                }else if(type == 1){
                    BaseBean bean = JsonUtils.getGson().fromJson(string,BaseBean.class);
                    if(bean.getCode() == 0){
                        TShow.showToast(ListCradActivity.this,R.string.tips_delete_success);
                        listData.remove(selectPos);
                        adapter.notifyDataSetChanged();
                    }else{
                        TShow.showToast(ListCradActivity.this,R.string.tips_delete_failed);
                    }
                    selectPos = -1;
                }else if(type == 2){
                    BaseBean bean = JsonUtils.getGson().fromJson(string,BaseBean.class);
                    if(bean.getCode() == 0){
                        TShow.showToast(ListCradActivity.this,R.string.tips_add_success);
                        loadData();
                    }else{
                        TShow.showToast(ListCradActivity.this,R.string.tips_add_failed);
                    }

                }else if(type == 3){
                    BaseBean bean = JsonUtils.getGson().fromJson(string,BaseBean.class);
                    if(bean.getCode() == 0){
                        TShow.showToast(ListCradActivity.this,R.string.tips_modify_success);
                        adapter.notifyDataSetChanged();
                    }else{
                        TShow.showToast(ListCradActivity.this,R.string.tips_modify_failed);
                    }

                }
            }
        }

        @Override
        public void parseError(VolleyError error) {
            TShow.showToast(ListCradActivity.this,R.string.connect_failed_tips);
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



    private void showDialog(){
        MyTipsDialog.showExitDialog(ListCradActivity.this,R.string.tips_delicornot, new MyTipsDialog.IDialogInputNoteMethod() {
            @Override
            public void sure(String note) {
                del();
            }
        });
    }

    private void del(){
        mTipDlg.show();
        if(BluetoothGattManager.gattMap.containsKey(bean.getSn())){
            ManyBlue.blueWriteDataByteArray(BleUtils.setConnectPwdByte(bean.getCommKey()),bean.getSn());
        }else{
            ManyBlue.blueConnectDevice(bean.getSn(), bean.getSn());//连接该设备  并且以该设备的mac地址作为标识
        }
        //ManyBlue.blueConnectDevice(bean.getSn(), bean.getSn());//连接该设备  并且以该设备的mac地址作为标识
    }


    boolean isTop = false;
    @Override
    protected void onResume() {
        super.onResume();
        isTop = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isTop = false;
    }

    boolean isDel = false;
    private void displayData(byte[] buf){
//        Log.e("dfc","<--buflength--->"+BleUtils.bytesToString(buf));
        if(buf[5] == (byte)0x10){
            if(buf[6] == (byte)0x41){
                if(buf[1] ==(byte)0x01 ){
                    setDefault();
                    TShow.showToast(ListCradActivity.this,R.string.tips_delete_failed);
                }else{
                    if(isTop){
                        LockCard bean1 = (LockCard) adapter.getItem(selectPos);
                        int innerId = Integer.parseInt(bean1.getInnerId());
                        ManyBlue.blueWriteDataByteArray(BleUtils.delCard(innerId),bean.getSn());
                    }
                }
            }
        }else if(buf[5] == (byte)0x02){
            if(buf[6] == (byte)0x48){
                if(isDel)
                    return;
                isDel = true;
                LockCard bean = (LockCard) adapter.getItem(selectPos);
                delType(bean.getId());
            }
        }
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
            TShow.showToast(ListCradActivity.this,R.string.tips_connectblefailed);
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
