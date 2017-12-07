package cnaio.imooc.com.cniao5.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.w4lle.library.NineGridAdapter;
import com.w4lle.library.NineGridlayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import cnaio.imooc.com.cniao5.R;
import cnaio.imooc.com.cniao5.model.Order;
import cnaio.imooc.com.cniao5.model.OrderItem;
import cnaio.imooc.com.cniao5.utils.GlideUtils;

/**
*作者:candy
*创建时间:2017/12/3 15:05
*邮箱:1601796593@qq.com
*功能描述:
 * 我的订单
**/
public class MyOrderAdapter extends CommonAdapter<Order> {
    @Override
    protected void convert(ViewHolder viewHoder, Order item, int position) {
        viewHoder.setText(R.id.txt_order_num,"订单号："+item.getOrderNum());
        viewHoder.setText(R.id.txt_order_money,"实付金额： ￥："+item.getAmount());
        TextView txtStatus = viewHoder.getView(R.id.txt_status);
        switch (item.getStatus()){

            case Order.STATUS_SUCCESS:
                txtStatus.setText("成功");
                txtStatus.setTextColor(Color.parseColor("#ff4CAF50"));
                break;
            case Order.STATUS_PAY_FAIL:
                txtStatus.setText("支付失败");
                txtStatus.setTextColor(Color.parseColor("#ffF44336"));
                break;

            case Order.STATUS_PAY_WAIT:
                txtStatus.setText("等待支付");
                txtStatus.setTextColor(Color.parseColor("#ffFFEB3B"));
                break;

        }
        NineGridlayout nineGridlayout= (NineGridlayout) viewHoder.getView(R.id.iv_ngrid_layout);
        nineGridlayout.setGap(5);
        nineGridlayout.setDefaultWidth(50);
        nineGridlayout.setDefaultHeight(50);
        nineGridlayout.setAdapter(new OrderItemAdapter(mContext,item.getItems()));

    }

    public MyOrderAdapter(Context context, List<Order> datas) {
        super(context, R.layout.template_my_orders,datas);
    }


    class  OrderItemAdapter extends NineGridAdapter {

        private  List<OrderItem> items ;

        public OrderItemAdapter(Context context, List<OrderItem> items) {
            super(context, items);
            this.items = items;
        }

        @Override
        public int getCount() {
            return (items == null) ? 0 : items.size();
        }

        @Override
        public String getUrl(int position) {

            OrderItem item = getItem(position);

            return  item.getWares().getImgUrl();

        }

        @Override
        public OrderItem getItem(int position) {
            return (items == null) ? null : items.get(position);
        }

        @Override
        public long getItemId(int position) {

            OrderItem item = getItem(position);
            return item==null?0:item.getId();
        }

        @Override
        public View getView(int i, View view) {

            ImageView iv = new ImageView(context);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setBackgroundColor(Color.parseColor("#f5f5f5"));
            GlideUtils.loadImageView(context,  getUrl(i),iv);
            return iv;
        }


    }










}
