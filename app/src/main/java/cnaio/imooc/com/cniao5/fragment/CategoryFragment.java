package cnaio.imooc.com.cniao5.fragment;


import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
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
import cnaio.imooc.com.cniao5.adapter.CategoryAdapter;
import cnaio.imooc.com.cniao5.adapter.WareAdapter;
import cnaio.imooc.com.cniao5.bannerholder.BannerHolder;
import cnaio.imooc.com.cniao5.base.BaseApplication;
import cnaio.imooc.com.cniao5.base.BaseFragment;
import cnaio.imooc.com.cniao5.model.Banner;
import cnaio.imooc.com.cniao5.model.Category;
import cnaio.imooc.com.cniao5.model.Wares;
import cnaio.imooc.com.cniao5.utils.Contants;
import cnaio.imooc.com.cniao5.utils.JSONUtil;
import cnaio.imooc.com.cniao5.widget.CNiaoToolBar;
import cnaio.imooc.com.cniao5.widget.DividerItemDecoration;

/**
 * 作者:candy
 * 创建时间:2017/12/3 20:50
 * 邮箱:1601796593@qq.com
 * 功能描述: 分类
 **/
public class CategoryFragment extends BaseFragment implements MultiItemTypeAdapter.OnItemClickListener, OnRefreshLoadmoreListener {
    private static final int PAGE_SIZE_NUM = 15;
    @BindView(R.id.toolbar)
    CNiaoToolBar toolbar;
    @BindView(R.id.recyclerview_category)
    RecyclerView recyclerviewCategory;
    @BindView(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @BindView(R.id.recyclerview_wares)
    RecyclerView recyclerviewWares;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;


    private CategoryAdapter categoryAdapter;
    private WareAdapter wareAdapter;

    private Category mCategory;

    private int mCurPage;
    private List<Wares> wares=new ArrayList<>();

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_category;
    }

    @Override
    protected void viewAttributes() {
        recyclerviewCategory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerviewCategory.addItemDecoration(new DividerItemDecoration());
        recyclerviewWares.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        smartRefreshLayout.setOnRefreshLoadmoreListener(this);
        smartRefreshLayout.autoRefresh();
    }

    @Override
    protected void onLazyLoad() {
        super.onLazyLoad();
        //分类列表
        requestCatoryList();
        //Banner广告位
        requestBanner();
    }
    /***
     * 分类列表
     */
    private void requestCatoryList() {
        ArrayMap<String, Object> param = new ArrayMap<>();
        param.put("token", BaseApplication.getInstance().getToken());
        getNovate().rxPost(Contants.API.CATEGORY_LIST,param, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (!TextUtils.isEmpty(response)) {
                    resetWareList(response);
                }
            }
            @Override
            public void onError(Object tag, Throwable e) {
            }

            @Override
            public void onCancel(Object tag, Throwable e) {
            }
        });
    }

    private void resetWareList(String response) {
        List<Category> categories = JSONUtil.fromJson(response, new TypeToken<List<Category>>() {
        }.getType());
        categoryAdapter = new CategoryAdapter(mActivity, R.layout.item_catory_list,
                categories);
        categoryAdapter.setOnItemClickListener(this);
        recyclerviewCategory.setAdapter(categoryAdapter);
    }

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    /***
     * 广告位
     */
    private void requestBanner() {
        ArrayMap<String, Object> param = new ArrayMap<>(1);
        param.put("type", 1);
        param.put("token", BaseApplication.getInstance().getToken());
        getNovate()
                .rxPost(Contants.API.BANNER, param, new RxStringCallback() {
                    @Override
                    public void onNext(Object tag, String response) {
                        if (!TextUtils.isEmpty(response)) {
                            List<Banner> banners =
                                    JSONUtil.fromJson(response, new TypeToken<List<Banner>>() {
                                    }.getType());
                            setUpBanner(banners);
                        }
                    }

                    @Override
                    public void onError(Object tag, Throwable e) {

                    }

                    @Override
                    public void onCancel(Object tag, Throwable e) {

                    }
                });

    }

    /***
     * 初始化Banner广告
     * @param banners 广告集合
     */
    private void setUpBanner(List<Banner> banners) {
        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        convenientBanner.setPages(
                new CBViewHolderCreator<BannerHolder>() {
                    @Override
                    public BannerHolder createHolder() {
                        return new BannerHolder();
                    }
                }, banners)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        convenientBanner.stopTurning();
    }

    @Override
    public void onResume() {
        super.onResume();
        convenientBanner.startTurning(2000);
    }

    TextView mTextView;

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        /***
         * url: WARES_LIST
         *categoryId
         * curPage
         * pageSize
         */
        if (mTextView != null && !mTextView.getTag().toString().equals(position + "")) {
            mTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.defaultTextColor));
            mTextView = (TextView) view;
            mTextView.setTag(position);
            ((TextView) view).setTextColor(ContextCompat.getColor(mActivity, R.color.red));
        } else {
            mTextView = (TextView) view;
            mTextView.setTag(position);
            mTextView.setTextColor(ContextCompat.getColor(mActivity, R.color.red));
        }
        mCategory = categoryAdapter.getDatas().get(position);
        requestWareList(Contants.LOAD_MODE.REFRESH);
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

    /***
     * 热卖列表
     */
    private void requestWareList(final Contants.LOAD_MODE mode) {
        if (mode == Contants.LOAD_MODE.REFRESH) {
            mCurPage = 1;
        } else {
            mCurPage++;
        }
        ArrayMap<String, Object> param = new ArrayMap<>(3);
        param.put("categoryId", mCategory == null ? 1 : mCategory.getId());
        param.put("curPage", mCurPage);
        param.put("pageSize", PAGE_SIZE_NUM);
        param.put("token", BaseApplication.getInstance().getToken());
        getNovate()
                .rxPost(Contants.API.WARES_LIST, param, new RxStringCallback() {
                    @Override
                    public void onNext(Object tag, String response) {
                        if (!TextUtils.isEmpty(response)) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response);

                                wares = JSONUtil.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<List<Wares>>() {
                                }.getType());
//                            DataParserUtils.parseWares(response);
                                if (mode == Contants.LOAD_MODE.REFRESH) {
                                    wareAdapter = new WareAdapter(mActivity, R.layout.template_hot_wares, wares);
                                    recyclerviewWares.setAdapter(wareAdapter);
                                    wareAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                                            WareDetailctivity.actionStart(mActivity, wareAdapter.getDatas().get(position));
                                        }
                                        @Override
                                        public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                                            return false;
                                        }
                                    });
                                } else {
                                    wareAdapter.getDatas().addAll(wares);
                                    wareAdapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            loadFinish();
                        }
                    }

                    @Override
                    public void onError(Object tag, Throwable e) {
                        loadFinish();
                    }

                    @Override
                    public void onCancel(Object tag, Throwable e) {
                        loadFinish();
                    }
                });

    }

    private void loadFinish() {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishLoadmore();
            smartRefreshLayout.finishRefresh();
        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        requestWareList(Contants.LOAD_MODE.LOAD_MORE);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        requestWareList(Contants.LOAD_MODE.REFRESH);
    }

}
