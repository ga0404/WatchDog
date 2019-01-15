package com.dodoiot.lockapp.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dodoiot.lockapp.R;
import com.dodoiot.lockapp.model.LockPassWord;
import com.dodoiot.lockapp.view.CircleImageView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PwdAdapter<T> extends BaseAdapter {
    Context context;
    List<T> list;
    LayoutInflater inflater;

    public PwdAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_pwd_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LockPassWord bean = (LockPassWord) list.get(i);
        //0管理员密码 1清除密码 2永久密码 3临时密码
        holder.pwd.setText(bean.getValue());
        if(bean.getType().equals("1")){
            holder.operationtype.setText(R.string.managerpwd);
        }else if(bean.getType().equals("7")){
            holder.operationtype.setText(R.string.clearpwd);
        }else if(bean.getType().equals("6")){
            holder.operationtype.setText(R.string.allpwd);
        }else if(bean.getType().equals("5")){
            holder.operationtype.setText(R.string.offlinepwd);
        }else if(bean.getType().equals("4")){
            holder.operationtype.setText(R.string.zdypwd);
        }else if(bean.getType().equals("3")){
            holder.operationtype.setText(R.string.seehousepwd);
        }
        if(!bean.getFromTime().equals("") && !bean.getToTime().equals("")){
            holder.operationtime.setText(bean.getFromTime()+"-"+bean.getToTime());
        }else{
            holder.operationtime.setText(R.string.forever);
        }

        return convertView;
    }

    class ViewHolder {
        @InjectView(R.id.lockimage)
        CircleImageView lockimage;
        @InjectView(R.id.operationtype)
        TextView operationtype;
        @InjectView(R.id.operationtime)//
                TextView operationtime;
        @InjectView(R.id.next)
        ImageView next;
        @InjectView(R.id.pwd)
        TextView pwd;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
