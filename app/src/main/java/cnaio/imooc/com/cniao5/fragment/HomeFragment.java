package cnaio.imooc.com.cniao5.fragment;


import android.app.ProgressDialog;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.google.gson.reflect.TypeToken;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

import butterknife.BindView;
import cnaio.imooc.com.cniao5.ItemViewDelegate.TemplateLeft;
import cnaio.imooc.com.cniao5.ItemViewDelegate.TemplateRight;
import cnaio.imooc.com.cniao5.R;
import cnaio.imooc.com.cniao5.activity.WareListActivity;
import cnaio.imooc.com.cniao5.bannerholder.BannerHolder;
import cnaio.imooc.com.cniao5.base.BaseApplication;
import cnaio.imooc.com.cniao5.base.BaseFragment;
import cnaio.imooc.com.cniao5.model.Banner;
import cnaio.imooc.com.cniao5.model.Campaign;
import cnaio.imooc.com.cniao5.utils.Contants;
import cnaio.imooc.com.cniao5.utils.JSONUtil;
import cnaio.imooc.com.cniao5.widget.DividerItemDecoration;

/**
 * 作者:candy
 * 创建时间:2017/11/30 23:02
 * 邮箱:1601796593@qq.com
 * 功能描述:
 * 首页
 **/
public class HomeFragment extends BaseFragment {


    @BindView(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_home;
    }
    @Override
    protected void viewAttributes() {
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerview.addItemDecoration(new DividerItemDecoration());
    }

    @Override
    protected void onLazyLoad() {
        super.onLazyLoad();

        requestBanner();
        requestData();
    }
    private void requestBanner() {
        ArrayMap<String, Object> param = new ArrayMap<>(1);
        param.put("type", 1);
        param.put("token", BaseApplication.getInstance().getToken());
        getNovate()
                .rxPost(Contants.API.BANNER, param, new RxStringCallback() {
                    @Override
                    public void onNext(Object tag, String response) {
                        if (!TextUtils.isEmpty(response)) {
                            List<Banner> banners = JSONUtil.fromJson(response,new TypeToken<List<Banner>>(){}.getType());
//                            DataParserUtils.parseBanners(response);
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
     * 数据请求
     */
    private void requestData() {
        final ProgressDialog progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage("loading...");
        progressDialog.setCancelable(false);
        ArrayMap<String, Object> param = new ArrayMap<>(1);
        param.put("token", BaseApplication.getInstance().getToken());
        getNovate()
                .rxGet(Contants.API.CAMPAIGN_HOME, param, new RxStringCallback() {
                    @Override
                    public void onStart(Object tag) {
                        super.onStart(tag);
                        progressDialog.show();
                    }

                    @Override
                    public void onError(Object tag, Throwable e) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancel(Object tag, Throwable e) {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onNext(Object tag, String response) {
                        if (!TextUtils.isEmpty(response)) {
                            List<Campaign> campaigns =JSONUtil.fromJson(response,new TypeToken<List<Campaign>>(){}.getType());
//                            DataParserUtils.parseCampaigns(response);
                            MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(getActivity(), campaigns);
                            TemplateLeft templateLeft = new TemplateLeft(getActivity());
                            TemplateRight templateRight = new TemplateRight(getActivity());
                            adapter.addItemViewDelegate(templateLeft);
                            adapter.addItemViewDelegate(templateRight);
                            recyclerview.setAdapter(adapter);
                            templateLeft.setListener(new TemplateLeft.onSingleClickListener() {
                                @Override
                                public void onClick(int tag, Campaign campaign) {
                                    WareListActivity.actionStart(mActivity, campaign.getId());
                                }
                            });
                            templateRight.setListener(new TemplateLeft.onSingleClickListener() {
                                @Override
                                public void onClick(int tag, Campaign campaign) {
                                    WareListActivity.actionStart(mActivity, campaign.getId());
                                }
                            });
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
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
}
