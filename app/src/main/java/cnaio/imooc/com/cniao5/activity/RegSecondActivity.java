package cnaio.imooc.com.cniao5.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxResultCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cnaio.imooc.com.cniao5.R;
import cnaio.imooc.com.cniao5.base.BaseActivity;
import cnaio.imooc.com.cniao5.base.BaseApplication;
import cnaio.imooc.com.cniao5.model.LoginRespMsg;
import cnaio.imooc.com.cniao5.model.User;
import cnaio.imooc.com.cniao5.utils.Contants;
import cnaio.imooc.com.cniao5.utils.DESUtil;
import cnaio.imooc.com.cniao5.utils.ToastUtils;
import cnaio.imooc.com.cniao5.widget.CNiaoToolBar;
import cnaio.imooc.com.cniao5.widget.ClearEditText;
import cnaio.imooc.com.cniao5.widget.CountTimerView;

/**
 * 作者:candy
 * 创建时间:2017/11/29 21:21
 * 邮箱:1601796593@qq.com
 * 功能描述:
 * 注册第二部
 **/
public class RegSecondActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    CNiaoToolBar toolbar;
    @BindView(R.id.txtTip)
    TextView txtTip;
    @BindView(R.id.edittxt_code)
    ClearEditText edittxtCode;
    @BindView(R.id.btn_reSend)
    Button btnReSend;
    private String phone;
    private String pwd;
    private String countryCode;

    private CountTimerView countTimerView;
    private ProgressDialog dialog;
    private SMSEvenHanlder evenHanlder;

    @Override
    protected int setLayoutResouceId() {
        return R.layout.activity_reg_second;
    }

    @Override
    protected void viewAttributes() {
        phone = getIntent().getStringExtra("phone");
        pwd = getIntent().getStringExtra("pwd");
        countryCode = getIntent().getStringExtra("countryCode");

        String formatedPhone = "+" + countryCode + " " + splitPhoneNum(phone);
        String text = getString(R.string.smssdk_send_mobile_detail) + formatedPhone;
        txtTip.setText(Html.fromHtml(text));

        CountTimerView timerView = new CountTimerView(btnReSend);
        timerView.start();
        evenHanlder = new SMSEvenHanlder();
        SMSSDK.registerEventHandler(evenHanlder);

        dialog = new ProgressDialog(this);
        dialog.setMessage("正在校验验证码");


        toolbar.setRightButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCode();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @OnClick(R.id.btn_reSend)
    public void reSendCode(View view) {

        SMSSDK.getVerificationCode("+" + countryCode, phone);
        countTimerView = new CountTimerView(btnReSend, R.string.smssdk_resend_identify_code);
        countTimerView.start();

        dialog.setMessage("正在重新获取验证码");
        dialog.show();
    }

    /**
     * 分割电话号码
     */
    private String splitPhoneNum(String phone) {
        StringBuilder builder = new StringBuilder(phone);
        builder.reverse();
        for (int i = 4, len = builder.length(); i < len; i += 5) {
            builder.insert(i, ' ');
        }
        builder.reverse();
        return builder.toString();
    }


    private void submitCode() {

        String vCode = edittxtCode.getText().toString().trim();

        if (TextUtils.isEmpty(vCode)) {
            ToastUtils.show(this, R.string.smssdk_write_identify_code);
            return;
        }
        SMSSDK.submitVerificationCode(countryCode, phone, vCode);
        dialog.show();
    }


    private void doReg() {

        Map<String, Object> params = new HashMap<>(2);
        params.put("phone", phone);
        params.put("password", DESUtil.encode(Contants.DES_KEY, pwd));

        getNovate().rxPost(Contants.API.REG, params, new RxResultCallback<LoginRespMsg<User>>() {


            @Override
            public void onNext(Object tag, int code, String message, LoginRespMsg<User> userLoginRespMsg) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (userLoginRespMsg.getStatus() == LoginRespMsg.STATUS_ERROR) {
                    ToastUtils.show(RegSecondActivity.this, "注册失败:" + userLoginRespMsg.getMessage());
                    return;
                }
                BaseApplication application = BaseApplication.getInstance();
                application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());
                startActivity(new Intent(RegSecondActivity.this, MainActivity.class));
                finish();
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
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(evenHanlder);
    }
    class SMSEvenHanlder extends EventHandler {
        @Override
        public void afterEvent(final int event, final int result,
                               final Object data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (dialog != null && dialog.isShowing())
                        dialog.dismiss();

                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {


//                              HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
//                              String country = (String) phoneMap.get("country");
//                              String phone = (String) phoneMap.get("phone");

//                            ToastUtils.show(RegSecondActivity.this,"验证成功："+phone+",country:"+country);


                            doReg();
                            dialog.setMessage("正在提交注册信息");
                            dialog.show();
                            ;
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
//                                ToastUtils.show(RegActivity.this, des);
                                return;
                            }
                        } catch (Exception e) {
                        }

                    }


                }
            });
        }
    }


}
