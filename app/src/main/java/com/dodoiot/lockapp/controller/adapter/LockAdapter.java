package com.dodoiot.lockapp.controller.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dodoiot.lockapp.R;
import com.dodoiot.lockapp.model.DeviceBean;
import com.dodoiot.lockapp.view.CircleImageView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LockAdapter<T> extends BaseAdapter {

    Context context;
    List<T> list;
    LayoutInflater inflater;

    public LockAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }



    public void addDevice(BluetoothDevice device) {
//        if (list.indexOf(device) == -1) {
//            list.add(device);
//            notifyItemInserted(list.size() - 1);
//        }
    }

    public void onRefresh(List<BluetoothDevice> list) {
//        this.list.clear();
//        this.list.addAll(list);
//        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_lock_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DeviceBean bean = (DeviceBean) list.get(i);
        holder.lockname.setText(bean.getName());
        if(bean.getValideFrom().equals("") && bean.getValideTo().equals("")){
            holder.lockpower.setText(R.string.forever);
        }else{
            holder.lockpower.setText(bean.getValideFrom()+"-"+bean.getValideTo());
        }
        return convertView;
    }


    class ViewHolder {
        @InjectView(R.id.lockimage)
        CircleImageView lockimage;
        @InjectView(R.id.lockname)
        TextView lockname;
        @InjectView(R.id.lockpower)
        TextView lockpower;
        @InjectView(R.id.next)
        ImageView next;
        @InjectView(R.id.lockitemlayout)
        RelativeLayout lockitemlayout;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
