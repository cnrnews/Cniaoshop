package cnaio.imooc.com.cniao5.bannerholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;

import cnaio.imooc.com.cniao5.model.Banner;

/**
 * @author：lihl on 2017/11/19 16:29
 * @email：1601796593@qq.com
 */
public class BannerHolder implements Holder<Banner> {
    private ImageView imageView;
    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }
    @Override
    public void UpdateUI(Context context, int position, Banner banner) {
        Glide.with(context)
                .load(banner.getImgUrl())
                .crossFade()
                .into(imageView);
    }
}
