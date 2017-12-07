package cnaio.imooc.com.cniao5.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.Serializable;

import butterknife.BindView;
import cnaio.imooc.com.cniao5.R;
import cnaio.imooc.com.cniao5.base.BaseActivity;
import cnaio.imooc.com.cniao5.model.Wares;
import cnaio.imooc.com.cniao5.utils.CartProvider;
import cnaio.imooc.com.cniao5.utils.Contants;
import cnaio.imooc.com.cniao5.utils.ToastUtils;
import cnaio.imooc.com.cniao5.widget.CNiaoToolBar;

/**
 * 作者:candy
 * 创建时间:2017/12/3 19:01
 * 邮箱:1601796593@qq.com
 * 功能描述:商品详情
 **/
public class WareDetailctivity extends BaseActivity {

    @BindView(R.id.toolbar)
    CNiaoToolBar toolbar;
    @BindView(R.id.webview)
    WebView webview;
    private Wares wares;


    private ProgressDialog dialog;


    @Override
    protected void initData(Bundle arguments) {
        super.initData(arguments);
        Serializable serializable = getIntent().getSerializableExtra("ware");
        if (serializable == null) {
            finish();
        }
        wares = (Wares) serializable;
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.activity_ware_detail;
    }

    @Override
    protected void viewAttributes() {
        toolbar.setRightButton(ContextCompat.getDrawable(this, R.drawable.icon_share));
        dialog = new ProgressDialog(this);
        dialog.setMessage("loading");
        dialog.show();
        initWebSeeting();
    }

    public static void actionStart(Context context, Wares wares) {
        Intent intent = new Intent(context, WareDetailctivity.class);
        intent.putExtra("ware", wares);
        context.startActivity(intent);
    }

    private void initWebSeeting() {
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);

        final WebAppInterface webAppInterface = new WebAppInterface(this);
        webview.addJavascriptInterface(webAppInterface, "appInterface");
        webview.loadUrl(Contants.API.WARES_DETAIL);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                webAppInterface.showDetail(wares.getId());
            }
        });
    }

    class WebAppInterface {

        CartProvider cartProvider;

        public WebAppInterface(Context context) {
            cartProvider = new CartProvider(context);
        }

        @JavascriptInterface
        public void showDetail(int id) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webview.loadUrl("javascript:showDetail(" + wares.getId() + ")");
                }
            });
        }

        @JavascriptInterface
        public void buy(int id) {
        }

        @JavascriptInterface
        public void addToCart(int id) {
            cartProvider.put(wares);
            ToastUtils.show(WareDetailctivity.this,
                    "添加购物车成功");
        }
    }

    @Override
    public void setListener() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
