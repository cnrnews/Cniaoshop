package cnaio.imooc.com.cniao5.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cnaio.imooc.com.cniao5.R;
import cnaio.imooc.com.cniao5.adapter.WareAdapter;
import cnaio.imooc.com.cniao5.base.BaseActivity;
import cnaio.imooc.com.cniao5.model.Wares;
import cnaio.imooc.com.cniao5.utils.Contants;
import cnaio.imooc.com.cniao5.utils.JSONUtil;
import cnaio.imooc.com.cniao5.widget.CNiaoToolBar;
import cnaio.imooc.com.cniao5.widget.DividerItemDecoration;

/**
*作者:candy
*创建时间:2017/12/3 19:01
*邮箱:1601796593@qq.com
*功能描述:
 * 商品列表
**/
public class WareListActivity extends BaseActivity
        implements OnRefreshLoadmoreListener, TabLayout.OnTabSelectedListener, MultiItemTypeAdapter.OnItemClickListener {
    private static final int TAG_DEFAULT = 0;
    private static final int TAG_PRICE = 1;
    private static final int TAG_SALE = 2;

    private static final int MODE_GRID = 1;
    private static final int MODE_LIST = 2;

    public static final String CAMPAIGNID = "campaignId";


    @BindView(R.id.ware_list_toolbar)
    CNiaoToolBar wareListToolbar;
    @BindView(R.id.ware_list_tablayout)
    TabLayout wareListTablayout;
    @BindView(R.id.ware_list_recyclerview)
    RecyclerView wareListRecyclerview;
    @BindView(R.id.ware_list_refreshlayout)
    SmartRefreshLayout wareListRefreshlayout;

    private WareAdapter adapter;

    private int mCurPage;
    private int mCampaignId;
    private int mOrderBy = TAG_DEFAULT;
    private int mViewMode = MODE_LIST;
    private List<Wares> mWareList=new ArrayList<>();

    @Override
    protected void initData(Bundle arguments) {
        super.initData(arguments);

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mCampaignId = bundle.getInt(CAMPAIGNID);
        }
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.activity_ware_list;
    }

    @Override
    protected void viewAttributes() {
        initTab();
        initRecylerView();
        loadData(Contants.LOAD_MODE.REFRESH);
    }
    public static void actionStart(Context context, int campaignId) {
        Intent intent = new Intent(context, WareListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(CAMPAIGNID, campaignId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    @Override
    public void setListener() {
        wareListRefreshlayout.setOnRefreshLoadmoreListener(this);
        wareListRefreshlayout.autoRefresh();
        wareListToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        wareListToolbar.setRightButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewMode == MODE_LIST) {
                    mViewMode = MODE_GRID;
                    wareListToolbar.setRightButton(ContextCompat.getDrawable(activity, R.drawable.icon_mode_grid));
                    wareListRecyclerview.setLayoutManager(new GridLayoutManager(activity, 2));
                    wareListRecyclerview.setAdapter(adapter);
                } else {
                    mViewMode = MODE_LIST;
                    wareListToolbar.setRightButton(ContextCompat.getDrawable(activity, R.drawable.icon_mode_list));
                    wareListRecyclerview.setLayoutManager(new LinearLayoutManager(activity));
                    adapter = new WareAdapter(activity, R.layout.template_hot_wares, mWareList);
                    wareListRecyclerview.setAdapter(adapter);
                }
            }
        });
    }

    private void initRecylerView() {
        wareListRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        wareListRecyclerview.addItemDecoration(new DividerItemDecoration());
        adapter = new WareAdapter(activity, R.layout.template_hot_wares, mWareList);
        adapter.setOnItemClickListener(this);
    }
    private void initTab() {
        TabLayout.Tab tab = wareListTablayout.newTab();
        tab.setText("默认");
        tab.setTag(TAG_DEFAULT);
        wareListTablayout.addTab(tab);

        tab = wareListTablayout.newTab();
        tab.setText("价格");
        tab.setTag(TAG_PRICE);
        wareListTablayout.addTab(tab);


        tab = wareListTablayout.newTab();
        tab.setText("销量");
        tab.setTag(TAG_SALE);
        wareListTablayout.addTab(tab);
        wareListTablayout.addOnTabSelectedListener(this);
    }
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mOrderBy = (int) tab.getTag();
        loadData(Contants.LOAD_MODE.REFRESH);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        mOrderBy = (int) tab.getTag();
        loadData(Contants.LOAD_MODE.REFRESH);
    }

    public void loadData(final Contants.LOAD_MODE mode) {
        if (Contants.LOAD_MODE.REFRESH == mode) {
            mCurPage = 1;
        } else {
            mCurPage++;
        }
        ArrayMap<String, Object> param = new ArrayMap<>(4);
        param.put("campaignId", mCampaignId);
        param.put("orderBy", mOrderBy);
        param.put("curPage", mCurPage);
        param.put("pageSize", 12);
        getNovate().rxPost(Contants.API.WARES_CAMPAIN_LIST, param, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String json = jsonObject.getJSONArray("list").toString();
                        mWareList = JSONUtil.fromJson(json, new TypeToken<List<Wares>>() {
                        }.getType());
                        if (mode == Contants.LOAD_MODE.REFRESH) {
                            if (mViewMode == MODE_GRID) {
                                adapter = new WareAdapter(WareListActivity.this, R.layout.template_grid_wares, mWareList);
                            } else {
                                adapter = new WareAdapter(WareListActivity.this, R.layout.template_hot_wares, mWareList);

                            }
                            wareListRecyclerview.setAdapter(adapter);
                        } else {
                            adapter.getDatas().addAll(mWareList);
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                loaadEnded();
            }

            @Override
            public void onError(Object tag, Throwable e) {
                loaadEnded();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {
            }
        });
    }

    private void loaadEnded() {
        wareListRefreshlayout.finishRefresh();
        wareListRefreshlayout.finishLoadmore();
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        loadData(Contants.LOAD_MODE.LOAD_MORE);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        loadData(Contants.LOAD_MODE.REFRESH);
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        WareDetailctivity.actionStart(activity,adapter.getDatas().get(position));
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }
}
