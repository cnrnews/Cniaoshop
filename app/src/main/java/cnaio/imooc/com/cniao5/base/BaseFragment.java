package cnaio.imooc.com.cniao5.base;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tamic.novate.Novate;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cnaio.imooc.com.cniao5.utils.Contants;


/**
*作者:candy
*创建时间:2017/12/3 21:30
*邮箱:1601796593@qq.com
*功能描述:Fragment封装
**/
public abstract class BaseFragment extends Fragment {
    /**
     * 贴附的activity
     */
    public FragmentActivity mActivity;

    /**
     * 根view
     */
    protected View mRootView;

    /**
     * 是否对用户可见
     */
    protected boolean mIsVisible;
    /**
     * 是否加载完成
     * 当执行完oncreatview,View的初始化方法后方法后即为true
     */
    protected boolean mIsPrepare;
    private Unbinder unbinder;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        mActivity = getActivity();
    }
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        mRootView = inflater.inflate(setLayoutResouceId(), container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        viewAttributes();
        initData(getArguments());
        mIsPrepare = true;
        onLazyLoad();
        setListener();
        return mRootView;
    }

    /**
     * 初始化数据
     * @author 漆可
     * @date 2016-5-26 下午3:57:48
     * @param arguments 接收到的从其他地方传递过来的参数
     */
    protected void initData(Bundle arguments)
    {

    }
    /**
     * 设置监听事件
     * @author 漆可
     * @date 2016-5-26 下午3:59:36
     */
    protected void setListener()
    {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);

        this.mIsVisible = isVisibleToUser;

        if (isVisibleToUser)
        {
            onVisibleToUser();
        }
    }

    /**
     * 用户可见时执行的操作
     * @author 漆可
     * @date 2016-5-26 下午4:09:39
     */
    protected void onVisibleToUser()
    {
        if (mIsPrepare && mIsVisible)
        {
            onLazyLoad();
        }
    }

    /**
     * 懒加载，仅当用户可见切view初始化结束后才会执行
     * @author 漆可
     * @date 2016-5-26 下午4:10:20
     */
    protected void onLazyLoad()
    {

    }
    /**
     * 设置根布局资源id
     * @author 漆可
     * @date 2016-5-26 下午3:57:09
     * @return
     */
    protected abstract int setLayoutResouceId();
    /**组件属性*/
    protected abstract void viewAttributes();
    protected Novate getNovate() {
        return
                new Novate.Builder(getActivity())
                        .baseUrl(Contants.API.BASE_URL)
                        .build();
    }
    public void startActivity(Class clas)
    {
        Intent intent=new Intent(getActivity(),clas);
        startActivity(intent);
    }
    public void startActivityForResult(Class clas)
    {
        Intent intent=new Intent(getActivity(),clas);
        getActivity().startActivityForResult(intent, Contants.REQUEST_CODE);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
