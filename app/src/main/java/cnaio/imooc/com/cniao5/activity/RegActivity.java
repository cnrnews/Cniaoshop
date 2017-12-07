package cnaio.imooc.com.cniao5.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cnaio.imooc.com.cniao5.R;
import cnaio.imooc.com.cniao5.base.BaseActivity;
import cnaio.imooc.com.cniao5.utils.ToastUtils;
import cnaio.imooc.com.cniao5.widget.CNiaoToolBar;
import cnaio.imooc.com.cniao5.widget.ClearEditText;

/**
 * 作者:candy
 * 创建时间:2017/11/28 21:52
 * 邮箱:1601796593@qq.com
 * 功能描述:注册
 **/
public class RegActivity extends BaseActivity {

    private static final String TAG = "RegActivity";
    // 默认使用中国区号
    private static final String DEFAULT_COUNTRY_ID = "42";
    @BindView(R.id.toolbar)
    CNiaoToolBar toolbar;
    @BindView(R.id.txtCountry)
    TextView txtCountry;
    @BindView(R.id.txtCountryCode)
    TextView txtCountryCode;
    @BindView(R.id.edittxt_phone)
    ClearEditText edittxtPhone;
    @BindView(R.id.edittxt_pwd)
    ClearEditText edittxtPwd;


    private ProgressDialog dialog;
    private SMSEvenHanlder evenHanlder;


    @Override
    protected int setLayoutResouceId() {
        return R.layout.activity_reg;
    }

    @Override
    protected void viewAttributes() {
        // 创建EventHandler对象
        evenHanlder = new SMSEvenHanlder();
        SMSSDK.registerEventHandler(evenHanlder);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setRightButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SMSSDK.getVerificationCode();
                getCode();

            }
        });
        String[] country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
        if (country != null) {
            txtCountryCode.setText("+" + country[1]);
            txtCountry.setText(country[0]);
        }
    }
    class SMSEvenHanlder extends EventHandler {
        @Override
        public void afterEvent(final int event, final int result,
                               final Object data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                            onCountryListGot((ArrayList<HashMap<String, Object>>) data);
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            // 请求验证码后，跳转到验证码填写页面
                            afterVerificationCodeRequested((Boolean) data);
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {

                        }
                    } else {
                        // 根据服务器返回的网络错误，给toast提示
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;

                            JSONObject object = new JSONObject(
                                    throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)) {
                                ToastUtils.show(RegActivity.this, des);
                                return;
                            }
                        } catch (Exception e) {
                        }

                    }


                }
            });
        }
    }



    private void getCode() {

        String phone = edittxtPhone.getText().toString().trim().replaceAll("\\s*", "");
        String code = txtCountryCode.getText().toString().trim();
        String pwd = edittxtPwd.getText().toString().trim();

        checkPhoneNum(phone, code);

        //not 86   +86
        SMSSDK.getVerificationCode(code, phone);

    }


    private void checkPhoneNum(String phone, String code) {
        if (code.startsWith("+")) {
            code = code.substring(1);
        }

        if (TextUtils.isEmpty(phone)) {
            ToastUtils.show(this, "请输入手机号码");
            return;
        }

        if (code == "86") {
            if (phone.length() != 11) {
                ToastUtils.show(this, "手机号码长度不对");
                return;
            }

        }

        String rule = "^1(3|5|7|8|4)\\d{9}";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(phone);

        if (!m.matches()) {
            ToastUtils.show(this, "您输入的手机号码格式不正确");
            return;
        }

    }

    private void onCountryListGot(ArrayList<HashMap<String, Object>> countries) {
        // 解析国家列表
        for (HashMap<String, Object> country : countries) {
            String code = (String) country.get("zone");
            String rule = (String) country.get("rule");
            if (TextUtils.isEmpty(code) || TextUtils.isEmpty(rule)) {
                continue;
            }
        }
    }

    /**
     * 请求验证码后，跳转到验证码填写页面
     */
    private void afterVerificationCodeRequested(boolean smart) {
        String phone = edittxtPhone.getText().toString().trim().replaceAll("\\s*", "");
        String code = txtCountryCode.getText().toString().trim();
        String pwd = edittxtPwd.getText().toString().trim();

        if (code.startsWith("+")) {
            code = code.substring(1);
        }

        Intent intent = new Intent(this, RegSecondActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("pwd", pwd);
        intent.putExtra("countryCode", code);

        startActivity(intent);
    }


    private String[] getCurrentCountry() {
        String mcc = getMCC();
        String[] country = null;
        if (!TextUtils.isEmpty(mcc)) {
            country = SMSSDK.getCountryByMCC(mcc);
        }

        if (country == null) {
            Log.w("SMSSDK", "no country found by MCC: " + mcc);
            country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
        }
        return country;
    }

    private String getMCC() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        // 返回当前手机注册的网络运营商所在国家的MCC+MNC. 如果没注册到网络就为空.
        String networkOperator = tm.getNetworkOperator();
        if (!TextUtils.isEmpty(networkOperator)) {
            return networkOperator;
        }

        // 返回SIM卡运营商所在国家的MCC+MNC. 5位或6位. 如果没有SIM卡返回空
        return tm.getSimOperator();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(evenHanlder);
    }
}
