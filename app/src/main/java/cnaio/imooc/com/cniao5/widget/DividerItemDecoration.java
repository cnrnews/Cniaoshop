package cnaio.imooc.com.cniao5.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author：lihl on 2017/11/19 20:32
 * @email：1601796593@qq.com
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(1,1,1,1);
    }
}
