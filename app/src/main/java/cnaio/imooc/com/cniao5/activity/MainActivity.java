package cnaio.imooc.com.cniao5.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import butterknife.BindView;
import cnaio.imooc.com.cniao5.R;
import cnaio.imooc.com.cniao5.base.BaseActivity;
import cnaio.imooc.com.cniao5.fragment.CartFragment;
import cnaio.imooc.com.cniao5.fragment.CategoryFragment;
import cnaio.imooc.com.cniao5.fragment.HomeFragment;
import cnaio.imooc.com.cniao5.fragment.HotFragment;
import cnaio.imooc.com.cniao5.fragment.MineFragment;

/**
*作者:candy
*创建时间:2017/12/2 18:19
*邮箱:1601796593@qq.com
*功能描述:
**/
public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.bottom_navigation_bar)
    @Nullable
    BottomNavigationBar mBottomNavigationBar;
    private CartFragment mCartFragment;
    private CategoryFragment mCategoryFragment;
    private HomeFragment mHomeFragment;
    private HotFragment mHotFragment;
    private MineFragment mMineFragment;

    @Override
    protected int setLayoutResouceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void viewAttributes() {
        InitNavigationBar();
        setDefaultFragment();
    }
    private void InitNavigationBar() {
        mBottomNavigationBar.setTabSelectedListener(this);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.icon_home_press,getString(R.string.menu_home))
                        .setActiveColorResource(R.color.colorAccent)
                        .setInactiveIconResource(R.drawable.icon_home))
                .addItem(new BottomNavigationItem(R.drawable.icon_hot_press, getString(R.string.menu_hot))
                        .setActiveColorResource(R.color.colorAccent)
                        .setInactiveIconResource(R.drawable.icon_hot))
                .addItem(new BottomNavigationItem(R.drawable.icon_discover_press, getString(R.string.menu_category))
                        .setActiveColorResource(R.color.colorAccent)
                        .setInactiveIconResource(R.drawable.icon_discover))
                .addItem(new BottomNavigationItem(R.drawable.icon_cartfill_press, getString(R.string.menu_cart))
                        .setActiveColorResource(R.color.colorAccent)
                        .setInactiveIconResource(R.drawable.icon_cart))
                .addItem(new BottomNavigationItem(R.drawable.icon_user_press, getString(R.string.menu_mine)).
                        setActiveColorResource(R.color.colorAccent)
                        .setInactiveIconResource(R.drawable.icon_user))
                .setFirstSelectedPosition(0)
                .initialise();
    }
    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mHomeFragment = HomeFragment.newInstance();
        transaction.replace(R.id.fragment_container, mHomeFragment);
        transaction.commit();
    }
    @Override
    public void onTabSelected(int position) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment.newInstance();
                }
                transaction.replace(R.id.fragment_container, mHomeFragment);
                break;
            case 1:
                if (mHotFragment == null) {
                    mHotFragment = HotFragment.newInstance();
                }
                transaction.replace(R.id.fragment_container, mHotFragment);
                break;
            case 2:
                if (mCategoryFragment== null) {
                    mCategoryFragment = CategoryFragment.newInstance();
                }
                transaction.replace(R.id.fragment_container, mCategoryFragment);
                break;
            case 3:
                if (mCartFragment == null) {
                    mCartFragment = CartFragment.newInstance();
                }
                transaction.replace(R.id.fragment_container, mCartFragment);
                break;
            case 4:
                if (mMineFragment== null) {
                    mMineFragment = MineFragment.newInstance();
                }
                transaction.replace(R.id.fragment_container, mMineFragment);
                break;
            default:
                break;
        }
        // 事务提交
        transaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
