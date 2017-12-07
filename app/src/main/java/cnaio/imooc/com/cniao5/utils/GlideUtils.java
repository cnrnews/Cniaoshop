package cnaio.imooc.com.cniao5.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * @author：lihl on 2017/11/19 18:55
 * @email：1601796593@qq.com
 */
public class GlideUtils {

    public static void loadImageView(Context context, String url, ImageView imageView)
    {
        Glide.with(context)
                .load(url)
                .crossFade()
                .into(imageView);
    }
}
