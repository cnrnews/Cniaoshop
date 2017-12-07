package cnaio.imooc.com.cniao5.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import cnaio.imooc.com.cniao5.R;
import cnaio.imooc.com.cniao5.model.Wares;
import cnaio.imooc.com.cniao5.utils.CartProvider;
import cnaio.imooc.com.cniao5.utils.GlideUtils;

/**
 * 热卖
 * @author：lihl on 2017/11/20 21:32
 * @email：1601796593@qq.com
 */
public class WareAdapter extends CommonAdapter<Wares> {
    private CartProvider cartProvider;
    public WareAdapter(Context context, int layoutId, List<Wares> datas) {
        super(context, layoutId, datas);
        cartProvider=new CartProvider(context);
    }
    @Override
    protected void convert(ViewHolder holder, final Wares wares, int position) {
        GlideUtils.loadImageView(mContext,wares.getImgUrl(), (ImageView) holder.getView(R.id.drawee_view));
        holder.setText(R.id.text_title,wares.getName());
        holder.setText(R.id.text_price,"¥"+wares.getPrice()+"");
        holder.getView(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartProvider.put(wares);
                Toast.makeText(mContext,"添加购物车成功",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /***
     * 切换布局
     * @param layoutId
     */
    public void  resetLayout(int layoutId){
        this.mLayoutId = layoutId;
        notifyDataSetChanged();
    }

}
