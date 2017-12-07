package cnaio.imooc.com.cniao5.ItemViewDelegate;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import cnaio.imooc.com.cniao5.R;
import cnaio.imooc.com.cniao5.model.Campaign;
import cnaio.imooc.com.cniao5.utils.GlideUtils;

/**
 * @author：lihl on 2017/11/19 20:11
 * @email：1601796593@qq.com
 */
public class TemplateRight implements ItemViewDelegate<Campaign> {


    private Context mContext;
    public TemplateRight(Context context) {

        this.mContext=context;
    }
    @Override
    public int getItemViewLayoutId() {
        return R.layout.template_home_cardview2;
    }

    @Override
    public boolean isForViewType(Campaign item, int position) {
        return position%2!=0;
    }

    @Override
    public void convert(ViewHolder holder, final Campaign campaign, int position) {
        holder.setText(R.id.text_title,campaign.getTitle());

        ImageView topImageView= (ImageView) holder.getView(R.id.imgview_small_top);
        ImageView bottomImageView= (ImageView) holder.getView(R.id.imgview_small_bottom);
        ImageView bigImageView= (ImageView) holder.getView(R.id.imgview_big);

        GlideUtils.loadImageView(mContext,campaign.getCpOne().getImgUrl(), (ImageView) holder.getView(R.id.imgview_big));
        GlideUtils.loadImageView(mContext,campaign.getCpTwo().getImgUrl(), (ImageView) holder.getView(R.id.imgview_small_top));
        GlideUtils.loadImageView(mContext,campaign.getCpThree().getImgUrl(), (ImageView) holder.getView(R.id.imgview_small_bottom));

        bigImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null)
                {
                    listener.onClick(0,campaign);
                }
            }
        });
        topImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null)
                {
                    listener.onClick(1,campaign);
                }
            }
        });
        bottomImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null)
                {
                    listener.onClick(2,campaign);
                }
            }
        });
    }
    private TemplateLeft.onSingleClickListener listener;
    public void setListener(TemplateLeft.onSingleClickListener listener) {
        this.listener = listener;
    }
    public  interface onSingleClickListener
    {
        void onClick(int tag,Campaign campaign);
    }
}
