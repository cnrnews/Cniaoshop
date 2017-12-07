package cnaio.imooc.com.cniao5.fragment;


import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
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
import cnaio.imooc.com.cniao5.activity.WareDetailctivity;
import cnaio.imooc.com.cniao5.adapter.WareAdapter;
import cnaio.imooc.com.cniao5.base.BaseApplication;
import cnaio.imooc.com.cniao5.base.BaseFragment;
import cnaio.imooc.com.cniao5.model.Wares;
import cnaio.imooc.com.cniao5.utils.Contants;
import cnaio.imooc.com.cniao5.utils.JSONUtil;
import cnaio.imooc.com.cniao5.widget.DividerItemDecoration;

/**
 * 热卖
 * A simple {@link Fragment} subclass.
 *
 * @author lihl
 */
public class HotFragment extends BaseFragment implements OnRefreshLoadmoreListener, MultiItemTypeAdapter.OnItemClickListener {


    private static final Object PAGE_SIZE = 15;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    private WareAdapter wareAdapter;
    private List<Wares> wares = new ArrayList<>();
    private ProgressDialog progressDialog;
    private int mCurPage;
    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_hot;
    }

    @Override
    protected void viewAttributes() {
        recyclerview.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        recyclerview.addItemDecoration(new DividerItemDecoration());
        wareAdapter = new WareAdapter(mActivity, R.layout.template_hot_wares, wares);
        recyclerview.setAdapter(wareAdapter);
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage("loading...");
        smartRefreshLayout.autoRefresh();
    }

    @Override
    protected void setListener() {
        super.setListener();

        smartRefreshLayout.setOnRefreshLoadmoreListener(this);
        wareAdapter.setOnItemClickListener(this);
    }

    public static HotFragment newInstance() {
        return new HotFragment();
    }
    /***
     * 数据加载
     * @param mode
     */
    public void loadData(final Contants.LOAD_MODE mode) {
        progressDialog.show();
        if (mode == Contants.LOAD_MODE.REFRESH) {
            mCurPage = 1;
        } else {
            mCurPage++;
        }
        ArrayMap<String, Object> param = new ArrayMap<>(2);
        param.put("curPage", mCurPage);
        param.put("pageSize", PAGE_SIZE);
        param.put("token", BaseApplication.getInstance().getToken());
        getNovate().rxPost(Contants.API.WARES_HOT, param, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (!TextUtils.isEmpty(response)) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                        wares = JSONUtil.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<List<Wares>>() {
                        }.getType());
//                    DataParserUtils.parseWares(response);
                        if (mode == Contants.LOAD_MODE.REFRESH) {
                            wareAdapter.getDatas().clear();
                            wareAdapter.getDatas().addAll(wares);
                            recyclerview.setAdapter(wareAdapter);
                        } else {
                            wareAdapter.getDatas().addAll(wares);
                            wareAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    loadEnd();
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {
                loadEnd();
            }

            @Override
            public void onCancel(Object tag, Throwable e) {
                loadEnd();
            }
        });
    }
    private void loadEnd() {
        progressDialog.dismiss();
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh();
            smartRefreshLayout.finishLoadmore();
        }
    }
    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        loadData(Contants.LOAD_MODE.REFRESH);
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        loadData(Contants.LOAD_MODE.LOAD_MORE);
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        WareDetailctivity.actionStart(mActivity, wareAdapter.getDatas().get(position));
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }
}

