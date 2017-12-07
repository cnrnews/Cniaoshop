package cnaio.imooc.com.cniao5.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.Iterator;
import java.util.List;

import cnaio.imooc.com.cniao5.R;
import cnaio.imooc.com.cniao5.model.ShoppingCart;
import cnaio.imooc.com.cniao5.utils.CartProvider;
import cnaio.imooc.com.cniao5.utils.GlideUtils;
import cnaio.imooc.com.cniao5.widget.AmountView;


/**
 * Created by <a href="http://www.cniao5.com">菜鸟窝</a>
 * 一个专业的Android开发在线教育平台
 */
public class CartAdapter extends CommonAdapter<ShoppingCart> {


    private CheckBox mCheckBox;
    private TextView mTotalPrice;

    private CartProvider cartProvider;

    public CartAdapter(Context context, int layoutId, List<ShoppingCart> datas, CheckBox checkBox, TextView totalPrice) {
        super(context, layoutId, datas);
        mCheckBox = checkBox;
        mTotalPrice = totalPrice;

        cartProvider = new CartProvider(context);
        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAllCheckState(mCheckBox.isChecked());
                showTotalPrice();
            }
        });
        showTotalPrice();
    }

    @Override
    protected void convert(ViewHolder holder, final ShoppingCart shoppingCart, final int position) {

        GlideUtils.loadImageView(mContext, shoppingCart.getImgUrl(), (ImageView) holder.getView(R.id.drawee_view));
        holder.setText(R.id.text_title, shoppingCart.getName());
        holder.setText(R.id.text_price, shoppingCart.getPrice() + "");
        holder.setChecked(R.id.checkbox, shoppingCart.isChecked());

        //购物车数量更新  更新总额
        AmountView amountView = holder.getView(R.id.num_control);
        amountView.setEditCount(shoppingCart.getCount());
        amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                shoppingCart.setCount(amount);
                cartProvider.update(shoppingCart);
                showTotalPrice();
            }
        });

        //列表点击事件  更新checkbox状态->更新总额
        final CheckBox itemCheckBox = holder.getView(R.id.checkbox);
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemCheckBox.setChecked(!itemCheckBox.isChecked());
                shoppingCart.setIsChecked(itemCheckBox.isChecked());
                cartProvider.update(shoppingCart);
//                notifyItemChanged(position);
                updateCheckAllState();
                showTotalPrice();
            }
        });
    }



    /***
     * 更新每个Item checkbox选中状态
     * @param checked
     */
    public void checkAllCheckState(boolean checked) {
        if (isNull()) {
            return;
        }
        for (ShoppingCart item : mDatas) {
            item.setIsChecked(checked);
            cartProvider.update(item);
        }
        notifyDataSetChanged();
    }

    /***
     * 删除购物车
     */
    public void delCart()
    {
        if (isNull())
        {
            return;
        }
        for ( Iterator<ShoppingCart> iterator=mDatas.iterator();iterator.hasNext();)
        {
            ShoppingCart shoppingCart=iterator.next();
            if (shoppingCart.isChecked())
            {
                int position=mDatas.indexOf(shoppingCart);
                cartProvider.delete(shoppingCart);
                iterator.remove();
                notifyItemRemoved(position);
            }
        }

    }


    /***
     * 更新全选状态
     */
    public void updateCheckAllState() {
        boolean allCheck = true;
        if (isNull()) {
            return;
        }
        for (ShoppingCart item : mDatas) {
            if (!item.isChecked()) {
                allCheck = false;
                break;
            }
        }
        mCheckBox.setChecked(allCheck);
    }


    /**
     * 获取总额
     *
     * @return 总额
     */
    public float getTotalPrice() {
        float totalPrice = 0;
        if (isNull()) {
            return totalPrice;
        }
        for (ShoppingCart item : mDatas) {
            if (item.isChecked()) {
                totalPrice += item.getPrice() * item.getCount();
            }
        }
        return totalPrice;
    }

    /***
     * 购物车是否为空
     * @return 购物车是否为空
     */
    public boolean isNull() {
        return mDatas == null || mDatas.size() == 0;
    }

    /***
     * 显示总额
     */
    public void showTotalPrice() {
        float totalPrice = getTotalPrice();
        mTotalPrice.setText("合計:$" + String.valueOf(totalPrice));
    }
}
