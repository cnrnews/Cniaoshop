package cnaio.imooc.com.cniao5.adapter;

import android.content.Context;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import cnaio.imooc.com.cniao5.R;
import cnaio.imooc.com.cniao5.model.Category;

/**
 * @author：lihl on 2017/11/20 23:07
 * @email：1601796593@qq.com
 */
public class CategoryAdapter extends CommonAdapter<Category> {
    public CategoryAdapter(Context context, int layoutId, List<Category> datas) {
        super(context, layoutId, datas);
    }
    @Override
    protected void convert(ViewHolder holder, Category category, int position) {
        holder.setText(R.id.category_name, category.getName());
    }
}
