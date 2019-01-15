package com.dodoiot.lockapp.controller.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.dodoiot.lockapp.controller.fragment.LockFragment;

import java.util.List;

public class SetPwdPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager fragmentManager;
    private List<String> listString;
    List<Fragment> listFragment;
    int type;
    public SetPwdPagerAdapter(FragmentManager fm,List<String> listString,List<Fragment> listFragment) {
        super(fm);
        this.fragmentManager = fm;
        this.listString = listString;
        this.listFragment = listFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listString.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listString.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        this.fragmentManager.beginTransaction().show(fragment).commit();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = listFragment.get(position);
        fragmentManager.beginTransaction().hide(fragment).commit();
    }

}
