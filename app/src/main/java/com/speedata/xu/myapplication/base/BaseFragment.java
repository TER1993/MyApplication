package com.speedata.xu.myapplication.base;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.speedata.xu.myapplication.MainActivity;
import com.speedata.xu.myapplication.R;


/**
 * @author xuyan
 */
public abstract class BaseFragment extends Fragment implements IBaseFragment {
    private static final int containerViewId = R.id.container;
    private static Toast mToast;
    /**
     * 基础主界面
     **/
    public MainActivity mActivity;
    public SharedPreferences preferences;


    @Override
    public void onAttach(Activity activity) {
        mActivity = (MainActivity) getActivity();
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
//        mActivity = (MainActivity) getActivity();

        return inflater.inflate(setFragmentLayout(), container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(mActivity); //mActivity
        // .getSharedPreferences();//("",mActivity.MODE_PRIVATE);
        findById(this.getView());
    }

    /**
     * 打开新的Fragment
     *
     * @param fragment
     */
    public void openFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(containerViewId, fragment);
        transaction.addToBackStack(null);
        // 提交事物
        transaction.commit();
    }

    /**
     * 关闭Fragment
     */
    public void closeFragment() {
        FragmentManager fm = getFragmentManager();
        fm.popBackStack();
//		// 开启Fragment事务
//		FragmentTransaction transaction = fm.beginTransaction();
//		transaction.remove(fragment);
//		transaction.addToBackStack(null);
//		// 提交事物
//		transaction.commit();
    }

    @Override
    public void onPause() {
//        mActivity.cancelOnKeyDownListener();
//        mActivity.cancelonKeyUpListener();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (mToast != null) {
            mToast.cancel();
        }
        super.onDestroyView();
    }


}
