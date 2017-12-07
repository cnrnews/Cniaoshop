package cnaio.imooc.com.cniao5.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tamic.novate.Novate;

import butterknife.ButterKnife;
import cnaio.imooc.com.cniao5.utils.Contants;
/**
 * @author：lihl on 2017/11/26 22:27
 * @email：1601796593@qq.com
 */
public abstract class BaseActivity extends AppCompatActivity {
    public Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutResouceId());
        ButterKnife.bind(this);
        activity=this;
        initData(getIntent().getExtras());
        viewAttributes();
        setListener();
    }
    /**
     * 初始化数据
     * @author 漆可
     * @date 2016-5-26 下午3:57:48
     * @param arguments 接收到的从其他地方传递过来的参数
     */
    protected void initData(Bundle arguments)
    {

    }
    /**
     * 设置监听事件
     * @author 漆可
     * @date 2016-5-26 下午3:59:36
     */
    protected void setListener()
    {

    }
    /**
     * 设置根布局资源id
     * @author 漆可
     * @date 2016-5-26 下午3:57:09
     * @return
     */
    protected abstract int setLayoutResouceId();
    /**组件属性*/
    protected abstract void viewAttributes();
    protected Novate getNovate() {
        return
                new Novate.Builder(this)
                        .baseUrl(Contants.API.BASE_URL)
                        .build();
    }
    public void startActivity(Class clas)
    {
        Intent intent=new Intent(this,clas);
        startActivity(intent);
    }
    public void startActivityForResult(Class clas)
    {
        Intent intent=new Intent(this,clas);
        startActivityForResult(intent, Contants.REQUEST_CODE);
    }

}
