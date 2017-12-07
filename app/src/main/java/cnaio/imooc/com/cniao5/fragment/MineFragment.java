package cnaio.imooc.com.cniao5.fragment;


import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cnaio.imooc.com.cniao5.R;
import cnaio.imooc.com.cniao5.activity.AddressListActivity;
import cnaio.imooc.com.cniao5.activity.LoginActivity;
import cnaio.imooc.com.cniao5.activity.MyOrderActivity;
import cnaio.imooc.com.cniao5.base.BaseApplication;
import cnaio.imooc.com.cniao5.base.BaseFragment;
import cnaio.imooc.com.cniao5.model.User;
import cnaio.imooc.com.cniao5.utils.Contants;
import cnaio.imooc.com.cniao5.utils.GlideUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 作者:candy
 * 创建时间:2017/12/2 21:34
 * 邮箱:1601796593@qq.com
 * 功能描述:
 * 我的
 **/
public class MineFragment extends BaseFragment {
    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.txt_username)
    TextView txtUsername;
    @BindView(R.id.txt_my_orders)
    TextView txtMyOrders;
    @BindView(R.id.txt_my_favorite)
    TextView txtMyFavorite;
    @BindView(R.id.txt_my_address)
    TextView txtMyAddress;
    @BindView(R.id.btn_logout)
    Button btnLogout;
    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void viewAttributes() {

    }

    @Override
    protected void onLazyLoad() {
        super.onLazyLoad();

        showUser();
    }
    private void showUser() {
        User user = BaseApplication.getInstance().getUser();
        if (user == null) {
            btnLogout.setVisibility(View.GONE);
            txtUsername.setText(R.string.to_login);
        } else {
            btnLogout.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(user.getLogo_url())) {
                GlideUtils.loadImageView(mActivity, user.getLogo_url(), imgHead);
            }
            txtUsername.setText(user.getUsername());
        }
    }
    @OnClick(value = {R.id.img_head, R.id.txt_username})
    public void toLoginActivity(View view) {
        User user = BaseApplication.getInstance().getUser();
        if (null == user) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, Contants.REQUEST_CODE);
        }
    }

    @OnClick(R.id.txt_my_orders)
    public void toMyOrderActivity(View view) {
        startActivity(MyOrderActivity.class);
    }

    @OnClick(R.id.txt_my_address)
    public void toAddressActivity(View view) {

        startActivity(AddressListActivity.class);
    }

    @OnClick(R.id.txt_my_favorite)
    public void toFavoriteActivity(View view) {
//        startActivity(new Intent(getActivity(), MyFavoriteActivity.class), true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showUser();
    }

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @OnClick(R.id.btn_logout)
    public void onViewClicked() {
        BaseApplication.getInstance().clearUser();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent, Contants.REQUEST_CODE);
    }
}
