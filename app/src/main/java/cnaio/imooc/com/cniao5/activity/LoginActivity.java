package cnaio.imooc.com.cniao5.activity;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tamic.novate.Throwable;
import com.tamic.novate.callback.RxStringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cnaio.imooc.com.cniao5.R;
import cnaio.imooc.com.cniao5.base.BaseActivity;
import cnaio.imooc.com.cniao5.base.BaseApplication;
import cnaio.imooc.com.cniao5.model.LoginRespMsg;
import cnaio.imooc.com.cniao5.model.User;
import cnaio.imooc.com.cniao5.utils.Contants;
import cnaio.imooc.com.cniao5.utils.DESUtil;
import cnaio.imooc.com.cniao5.utils.JSONUtil;
import cnaio.imooc.com.cniao5.utils.ToastUtils;
import cnaio.imooc.com.cniao5.widget.CNiaoToolBar;
import cnaio.imooc.com.cniao5.widget.ClearEditText;


public class LoginActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    CNiaoToolBar toolbar;
    @BindView(R.id.etxt_phone)
    ClearEditText etxtPhone;
    @BindView(R.id.etxt_pwd)
    ClearEditText etxtPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.txt_toReg)
    TextView txtToReg;

    @Override
    protected int setLayoutResouceId() {
        return R.layout.activity_login;
    }

    @Override
    protected void viewAttributes() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }
    @OnClick(R.id.btn_login)
    public void login(View view) {
        String phone = etxtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.show(this, "请输入手机号码");
            return;
        }
        String pwd = etxtPwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.show(this, "请输入密码");
            return;
        }

        ArrayMap<String, Object> params = new ArrayMap<>(2);
        params.put("phone", phone);
        params.put("password", DESUtil.encode(Contants.DES_KEY, pwd));
        getNovate().rxPost(Contants.API.LOGIN, params, new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                if (!TextUtils.isEmpty(response)) {
                    BaseApplication application = BaseApplication.getInstance();
                    LoginRespMsg<User> userLoginRespMsg = JSONUtil.fromJson(response,
                            new TypeToken<LoginRespMsg<User>>(){}.getType());
                    application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());
                    if (application.getIntent() == null) {
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        application.jumpToTargetActivity(LoginActivity.this);
                        finish();
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
    @OnClick(R.id.txt_toReg)
    public void register(View view) {
        startActivity(RegActivity.class);
    }
}
