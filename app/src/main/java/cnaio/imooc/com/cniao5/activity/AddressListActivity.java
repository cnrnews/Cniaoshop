package cnaio.imooc.com.cniao5.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cnaio.imooc.com.cniao5.R;
import cnaio.imooc.com.cniao5.adapter.AddressAdapter;
import cnaio.imooc.com.cniao5.base.BaseActivity;
import cnaio.imooc.com.cniao5.base.BaseApplication;
import cnaio.imooc.com.cniao5.model.Address;
import cnaio.imooc.com.cniao5.model.BaseRespMsg;
import cnaio.imooc.com.cniao5.utils.Contants;
import cnaio.imooc.com.cniao5.utils.JSONUtil;
import cnaio.imooc.com.cniao5.widget.CNiaoToolBar;
import cnaio.imooc.com.cniao5.widget.DividerItemDecoration;


/**
 * 作者:candy
 * 创建时间:2017/12/3 8:12
 * 邮箱:1601796593@qq.com
 * 功能描述:
 * 收货地址管理器
 **/
public class AddressListActivity extends BaseActivity implements AddressAdapter.AddressLisneter {


    private static final int EDIT_ADDRESS =0x110 ;
    @BindView(R.id.toolbar)
    CNiaoToolBar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private AddressAdapter mAdapter;

    @Override
    protected int setLayoutResouceId() {
        return R.layout.activity_address_list;
    }

    @Override
    protected void viewAttributes() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setRightButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toAddActivity();
            }
        });
        initAddress();
    }
    private void toAddActivity() {
        Intent intent = new Intent(this, AddressAddActivity.class);
        startActivityForResult(intent, Contants.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        initAddress();
    }

    private void initAddress() {
        Map<String, Object> params = new HashMap<>(1);
        params.put("user_id", BaseApplication.getInstance().getUser().getId());
        params.put("token", BaseApplication.getInstance().getToken());
        getNovate().rxGet(Contants.API.ADDRESS_LIST, params, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (!TextUtils.isEmpty(response)) {
                    List<Address> addresses = JSONUtil.fromJson(response, new TypeToken<List<Address>>() {
                    }.getType());
                    showAddress(addresses);
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

    private void showAddress(List<Address> addresses) {
        Collections.sort(addresses);
        Log.i("novateCallback", "showAddress: "+addresses.size());

        if (mAdapter == null) {
            mAdapter = new AddressAdapter(activity, R.layout.template_address, addresses, this);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(AddressListActivity.this));
            recyclerView.addItemDecoration(new DividerItemDecoration());
        } else {
            mAdapter.getDatas().clear();
            mAdapter.getDatas().addAll(addresses);
            recyclerView.setAdapter(mAdapter);
        }
    }
    public void setDefaultAddress(Address address) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", address.getId());
        params.put("consignee", address.getConsignee());
        params.put("phone", address.getPhone());
        params.put("addr", address.getAddr());
        params.put("zip_code", address.getZipCode());
        params.put("is_default", address.getIsDefault());
        params.put("token", BaseApplication.getInstance().getToken());
        getNovate().rxGet(Contants.API.ADDRESS_LIST, params, new RxStringCallback() {

            @Override
            public void onNext(Object tag, String response) {
                if (!TextUtils.isEmpty(response)) {
                    initAddress();
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

    @Override
    public void setDefault(Address address) {
        setDefaultAddress(address);
    }
    @Override
    public void delAddress(Address address) {
        deleteAddress(address);
    }

    @Override
    public void updateAddress(Address address) {
        Intent intent= new Intent(this,AddressAddActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("address",address);
        intent.putExtras(bundle);
        startActivityForResult(intent,EDIT_ADDRESS);
    }
    public void deleteAddress(Address address) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", address.getId());
        params.put("token", BaseApplication.getInstance().getToken());
        getNovate().rxPost(Contants.API.ADDRESS_DELETE, params, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                Log.i(TAG, "onNext: " + response);
                if (!TextUtils.isEmpty(response)) {
                    BaseRespMsg baseRespMsg = JSONUtil.fromJson(response, BaseRespMsg.class);
                    if (baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS) {
                        initAddress();
                    }
                }
            }

            @Override
            public void onError(Object tag, Throwable e) {

                Log.i(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }
        });
    }
}
