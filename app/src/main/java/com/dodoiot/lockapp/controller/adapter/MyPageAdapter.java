//package com.dodoiot.lockapp.controller.adapter;
//
//import android.support.annotation.NonNull;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.PagerAdapter;
//import android.util.Log;
//import android.view.ViewGroup;
//
//import com.dodoiot.lockapp.controller.fragment.LockFragment;
//import com.dodoiot.lockapp.model.DeviceBean;
//import com.dodoiot.lockapp.model.DeviceListBean;
//
//import java.util.List;
//
//public class MyPageAdapter extends FragmentPagerAdapter {
//    private FragmentManager fragmentManager;
//    private List<DeviceListBean> listString;
//    List<LockFragment> listFragment;
//    int type;
//    String branchMsg;
//    public MyPageAdapter(FragmentManager fm, List<DeviceListBean> listString, List<LockFragment> listFragment,String branchMsg) {
//        super(fm);
//        this.fragmentManager = fm;
//        this.listString = listString;
//        this.listFragment = listFragment;
//        this.branchMsg = branchMsg;
//    }
//
//
//    @Override
//    public Fragment getItem(int position) {
//        return listFragment.get(position).newStances(position, branchMsg, listString.get(position));
//    }
//
//
//    public void addAll(List<DeviceListBean> listData){
//        listString.clear();
//        listString.addAll(listData);
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public int getItemPosition(@NonNull Object object) {
//        return PagerAdapter.POSITION_NONE;
//    }
//
//    @Override
//    public int getCount() {
//        return listString.size();
//    }
//
////    @Override
////    public CharSequence getPageTitle(int position) {
////        return listString.get(position).getName();
////    }
//
//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        LockFragment fragment = (LockFragment) super.instantiateItem(container, position);
//        fragment.updateArguments(position, branchMsg, listString.get(position));
//        Log.e("dfc","instantiateItem=---------------->");
//        this.fragmentManager.beginTransaction().show(fragment).commit();
//        return fragment;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        Fragment fragment = listFragment.get(position);
//        fragmentManager.beginTransaction().hide(fragment).commit();
//    }
//
//}
