package com.tqmall.mvp.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.tqmall.di.component.DaggerFragmentComponent;
import com.tqmall.di.component.FragmentComponent;
import com.tqmall.di.module.FragmentModule;
import com.tqmall.global.App;
import com.tqmall.mvp.presenter.base.MvpPresenter;

/**
 * Created by Jay on 16/9/14.
 */

public abstract class BaseFragment<T extends MvpPresenter> extends Fragment implements View.OnClickListener{

    protected View view;
    protected Activity mAct;
    protected T mPresenter;
    protected FragmentComponent mFragmentComponent;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(getFragmentRes(), container, false);
        initRes();
        setFragmentClickEvent();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentComponent = DaggerFragmentComponent.builder()
                .applicationComponent(((App) getActivity().getApplication()).getApplicationComponent())
                .fragmentModule(new FragmentModule(this))
                .build();
        initInjector();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAct=getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    /**
     * 封装 findViewById
     */
    protected <T extends View> T findViewById(int id) {
        if (view == null) {
            throw new UnsupportedOperationException("view is null");
        }
        return (T) view.findViewById(id);
    }


    @Override
    public void onClick(View v) {
        onFragmentClick(v);
    }

    /**
     * 返回子fragment的布局
     *
     * @return
     */
    public abstract int getFragmentRes();

    /**
     * 初始化子类fragment的控件
     */
    public abstract void initRes();

    /**
     * 控件点击事件
     */
    public abstract void onFragmentClick(View view);

    /**
     * 控件点击事件监听
     */
    public abstract void setFragmentClickEvent();


    /**
     * 开启注入
     */
    public abstract void initInjector();



}
