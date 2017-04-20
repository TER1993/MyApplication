package com.speedata.xu.myapplication.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.speedata.xu.myapplication.R;
import com.speedata.xu.myapplication.fragment.CheckFirstFragment;

/**
 * Created by xu on 2016/4/7.
 */
public class ConActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        setDefaultFragment();
    }

    /**
     * 设置默认的Fragment
     */
    private void setDefaultFragment() {

        FragmentManager fm = this.getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, new CheckFirstFragment());
        transaction.commit();
    }

}
