package cnaio.imooc.com.cniao5.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.citywheel.CityPickerView;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cnaio.imooc.com.cniao5.R;
import cnaio.imooc.com.cniao5.base.BaseActivity;
import cnaio.imooc.com.cniao5.base.BaseApplication;
import cnaio.imooc.com.cniao5.model.Address;
import cnaio.imooc.com.cniao5.model.BaseRespMsg;
import cnaio.imooc.com.cniao5.utils.Contants;
import cnaio.imooc.com.cniao5.utils.JSONUtil;
import cnaio.imooc.com.cniao5.utils.ToastUtils;
import cnaio.imooc.com.cniao5.widget.CNiaoToolBar;
import cnaio.imooc.com.cniao5.widget.ClearEditText;

/**
 * 作者:candy
 * 创建时间:2017/12/3 8:28
 * 邮箱:1601796593@qq.com
 * 功能描述:
 * 添加收货地址
 **/
public class AddressAddActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    CNiaoToolBar toolbar;
    @BindView(R.id.edittxt_consignee)
    ClearEditText edittxtConsignee;
    @BindView(R.id.edittxt_phone)
    ClearEditText edittxtPhone;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.ll_city_picker)
    LinearLayout llCityPicker;
    @BindView(R.id.edittxt_add)
    ClearEditText edittxtAdd;
    private CityPickerView cityPicker;


    @Override
    protected void initData(Bundle arguments) {
        super.initData(arguments);
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            Address address = (Address) bundle.getSerializable("address");
            String strAdr = address.getAddr();
            String part1 = strAdr.substring(0, strAdr.lastIndexOf("区") + 1);
            String part2 = strAdr.substring(strAdr.lastIndexOf("区") + 1, strAdr.length());
            edittxtPhone.setText(address.getPhone());
            txtAddress.setText(part1);
            edittxtConsignee.setText(address.getConsignee());
            edittxtAdd.setText(part2);
        }
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.activity_address_add;
    }

    @Override
    protected void viewAttributes() {
        initCityPickerView();


    }

    private void initCityPickerView() {
        //详细属性设置，如果不需要自定义样式的话，可以直接使用默认的，去掉下面的属性设置，直接build()即可。
        CityConfig cityConfig = new CityConfig.Builder(this)
                .title("选择地区")
                .titleBackgroundColor("#E9E9E9")
                .textSize(18)
                .titleTextColor("#FF585858")
                .textColor("#FF585858")
                .confirTextColor("#0000FF")
                .cancelTextColor("#000000")
                .province("江苏")
                .city("常州")
                .district("新北区")
                .visibleItemsCount(5)
                .provinceCyclic(true)
                .cityCyclic(true)
                .showBackground(true)
                .districtCyclic(true)
                .itemPadding(5)
                .setCityInfoType(CityConfig.CityInfoType.BASE)
                .setCityWheelType(CityConfig.WheelType.PRO_CITY_DIS)
                .build();
        //配置属性
        cityPicker = new CityPickerView(cityConfig);
    }

    @Override
    public void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setRightButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddress();
            }
        });
        cityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                super.onSelected(province, city, district);

                StringBuilder builder = new StringBuilder();
                if (province != null) {
                    builder.append(province.getName());
                }

                if (city != null) {
                    builder.append("-" + city.getName());
                }

                if (district != null) {
                    builder.append("-" + district.getName());
                }
                txtAddress.setText(builder.toString());
            }

            @Override
            public void onCancel() {
                super.onCancel();
            }
        });
    }

    /***
     * 上传收货地址
     */
    public void createAddress() {
        String consignee = edittxtConsignee.getText().toString();
        String phone = edittxtPhone.getText().toString();
        String address = txtAddress.getText().toString() + edittxtAdd.getText().toString();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", BaseApplication.getInstance().getUser().getId());
        params.put("token", BaseApplication.getInstance().getToken());
        params.put("consignee", consignee);
        params.put("phone", phone);
        params.put("addr", address);
        params.put("zip_code", "000000");
        getNovate().rxPost(Contants.API.ADDRESS_CREATE, params, new RxStringCallback() {

            @Override
            public void onNext(Object tag, String response) {
                if (!TextUtils.isEmpty(response)) {
                    BaseRespMsg baseRespMsg = JSONUtil.fromJson(response, BaseRespMsg.class);

                    if (baseRespMsg != null && baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS) {
                        ToastUtils.show(AddressAddActivity.this, "添加成功.");
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        ToastUtils.show(AddressAddActivity.this, "添加失败,请稍后再试.");
                    }

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

    @OnClick(R.id.ll_city_picker)
    public void onViewClicked() {
        cityPicker.show();
    }


}
