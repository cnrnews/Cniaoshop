package cnaio.imooc.com.cniao5.fragment;


import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import cnaio.imooc.com.cniao5.R;
import cnaio.imooc.com.cniao5.adapter.CartAdapter;
import cnaio.imooc.com.cniao5.base.BaseFragment;
import cnaio.imooc.com.cniao5.model.ShoppingCart;
import cnaio.imooc.com.cniao5.utils.CartProvider;
import cnaio.imooc.com.cniao5.widget.CNiaoToolBar;
import cnaio.imooc.com.cniao5.widget.DividerItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends BaseFragment {

    private static final int ACTION_EDIT = 1;
    private static final int ACTION_COMPLETE = 2;
    @BindView(R.id.toolbar)
    CNiaoToolBar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.checkbox_all)
    CheckBox checkboxAll;
    @BindView(R.id.txt_total)
    TextView txtTotal;
    @BindView(R.id.btn_order)
    Button btnOrder;
    @BindView(R.id.btn_del)
    Button btnDel;
    Unbinder unbinder;
    private CartAdapter cartAdapter;


    private List<ShoppingCart> shoppingCartList;


    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_cart;
    }

    @Override
    protected void viewAttributes() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration());
        //初始化Tag
        toolbar.getRightButton().setTag(ACTION_COMPLETE);
    }
    @Override
    protected void onLazyLoad() {
        super.onLazyLoad();
        CartProvider cartProvider = new CartProvider(getActivity());
        shoppingCartList = cartProvider.getAll();
        if (shoppingCartList!=null) {
            cartAdapter = new CartAdapter(mActivity, R.layout.template_cart, shoppingCartList, checkboxAll, txtTotal);
            recyclerView.setAdapter(cartAdapter);
            cartAdapter.showTotalPrice();
        }
    }
    @Override
    protected void setListener() {
        toolbar.getRightButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int action = (int) toolbar.getRightButton().getTag();
                if (ACTION_EDIT == action) {
                    completeViewState();
                } else if (ACTION_COMPLETE == action) {
                    edtiViewState();
                }
            }
        });
    }
    public static CartFragment newInstance() {
        return new CartFragment();
    }
    @OnClick({R.id.btn_order, R.id.btn_del})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_order:
                break;
            case R.id.btn_del:
                cartAdapter.delCart();
                break;
        }
    }
    /***
     *   編輯模式
     */
    public void edtiViewState() {
        toolbar.setRightButton(ContextCompat.getDrawable(getActivity(), R.drawable.icon_cart_ensure));
        txtTotal.setVisibility(View.GONE);
        btnOrder.setVisibility(View.GONE);
        btnDel.setVisibility(View.VISIBLE);
        toolbar.getRightButton().setTag(ACTION_EDIT);
        checkboxAll.setChecked(false);
        cartAdapter.checkAllCheckState(false);
    }

    /***
     *   完成模式
     */
    public void completeViewState() {
        toolbar.setRightButton(ContextCompat.getDrawable(getActivity(), R.drawable.icon_cart_edit));
        txtTotal.setVisibility(View.VISIBLE);
        btnOrder.setVisibility(View.VISIBLE);
        btnDel.setVisibility(View.GONE);
        toolbar.getRightButton().setTag(ACTION_COMPLETE);
        checkboxAll.setChecked(true);
        cartAdapter.checkAllCheckState(true);
        cartAdapter.showTotalPrice();
    }
}
