package cnaio.imooc.com.cniao5.activity;

import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cnaio.imooc.com.cniao5.R;
import cnaio.imooc.com.cniao5.adapter.MyOrderAdapter;
import cnaio.imooc.com.cniao5.base.BaseActivity;
import cnaio.imooc.com.cniao5.base.BaseApplication;
import cnaio.imooc.com.cniao5.model.Order;
import cnaio.imooc.com.cniao5.utils.Contants;
import cnaio.imooc.com.cniao5.utils.JSONUtil;
import cnaio.imooc.com.cniao5.utils.LogUtils;
import cnaio.imooc.com.cniao5.widget.CNiaoToolBar;
import cnaio.imooc.com.cniao5.widget.CardViewtemDecortion;


/**
 * 作者:candy
 * 创建时间:2017/12/3 15:02
 * 邮箱:1601796593@qq.com
 * 功能描述:
 * 我的订单
 **/
public class MyOrderActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {


    public static final int STATUS_ALL = 1000;
    public static final int STATUS_SUCCESS = 1; //支付成功的订单
    public static final int STATUS_PAY_FAIL = -2; //支付失败的订单
    public static final int STATUS_PAY_WAIT = 0; //：待支付的订单
    @BindView(R.id.toolbar)
    CNiaoToolBar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private int status = STATUS_ALL;


    private MyOrderAdapter mAdapter;

    @Override
    protected int setLayoutResouceId() {
        return R.layout.activity_my_order;
    }

    @Override
    protected void viewAttributes() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initTab();
        getOrders();
    }


    private void initTab() {

        TabLayout.Tab tab = tabLayout.newTab();
        tab.setText("全部");
        tab.setTag(STATUS_ALL);
        tabLayout.addTab(tab);


        tab = tabLayout.newTab();
        tab.setText("支付成功");
        tab.setTag(STATUS_SUCCESS);
        tabLayout.addTab(tab);

        tab = tabLayout.newTab();
        tab.setText("待支付");
        tab.setTag(STATUS_PAY_WAIT);
        tabLayout.addTab(tab);

        tab = tabLayout.newTab();
        tab.setText("支付失败");
        tab.setTag(STATUS_PAY_FAIL);
        tabLayout.addTab(tab);
        tabLayout.setOnTabSelectedListener(this);
    }
    private void getOrders() {
        Long userId = BaseApplication.getInstance().getUser().getId();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("status", status);
        params.put("token", BaseApplication.getInstance().getToken());

        getNovate().rxGet(Contants.API.ORDER_LIST, params, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (!TextUtils.isEmpty(response)) {
                    List<Order> orders = JSONUtil.parseOrders(response);
                    showOrders(orders);
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {

                LogUtils.i(e.getMessage());
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
    }
    private void showOrders(List<Order> orders) {

        if (mAdapter == null) {
            mAdapter = new MyOrderAdapter(this, orders);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addItemDecoration(new CardViewtemDecortion());
            mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    toDetailActivity(position);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        } else {
            mAdapter.getDatas().clear();
            mAdapter.getDatas().addAll(orders);
            recyclerView.setAdapter(mAdapter);
        }
    }
    private void toDetailActivity(int position) {
//        Intent intent = new Intent(this, OrderDetailActivity.class);
//        Order order = mAdapter.getItem(position);
//        intent.putExtra("order", order);
//        startActivity(intent, true);
    }
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        status = (int) tab.getTag();
        getOrders();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
